package com.uroboros.wledluces;

public class FastFft {
    public static void transform(float[] real, float[] imag) {
        int n = real.length;
        if (Integer.bitCount(n) != 1) {
            throw new IllegalArgumentException("Length must be a power of 2");
        }
        int shift = 1 + Integer.numberOfLeadingZeros(n);
        for (int i = 0; i < n; i++) {
            int j = Integer.reverse(i) >>> shift;
            if (j > i) {
                float temp = real[i];
                real[i] = real[j];
                real[j] = temp;
                temp = imag[i];
                imag[i] = imag[j];
                imag[j] = temp;
            }
        }
        for (int len = 2; len <= n; len <<= 1) {
            int half = len >> 1;
            double angle = -2.0 * Math.PI / len;
            float wStepR = (float) Math.cos(angle);
            float wStepI = (float) Math.sin(angle);
            for (int i = 0; i < n; i += len) {
                float wR = 1.0f;
                float wI = 0.0f;
                for (int j = 0; j < half; j++) {
                    int posA = i + j;
                    int posB = posA + half;
                    float uR = real[posA];
                    float uI = imag[posA];
                    float vR = real[posB] * wR - imag[posB] * wI;
                    float vI = real[posB] * wI + imag[posB] * wR;
                    real[posA] = uR + vR;
                    imag[posA] = uI + vI;
                    real[posB] = uR - vR;
                    imag[posB] = uI - vI;
                    float nextWR = wR * wStepR - wI * wStepI;
                    wI = wR * wStepI + wI * wStepR;
                    wR = nextWR;
                }
            }
        }
    }
}
