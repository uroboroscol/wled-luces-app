package com.uroboros.wledluces;

import android.app.*;
import android.content.Intent;
import android.os.*;
import android.util.Log;
import androidx.core.app.NotificationCompat;

public class LightingService extends Service {
    private static final String TAG = "LightingService";
    private static final String CHANNEL_ID = "WLED_SERVICE_CHANNEL";
    private static final int NOTIF_ID = 1001;

    private static LightingService instance;
    public static LightingService getInstance() { return instance; }

    private final IBinder binder = new LocalBinder();
    public class LocalBinder extends Binder {
        public LightingService getService() { return LightingService.this; }
    }

    private UdpSender udpSender;
    private LucesEngine engine;
    private AudioProcessor audioProcessor;
    private PowerManager.WakeLock wakeLock;

    private AudioProcessor.AudioFeatures latestFeatures = new AudioProcessor.AudioFeatures();
    private long lastFrameTime = System.currentTimeMillis();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        udpSender = new UdpSender();
        engine = new LucesEngine(udpSender.getNumLeds());

        createNotificationChannel();
        Notification notif = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("WLED Luces Activo")
                .setContentText("Sincronizando luces con el micrófono...")
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setOngoing(true)
                .build();
        startForeground(NOTIF_ID, notif);

        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        if (pm != null) {
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "wled:lighting_lock");
            wakeLock.acquire(12 * 60 * 60 * 1000L); // 12 horas max
        }

        audioProcessor = new AudioProcessor(features -> {
            latestFeatures = features;
            long now = System.currentTimeMillis();
            float dt = Math.max(0.01f, Math.min(0.05f, (now - lastFrameTime) / 1000.0f));
            lastFrameTime = now;

            byte[] rgb = engine.render(features, dt);
            udpSender.enviar(rgb);
        });

        audioProcessor.start();
        Log.i(TAG, "LightingService creado e iniciado");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) { return binder; }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (audioProcessor != null) audioProcessor.stop();
        if (wakeLock != null && wakeLock.isHeld()) wakeLock.release();
        instance = null;
        Log.i(TAG, "LightingService destruido");
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel chan = new NotificationChannel(
                    CHANNEL_ID,
                    "WLED Servicio de Luces",
                    NotificationManager.IMPORTANCE_LOW
            );
            chan.setDescription("Mantiene el micrófono y el envío de luces activo");
            NotificationManager nm = getSystemService(NotificationManager.class);
            if (nm != null) nm.createNotificationChannel(chan);
        }
    }

    public UdpSender getUdpSender() { return udpSender; }
    public LucesEngine getEngine() { return engine; }
    public AudioProcessor getAudioProcessor() { return audioProcessor; }
    public AudioProcessor.AudioFeatures getLatestFeatures() { return latestFeatures; }
}
