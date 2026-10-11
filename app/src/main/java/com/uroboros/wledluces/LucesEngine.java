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
        // Paletas rojas y moradas nuevas
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
    private String genero = "techno";
    private long lastRndTime = 0;
    private float rndT = 8.0f;
    private float brillo = 0.9f;
    private int offset = 0;
    private boolean invertir = false;
    private boolean auto = false;

    // Ajustes & Parámetros adicionales
    private int frase = 8;
    private int div = 4;
    private int dropCompases = 2;
    private int dropMax = 16;
    private float apagon = 1.0f;
    private int adelanto = 70;
    private boolean estroboAuto = true;
    private boolean chispas = true;
    private boolean paletaDrop = true;
    private boolean rotarPaleta = true;

    private String holdEffect = null;
    private boolean dropManualTrigger = false;
    private long dropStartTime = 0;

    private float ang = 0.0f;
    private float rotorAng = 0.0f;
    private float bpm = 128.0f;
    private int beat = 0;
    private float fase = 0.0f;
    private long lastBeatTime = 0;

    public LucesEngine(int leds) {
        setNumLeds(leds);
    }

    public synchronized void setNumLeds(int n) { this.numLeds = Math.max(10, n); }
    public synchronized int getNumLeds() { return numLeds; }

    public synchronized void setEfecto(String fx) { this.efecto = fx; }
    public synchronized String getEfecto() { return efecto; }

    public synchronized void setPaleta(String pal) { this.paletaNombre = pal; }
    public synchronized String getPaleta() { return paletaNombre; }

    public synchronized void setGenero(String g) { this.genero = g; }
    public synchronized String getGenero() { return genero; }

    public synchronized void setRndT(float t) { this.rndT = Math.max(1.0f, t); }
    public synchronized float getRndT() { return rndT; }

    public synchronized void setBrillo(float b) { this.brillo = Math.max(0f, Math.min(1f, b)); }
    public synchronized float getBrillo() { return brillo; }

    public synchronized void setOffset(int off) { this.offset = off; }
    public synchronized void setInvertir(boolean inv) { this.invertir = inv; }
    public synchronized void setAuto(boolean a) { this.auto = a; }
    public synchronized boolean isAuto() { return auto; }

    public synchronized void setFrase(int f) { this.frase = f; }
    public synchronized void setDiv(int d) { this.div = d; }
    public synchronized void setDropCompases(int dc) { this.dropCompases = dc; }
    public synchronized void setDropMax(int dm) { this.dropMax = dm; }
    public synchronized void setApagon(float ap) { this.apagon = ap; }
    public synchronized void setAdelanto(int ad) { this.adelanto = ad; }
    public synchronized void setEstroboAuto(boolean ea) { this.estroboAuto = ea; }
    public synchronized void setChispas(boolean c) { this.chispas = c; }
    public synchronized void setPaletaDrop(boolean pd) { this.paletaDrop = pd; }
    public synchronized void setRotarPaleta(boolean rp) { this.rotarPaleta = rp; }

    public synchronized void setHold(String h) { this.holdEffect = h; }
    public synchronized void triggerDrop() {
        this.dropManualTrigger = true;
        this.dropStartTime = System.currentTimeMillis();
    }

    public synchronized float getBpm() { return bpm; }
    public synchronized int getBeat() { return beat; }

    public synchronized byte[] render(AudioProcessor.AudioFeatures f, float dt) {
        int n = numLeds;
        byte[] rgb = new byte[n * 3];

        long now = System.currentTimeMillis();

        // Control de ritmo básico y BPM
        if (f.kick) {
            long beatDt = now - lastBeatTime;
            if (beatDt > 250 && beatDt < 1000) {
                float instantBpm = 60000f / beatDt;
                bpm = bpm * 0.85f + instantBpm * 0.15f;
            }
            lastBeatTime = now;
            beat = (beat + 1) % 4;
            fase = 0f;
        } else {
            fase = (fase + dt * (bpm / 60.0f)) % 1.0f;
        }

        // Rotación de paleta aleatoria con temporizador ajustable
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

        // Verificar si hay efecto sostenido (HOLD: STROBE, BLINDER, FLASH, BLACKOUT)
        if (holdEffect != null) {
            switch (holdEffect) {
                case "blackout":
                    // Todo negro
                    return rgb;
                case "strobe":
                case "flash":
                    boolean on = (System.currentTimeMillis() % 60) < 30;
                    float flashVal = on ? 1.0f : 0.0f;
                    for (int i = 0; i < n; i++) { r[i] = flashVal; g[i] = flashVal; b[i] = flashVal; }
                    return finalizeRgb(r, g, b, rgb, n);
                case "blinder":
                    for (int i = 0; i < n; i++) { r[i] = 1.0f; g[i] = 0.9f; b[i] = 0.7f; }
                    return finalizeRgb(r, g, b, rgb, n);
            }
        }

        // Efecto activo (o Drop si fue disparado recientemente)
        String activeFx = efecto;
        if (dropManualTrigger && (now - dropStartTime < (long)(dropCompases * 2000L))) {
            activeFx = "explosion";
        } else {
            dropManualTrigger = false;
        }

        float vozVal = Math.min(f.vocal * 1.6f, 1.0f);

        switch (activeFx) {
            case "voz_centro": { // Espectro Global: 1 sola onda central
                float grosor = 0.1f + vozVal * 0.8f;
                float invSq = 1.0f / (grosor * grosor);
                float colR = pal.c0[0] * vozVal + pal.c1[0] * (1f - vozVal);
                float colG = pal.c0[1] * vozVal + pal.c1[1] * (1f - vozVal);
                float colB = pal.c0[2] * vozVal + pal.c1[2] * (1f - vozVal);
                float amp = 0.2f + 0.8f * vozVal;

                for (int i = 0; i < n; i++) {
                    float pos = (float) i / n;
                    float dist = Math.abs(pos - 0.5f) * 2.0f;
                    float onda = (float) Math.exp(-(dist * dist * invSq)) * amp;
                    r[i] = colR * onda; g[i] = colG * onda; b[i] = colB * onda;
                }
                break;
            }
            case "voz": { // Espectro Vocal: 4 ondas
                float grosor = 0.1f + vozVal * 0.8f;
                float invSq = 1.0f / (grosor * grosor);
                float colR = pal.c0[0] * vozVal + pal.c1[0] * (1f - vozVal);
                float colG = pal.c0[1] * vozVal + pal.c1[1] * (1f - vozVal);
                float colB = pal.c0[2] * vozVal + pal.c1[2] * (1f - vozVal);
                float amp = 0.2f + 0.8f * vozVal;

                for (int i = 0; i < n; i++) {
                    float pos = (float) i / n;
                    int pared = (int) (pos * 4.0f);
                    float u = pos * 4.0f - pared;
                    float dist = Math.abs(u - 0.5f) * 2.0f;
                    float onda = (float) Math.exp(-(dist * dist * invSq)) * amp;
                    r[i] = colR * onda; g[i] = colG * onda; b[i] = colB * onda;
                }
                break;
            }
            case "voz_laser": { // Contorno Vocal
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
                    float bDist = (dist - grosor) / 0.05f;
                    float borde = (float) Math.exp(-(bDist * bDist));

                    r[i] = (relleno * colR + borde * (colR * 0.6f + pal.c2[0] * 0.4f)) * amp;
                    g[i] = (relleno * colG + borde * (colG * 0.6f + pal.c2[1] * 0.4f)) * amp;
                    b[i] = (relleno * colB + borde * (colB * 0.6f + pal.c2[2] * 0.4f)) * amp;
                }
                break;
            }
            case "solido": { // Color Sólido limpio
                for (int i = 0; i < n; i++) {
                    r[i] = pal.c0[0]; g[i] = pal.c0[1]; b[i] = pal.c0[2];
                }
                break;
            }
            case "pulso": {
                float amp = 0.1f + f.kickEnergy * 0.9f;
                for (int i = 0; i < n; i++) {
                    r[i] = pal.c0[0] * amp; g[i] = pal.c0[1] * amp; b[i] = pal.c0[2] * amp;
                }
                break;
            }
            case "rotor": {
                for (int i = 0; i < n; i++) {
                    float pos = (float) i / n;
                    float dist = Math.abs((pos - rotorAng + 0.5f) % 1.0f - 0.5f) * 4.0f;
                    float val = Math.max(0f, 1f - dist) + (f.kick ? 0.4f : 0f);
                    r[i] = pal.c0[0] * val; g[i] = pal.c1[1] * val; b[i] = pal.c2[2] * val;
                }
                break;
            }
            case "espectro": {
                for (int i = 0; i < n; i++) {
                    float pos = (float) i / n;
                    int pared = (int) (pos * 4.0f);
                    float u = pos * 4.0f - pared;
                    int bin = Math.min(15, Math.max(0, (int) (u * 16.0f)));
                    float nivel = (float) Math.pow(f.espectro[bin], 1.3);
                    float colR = pal.c0[0] * (1f - u) + pal.c1[0] * u;
                    float colG = pal.c0[1] * (1f - u) + pal.c1[1] * u;
                    float colB = pal.c0[2] * (1f - u) + pal.c1[2] * u;
                    r[i] = colR * nivel; g[i] = colG * nivel; b[i] = colB * nivel;
                }
                break;
            }
            case "explosion": {
                float flash = f.kick ? 1.0f : 0.4f;
                for (int i = 0; i < n; i++) {
                    r[i] = flash; g[i] = flash * 0.5f; b[i] = flash * 0.2f;
                }
                break;
            }
            default: { // Ondas suaves
                for (int i = 0; i < n; i++) {
                    float pos = (float) i / n;
                    float s = (float) Math.sin(pos * 6.283f * 2.0f + ang) * 0.5f + 0.5f;
                    float amp = 0.2f + 0.8f * f.total;
                    r[i] = (pal.c0[0] * s + pal.c1[0] * (1f - s)) * amp;
                    g[i] = (pal.c0[1] * s + pal.c1[1] * (1f - s)) * amp;
                    b[i] = (pal.c0[2] * s + pal.c1[2] * (1f - s)) * amp;
                }
                break;
            }
        }

        return finalizeRgb(r, g, b, rgb, n);
    }

    private byte[] finalizeRgb(float[] r, float[] g, float[] b, byte[] rgb, int n) {
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
