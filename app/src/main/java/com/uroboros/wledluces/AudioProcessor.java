package com.uroboros.wledluces;

import android.annotation.SuppressLint;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

public class AudioProcessor {
    private static final String TAG = "AudioProcessor";
    public static final int SAMPLE_RATE = 44100;
    public static final int HOP = 1024;

    public static class AudioFeatures {
        public float total = 0f;
        public float kickEnergy = 0f;
        public boolean kick = false;
        public float vocal = 0f;
        public float hat = 0f;
        public float[] espectro = new float[16];
    }

    private AudioRecord audioRecord;
    private Thread recordThread;
    private volatile boolean isRunning = false;
    private float sens = 1.0f;

    private final float[] real = new float[HOP];
    private final float[] imag = new float[HOP];
    private final float[] window = new float[HOP];
    private final float[] mag = new float[HOP / 2];

    private float kickAvg = 0.1f;
    private long lastKickTime = 0;

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
        this.sens = Math.max(0.1f, Math.min(10.0f, s));
    }

    @SuppressLint("MissingPermission")
    public synchronized void start() {
        if (isRunning) return;
        int minBuf = AudioRecord.getMinBufferSize(
                SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT
        );
        int bufSize = Math.max(minBuf, HOP * 4);

        try {
            audioRecord = new AudioRecord(
                    MediaRecorder.AudioSource.MIC,
                    SAMPLE_RATE,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    bufSize
            );

            if (audioRecord.getState() != AudioRecord.STATE_INITIALIZED) {
                Log.e(TAG, "AudioRecord no pudo inicializarse");
                return;
            }

            audioRecord.startRecording();
            isRunning = true;

            recordThread = new Thread(this::recordLoop, "AudioRecordThread");
            recordThread.start();
            Log.i(TAG, "Captura de micrófono iniciada");
        } catch (Exception e) {
            Log.e(TAG, "Error iniciando captura de micrófono: " + e.getMessage());
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
        while (isRunning) {
            int read = audioRecord.read(buffer, 0, HOP);
            if (read != HOP) continue;

            float sumSq = 0f;
            for (int i = 0; i < HOP; i++) {
                float sample = (buffer[i] / 32768.0f) * sens;
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
            f.total = Math.min(1.0f, rms * 3.5f);

            // 16 bandas de espectro
            int binsPerBand = (HOP / 2) / 16;
            for (int b = 0; b < 16; b++) {
                float sum = 0f;
                for (int k = 0; k < binsPerBand; k++) {
                    sum += mag[b * binsPerBand + k];
                }
                f.espectro[b] = Math.min(1.0f, (sum / binsPerBand) * 8.0f);
            }

            // Detección de bajo (Kick) ~ 35 - 120 Hz
            int kickStart = (int) (35f / (SAMPLE_RATE / (float) HOP));
            int kickEnd = Math.max(kickStart + 1, (int) (120f / (SAMPLE_RATE / (float) HOP)));
            float kickSum = 0f;
            for (int i = kickStart; i < kickEnd && i < HOP / 2; i++) kickSum += mag[i];
            f.kickEnergy = Math.min(1.0f, kickSum * 4.0f);

            long now = System.currentTimeMillis();
            if (f.kickEnergy > kickAvg * 1.6f && f.kickEnergy > 0.25f && (now - lastKickTime > 200)) {
                f.kick = true;
                lastKickTime = now;
            }
            kickAvg = kickAvg * 0.95f + f.kickEnergy * 0.05f;

            // Voz (bins 4 a 11, frecuencias medias de 300Hz a 3000Hz)
            float vocalSum = 0f;
            for (int b = 4; b < 12; b++) vocalSum += f.espectro[b];
            f.vocal = Math.min(1.0f, (vocalSum / 8.0f) * 1.5f);

            if (listener != null) {
                listener.onFrame(f);
            }
        }
    }
}
