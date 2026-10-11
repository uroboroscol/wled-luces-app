package com.uroboros.wledluces;

import java.util.*;

public class LucesEngine {
    public static class Paleta {
        public float[] c0, c1, c2;
        public Paleta(int r0, int g0, int b0, int r1, int g1, int b1, int r2, int g2, int b2) {
            c0 = new float[]{r0 / 255f, g0 / 255f, b0 / 255f};
            c1 = new float[]{r1 / 255f, g1 / 255f, b1 / 255f};
            c2 = new float[]{r2 / 255f, g2 / 255f, b2 / 255f};
        }
    }

    public static final Map<String, Paleta> PALETAS = new LinkedHashMap<>();
    static {
        PALETAS.put("rojo_morado", new Paleta(255, 0, 0, 128, 0, 128, 255, 255, 255));
        PALETAS.put("morado_rojo", new Paleta(128, 0, 128, 255, 0, 0, 255, 255, 255));
        PALETAS.put("neon_vampire", new Paleta(255, 0, 50, 75, 0, 130, 255, 0, 0));
        PALETAS.put("dark_sith", new Paleta(180, 0, 0, 50, 0, 80, 255, 0, 0));
        PALETAS.put("techno", new Paleta(255, 0, 0, 255, 30, 0, 255, 255, 255));
        PALETAS.put("rojo_puro", new Paleta(255, 0, 0, 180, 0, 0, 255, 255, 255));
        PALETAS.put("sangre", new Paleta(180, 0, 0, 80, 0, 0, 255, 0, 40));
        PALETAS.put("sith_red", new Paleta(255, 0, 0, 100, 0, 0, 0, 0, 0));
        PALETAS.put("alerta_roja", new Paleta(255, 0, 0, 255, 60, 0, 255, 230, 0));
        PALETAS.put("red_neon", new Paleta(255, 0, 80, 255, 0, 0, 255, 120, 160));
        PALETAS.put("crimson_gold", new Paleta(220, 20, 60, 255, 215, 0, 255, 255, 255));
        PALETAS.put("infierno", new Paleta(255, 0, 0, 255, 80, 0, 255, 190, 0));
        PALETAS.put("fuego", new Paleta(255, 0, 0, 255, 100, 0, 255, 230, 0));
        PALETAS.put("rock_fire", new Paleta(255, 0, 0, 255, 75, 0, 255, 255, 255));
        PALETAS.put("firestorm", new Paleta(255, 20, 0, 255, 100, 0, 255, 250, 0));
        PALETAS.put("magma", new Paleta(255, 0, 0, 255, 120, 0, 60, 0, 0));
        PALETAS.put("rojo_violeta", new Paleta(255, 0, 60, 160, 0, 120, 255, 200, 255));
        PALETAS.put("berlin", new Paleta(255, 255, 255, 90, 90, 110, 255, 255, 255));
        PALETAS.put("uv", new Paleta(110, 0, 255, 0, 30, 255, 255, 0, 200));
        PALETAS.put("acid", new Paleta(110, 255, 0, 255, 210, 0, 255, 255, 255));
        PALETAS.put("cyber", new Paleta(0, 229, 255, 255, 0, 229, 255, 255, 255));
        PALETAS.put("hielo", new Paleta(0, 120, 255, 0, 255, 220, 255, 255, 255));
        PALETAS.put("caribe", new Paleta(255, 0, 128, 0, 229, 255, 255, 215, 0));
        PALETAS.put("street_purple", new Paleta(138, 43, 226, 75, 0, 130, 255, 255, 255));
        PALETAS.put("gold_drip", new Paleta(255, 215, 0, 184, 134, 11, 255, 255, 255));
        PALETAS.put("sunset", new Paleta(255, 94, 98, 255, 153, 102, 255, 235, 170));
        PALETAS.put("matrix", new Paleta(0, 255, 0, 0, 100, 0, 255, 255, 255));
        PALETAS.put("galaxia", new Paleta(120, 0, 255, 0, 200, 255, 255, 255, 255));
        PALETAS.put("supernova", new Paleta(255, 255, 255, 255, 180, 0, 255, 0, 80));
    }

    private int numLeds = 150;
    private String efecto = "voz_centro";
    private String paletaNombre = "techno";
    private String curRndPaleta = "techno";
    private long lastRndTime = 0;
    private float rndT = 8.0f;
    private float brillo = 0.9f;
    private int offset = 0;
    private boolean invertir = false;
    private boolean auto = false;

    private float ang = 0.0f;
    private float rotorAng = 0.0f;
    private int beatCount = 0;
    private float beatPhase = 0.0f;

    public LucesEngine(int leds) {
        setNumLeds(leds);
    }

    public synchronized void setNumLeds(int n) {
        this.numLeds = Math.max(10, n);
    }

    public synchronized void setEfecto(String fx) { this.efecto = fx; }
    public synchronized String getEfecto() { return efecto; }

    public synchronized void setPaleta(String pal) { this.paletaNombre = pal; }
    public synchronized String getPaleta() { return paletaNombre; }

    public synchronized void setRndT(float t) { this.rndT = Math.max(1.0f, t); }
    public synchronized float getRndT() { return rndT; }

    public synchronized void setBrillo(float b) { this.brillo = Math.max(0f, Math.min(1f, b)); }
    public synchronized float getBrillo() { return brillo; }

    public synchronized void setOffset(int off) { this.offset = off; }
    public synchronized void setInvertir(boolean inv) { this.invertir = inv; }
    public synchronized void setAuto(boolean a) { this.auto = a; }
    public synchronized boolean isAuto() { return auto; }

    public synchronized byte[] render(AudioProcessor.AudioFeatures f, float dt) {
        int n = numLeds;
        byte[] rgb = new byte[n * 3];

        long now = System.currentTimeMillis();
        if ("aleatoria".equals(paletaNombre)) {
            if (now - lastRndTime > (long)(rndT * 1000f)) {
                lastRndTime = now;
                List<String> keys = new ArrayList<>(PALETAS.keySet());
                curRndPaleta = keys.get(new Random().nextInt(keys.size()));
            }
        } else {
            curRndPaleta = paletaNombre;
        }

        Paleta pal = PALETAS.getOrDefault(curRndPaleta, PALETAS.get("techno"));
        if (pal == null) pal = PALETAS.get("techno");

        ang += dt * 1.5f;
        rotorAng = (rotorAng + dt * (0.8f + f.total * 2.0f)) % 1.0f;

        float[] r = new float[n];
        float[] g = new float[n];
        float[] b = new float[n];

        float vozVal = Math.min(f.vocal * 1.6f, 1.0f);

        switch (efecto) {
            case "voz_centro": { // Espectro Global: 1 sola onda en el medio de todo el espacio
                float grosor = 0.1f + vozVal * 0.8f;
                float invGrosorSq = 1.0f / (grosor * grosor);
                float colR = pal.c0[0] * vozVal + pal.c1[0] * (1f - vozVal);
                float colG = pal.c0[1] * vozVal + pal.c1[1] * (1f - vozVal);
                float colB = pal.c0[2] * vozVal + pal.c1[2] * (1f - vozVal);
                float amp = 0.2f + 0.8f * vozVal;

                for (int i = 0; i < n; i++) {
                    float pos = (float) i / n;
                    float dist = Math.abs(pos - 0.5f) * 2.0f;
                    float onda = (float) Math.exp(-(dist * dist * invGrosorSq));
                    float val = onda * amp;
                    r[i] = colR * val;
                    g[i] = colG * val;
                    b[i] = colB * val;
                }
                break;
            }
            case "voz": { // Espectro Vocal: onda en el centro de cada pared (4 cuadrantes)
                float grosor = 0.1f + vozVal * 0.8f;
                float invGrosorSq = 1.0f / (grosor * grosor);
                float colR = pal.c0[0] * vozVal + pal.c1[0] * (1f - vozVal);
                float colG = pal.c0[1] * vozVal + pal.c1[1] * (1f - vozVal);
                float colB = pal.c0[2] * vozVal + pal.c1[2] * (1f - vozVal);
                float amp = 0.2f + 0.8f * vozVal;

                for (int i = 0; i < n; i++) {
                    float pos = (float) i / n;
                    int pared = (int) (pos * 4.0f);
                    float u = pos * 4.0f - pared;
                    float dist = Math.abs(u - 0.5f) * 2.0f;
                    float onda = (float) Math.exp(-(dist * dist * invGrosorSq));
                    float val = onda * amp;
                    r[i] = colR * val;
                    g[i] = colG * val;
                    b[i] = colB * val;
                }
                break;
            }
            case "voz_laser": { // Contorno Vocal: borde brillante del anillo
                float grosor = 0.1f + vozVal * 0.8f;
                float colR = pal.c0[0] * vozVal + pal.c1[0] * (1f - vozVal);
                float colG = pal.c0[1] * vozVal + pal.c1[1] * (1f - vozVal);
                float colB = pal.c0[2] * vozVal + pal.c1[2] * (1f - vozVal);
                float amp = 0.2f + 0.8f * vozVal;

                for (int i = 0; i < n; i++) {
                    float pos = (float) i / n;
                    int pared = (int) (pos * 4.0f);
                    float u = pos * 4.0f - pared;
                    float dist = Math.abs(u - 0.5f) * 2.0f;
                    float relleno = (float) Math.exp(-(dist * dist) / (grosor * grosor)) * 0.25f;
                    float bordeDist = (dist - grosor) / 0.05f;
                    float borde = (float) Math.exp(-(bordeDist * bordeDist));

                    float valR = relleno * colR + borde * (colR * 0.6f + pal.c2[0] * 0.4f);
                    float valG = relleno * colG + borde * (colG * 0.6f + pal.c2[1] * 0.4f);
                    float valB = relleno * colB + borde * (colB * 0.6f + pal.c2[2] * 0.4f);
                    r[i] = valR * amp;
                    g[i] = valG * amp;
                    b[i] = valB * amp;
                }
                break;
            }
            case "solido": { // Color Sólido: sin parpadeos, luz estática
                for (int i = 0; i < n; i++) {
                    r[i] = pal.c0[0];
                    g[i] = pal.c0[1];
                    b[i] = pal.c0[2];
                }
                break;
            }
            case "pulso": { // Pulso con el bajo
                float amp = 0.1f + f.kickEnergy * 0.9f;
                for (int i = 0; i < n; i++) {
                    r[i] = pal.c0[0] * amp;
                    g[i] = pal.c0[1] * amp;
                    b[i] = pal.c0[2] * amp;
                }
                break;
            }
            case "rotor": { // Cometas girando
                for (int i = 0; i < n; i++) {
                    float pos = (float) i / n;
                    float dist = Math.abs((pos - rotorAng + 0.5f) % 1.0f - 0.5f) * 4.0f;
                    float val = Math.max(0f, 1f - dist) + (f.kick ? 0.4f : 0f);
                    r[i] = pal.c0[0] * val;
                    g[i] = pal.c1[1] * val;
                    b[i] = pal.c2[2] * val;
                }
                break;
            }
            case "espectro": { // Espectro 16 bandas repartidas
                for (int i = 0; i < n; i++) {
                    float pos = (float) i / n;
                    int pared = (int) (pos * 4.0f);
                    float u = pos * 4.0f - pared;
                    int bin = Math.min(15, Math.max(0, (int) (u * 16.0f)));
                    float nivel = (float) Math.pow(f.espectro[bin], 1.3);
                    float colR = pal.c0[0] * (1f - u) + pal.c1[0] * u;
                    float colG = pal.c0[1] * (1f - u) + pal.c1[1] * u;
                    float colB = pal.c0[2] * (1f - u) + pal.c1[2] * u;
                    r[i] = colR * nivel;
                    g[i] = colG * nivel;
                    b[i] = colB * nivel;
                }
                break;
            }
            case "explosion": { // Drop explosión
                float flash = f.kick ? 1.0f : 0.4f;
                for (int i = 0; i < n; i++) {
                    r[i] = flash;
                    g[i] = flash * 0.5f;
                    b[i] = flash * 0.2f;
                }
                break;
            }
            default: { // Ondas suaves
                for (int i = 0; i < n; i++) {
                    float pos = (float) i / n;
                    float s = (float) Math.sin(pos * 6.283f * 2.0f + ang) * 0.5f + 0.5f;
                    float colR = pal.c0[0] * s + pal.c1[0] * (1f - s);
                    float colG = pal.c0[1] * s + pal.c1[1] * (1f - s);
                    float colB = pal.c0[2] * s + pal.c1[2] * (1f - s);
                    float amp = 0.2f + 0.8f * f.total;
                    r[i] = colR * amp;
                    g[i] = colG * amp;
                    b[i] = colB * amp;
                }
                break;
            }
        }

        // Aplicar brillo general, offset e invertir
        int sign = invertir ? -1 : 1;
        for (int i = 0; i < n; i++) {
            int targetIdx = (offset + sign * i) % n;
            if (targetIdx < 0) targetIdx += n;

            int ir = Math.min(255, Math.max(0, (int) (r[i] * brillo * 255f)));
            int ig = Math.min(255, Math.max(0, (int) (g[i] * brillo * 255f)));
            int ib = Math.min(255, Math.max(0, (int) (b[i] * brillo * 255f)));

            rgb[targetIdx * 3] = (byte) ir;
            rgb[targetIdx * 3 + 1] = (byte) ig;
            rgb[targetIdx * 3 + 2] = (byte) ib;
        }

        return rgb;
    }
}
