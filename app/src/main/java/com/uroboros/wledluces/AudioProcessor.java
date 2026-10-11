package com.uroboros.wledluces;

import android.annotation.SuppressLint;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.util.Log;

public class AudioProcessor {
    private static final String TAG = "AudioProcessor";
    public static final int SAMPLE_RATE = 44100;
    public static final int HOP = 1024;

    public static class AudioFeatures {
        public float total = 0f;
        public float kickEnergy = 0f;
        public boolean kick = false;
        public boolean hat = false;
        public float vocal = 0f;
        public float[] espectro = new float[16];
    }

    private AudioRecord audioRecord;
    private Thread recordThread;
    private volatile boolean isRunning = false;
    private float sens = 2.0f; // Sensibilidad base mejorada

    private final float[] real = new float[HOP];
    private final float[] imag = new float[HOP];
    private final float[] window = new float[HOP];
    private final float[] mag = new float[HOP / 2];

    private float kickAvg = 0.05f;
    private float hatAvg = 0.05f;
    private long lastKickTime = 0;
    private long lastHatTime = 0;

    public interface OnAudioFrameListener {
        void onFrame(AudioFeatures features);
    }
    private OnAudioFrameListener listener;

    public AudioProcessor(OnAudioFrameListener listener) {
        this.listener = listener;
        for (int i = 0; i < HOP; i++) {
            window[i] = (float) (0.5 * (1.0 - Math.cos(2.0 * Math.PI * i / (HOP - 1))));
        }
    }

    public void setSens(float s) {
        this.sens = Math.max(0.1f, Math.min(20.0f, s));
    }
    public float getSens() { return sens; }

    @SuppressLint("MissingPermission")
    public synchronized void start() {
        if (isRunning) return;
        int minBuf = AudioRecord.getMinBufferSize(
                SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT
        );
        int bufSize = Math.max(minBuf, HOP * 4);

        int audioSource = MediaRecorder.AudioSource.MIC;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            audioSource = MediaRecorder.AudioSource.UNPROCESSED; // Audio puro sin cancelacion agresiva
        }

        try {
            audioRecord = new AudioRecord(
                    audioSource,
                    SAMPLE_RATE,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    bufSize
            );
        } catch (Exception e) {
            audioRecord = null;
        }

        // Fallback a MIC normal si UNPROCESSED falla
        if (audioRecord == null || audioRecord.getState() != AudioRecord.STATE_INITIALIZED) {
            try {
                audioRecord = new AudioRecord(
                        MediaRecorder.AudioSource.MIC,
                        SAMPLE_RATE,
                        AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_PCM_16BIT,
                        bufSize
                );
            } catch (Exception e) {
                Log.e(TAG, "No se pudo iniciar AudioRecord: " + e.getMessage());
                return;
            }
        }

        if (audioRecord.getState() != AudioRecord.STATE_INITIALIZED) {
            Log.e(TAG, "AudioRecord no inicializado");
            return;
        }

        try {
            audioRecord.startRecording();
            isRunning = true;
            recordThread = new Thread(this::recordLoop, "AudioRecordThread");
            recordThread.start();
            Log.i(TAG, "Captura de micrófono iniciada (Alta Sensibilidad)");
        } catch (Exception e) {
            Log.e(TAG, "Error startRecording: " + e.getMessage());
        }
    }

    public synchronized void stop() {
        isRunning = false;
        if (recordThread != null) {
            try { recordThread.join(500); } catch (Exception ignored) {}
            recordThread = null;
        }
        if (audioRecord != null) {
            try {
                audioRecord.stop();
                audioRecord.release();
            } catch (Exception ignored) {}
            audioRecord = null;
        }
    }

    private void recordLoop() {
        short[] buffer = new short[HOP];
        // Factor de pre-amplificación digital para compensar micrófonos móviles
        final float DIGITAL_BOOST = 6.0f;

        while (isRunning) {
            int read = audioRecord.read(buffer, 0, HOP);
            if (read != HOP) continue;

            float effectiveGain = sens * DIGITAL_BOOST;
            float sumSq = 0f;

            for (int i = 0; i < HOP; i++) {
                float sample = (buffer[i] / 32768.0f) * effectiveGain;
                real[i] = sample * window[i];
                imag[i] = 0f;
                sumSq += sample * sample;
            }
            float rms = (float) Math.sqrt(sumSq / HOP);

            FastFft.transform(real, imag);

            for (int i = 0; i < HOP / 2; i++) {
                mag[i] = (float) Math.sqrt(real[i] * real[i] + imag[i] * imag[i]) * 2.0f / HOP;
            }

            AudioFeatures f = new AudioFeatures();
            f.total = Math.min(1.0f, rms * 4.0f);

            // 16 bandas de espectro de frecuencia
            int binsPerBand = (HOP / 2) / 16;
            for (int b = 0; b < 16; b++) {
                float sum = 0f;
                for (int k = 0; k < binsPerBand; k++) {
                    sum += mag[b * binsPerBand + k];
                }
                // Escalamiento logarítmico para visibilidad vibrante
                f.espectro[b] = Math.min(1.0f, (sum / binsPerBand) * 12.0f);
            }

            // Detección de Kick (Bajo: 35Hz - 130Hz)
            int kStart = (int) (35f / (SAMPLE_RATE / (float) HOP));
            int kEnd = Math.max(kStart + 1, (int) (130f / (SAMPLE_RATE / (float) HOP)));
            float kSum = 0f;
            for (int i = kStart; i < kEnd && i < HOP / 2; i++) kSum += mag[i];
            f.kickEnergy = Math.min(1.0f, kSum * 6.0f);

            long now = System.currentTimeMillis();
            if (f.kickEnergy > kickAvg * 1.5f && f.kickEnergy > 0.15f && (now - lastKickTime > 180)) {
                f.kick = true;
                lastKickTime = now;
            }
            kickAvg = kickAvg * 0.93f + f.kickEnergy * 0.07f;

            // Detección de Hi-Hat / Agudos (7000Hz - 15000Hz)
            int hStart = (int) (7000f / (SAMPLE_RATE / (float) HOP));
            int hEnd = Math.min(HOP / 2, (int) (15000f / (SAMPLE_RATE / (float) HOP)));
            float hSum = 0f;
            for (int i = hStart; i < hEnd; i++) hSum += mag[i];
            float hatEnergy = hSum * 8.0f;
            if (hatEnergy > hatAvg * 1.6f && hatEnergy > 0.2f && (now - lastHatTime > 100)) {
                f.hat = true;
                lastHatTime = now;
            }
            hatAvg = hatAvg * 0.95f + hatEnergy * 0.05f;

            // Voz humana: bins 4 a 11 (300Hz a 3000Hz)
            float vSum = 0f;
            for (int b = 4; b < 12; b++) vSum += f.espectro[b];
            f.vocal = Math.min(1.0f, (vSum / 8.0f) * 2.2f);

            if (listener != null) {
                listener.onFrame(f);
            }
        }
    }
}
