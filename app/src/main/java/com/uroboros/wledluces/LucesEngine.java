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

    public static class GeneroConfig {
        public String[] groove;
        public String[] breaks;
        public String[] paletas;
        public GeneroConfig(String[] g, String[] b, String[] p) {
            this.groove = g;
            this.breaks = b;
            this.paletas = p;
        }
    }

    public static final Map<String, GeneroConfig> GENEROS = new HashMap<>();
    static {
        GENEROS.put("techno", new GeneroConfig(
                new String[]{"matrix", "rotor", "paredes", "ondas", "cortes", "pulso", "scanner", "espectro", "chispas", "relampago", "laser", "espejo", "barrido"},
                new String[]{"voz_centro", "voz", "voz_laser", "solido", "aurora", "respira", "olas", "ambiente", "corazon"},
                new String[]{"techno", "rojo_puro", "sangre", "sith_red", "alerta_roja", "red_neon", "infierno", "fuego", "rock_fire", "berlin", "uv", "acid", "cyber", "hielo", "matrix", "galaxia", "supernova", "rojo_morado", "morado_rojo", "neon_vampire", "dark_sith"}
        ));
        GENEROS.put("reggaeton", new GeneroConfig(
                new String[]{"perreo", "dembow", "paredes", "ondas", "pulso", "chispas", "relampago", "espejo", "sirena"},
                new String[]{"voz_centro", "voz", "voz_laser", "solido", "respira", "aurora", "olas", "ambiente", "corazon"},
                new String[]{"rojo_puro", "alerta_roja", "red_neon", "crimson_gold", "infierno", "fuego", "caribe", "cyber", "acid", "sunset", "gold_drip", "rojo_morado", "morado_rojo", "neon_vampire", "dark_sith"}
        ));
        GENEROS.put("rap", new GeneroConfig(
                new String[]{"triplet", "sub808", "pulso", "laser", "espectro", "scanner", "cortes", "sirena", "glitch"},
                new String[]{"voz_centro", "voz", "voz_laser", "solido", "ambiente", "aurora", "respira", "corazon"},
                new String[]{"rojo_puro", "sangre", "sith_red", "alerta_roja", "crimson_gold", "street_purple", "gold_drip", "uv", "berlin", "cyber", "galaxia", "red_neon", "rojo_morado", "morado_rojo", "neon_vampire", "dark_sith"}
        ));
        GENEROS.put("rock", new GeneroConfig(
                new String[]{"moshpit", "rock_riff", "relampago", "cortes", "paredes", "pulso", "chispas", "glitch"},
                new String[]{"voz_centro", "voz", "voz_laser", "solido", "aurora", "olas", "respira", "corazon"},
                new String[]{"rock_fire", "rojo_puro", "sangre", "sith_red", "alerta_roja", "magma", "infierno", "firestorm", "fuego", "berlin", "supernova", "rojo_morado", "morado_rojo", "neon_vampire", "dark_sith"}
        ));
        GENEROS.put("pop", new GeneroConfig(
                new String[]{"rainbow_chase", "pop_disco", "rotor", "scanner", "espejo", "pulso", "ondas", "estrellas", "barrido"},
                new String[]{"voz_centro", "voz", "voz_laser", "solido", "respira", "aurora", "ambiente", "corazon"},
                new String[]{"red_neon", "rojo_violeta", "crimson_gold", "alerta_roja", "cyber", "caribe", "sunset", "supernova", "rojo_morado", "morado_rojo", "neon_vampire", "dark_sith"}
        ));
        GENEROS.put("tropical", new GeneroConfig(
                new String[]{"salsa_clave", "dembow", "paredes", "ondas", "chispas", "barrido"},
                new String[]{"voz_centro", "voz", "voz_laser", "solido", "olas", "respira", "ambiente", "corazon"},
                new String[]{"rojo_puro", "crimson_gold", "fuego", "infierno", "alerta_roja", "caribe", "sunset", "rojo_morado", "morado_rojo", "neon_vampire", "dark_sith"}
        ));
        GENEROS.put("relax", new GeneroConfig(
                new String[]{"ocean_waves", "zen", "aurora", "respira", "olas", "ambiente", "estrellas"},
                new String[]{"voz_centro", "voz", "voz_laser", "solido", "ocean_waves", "zen", "aurora", "respira", "olas", "ambiente", "corazon", "estrellas"},
                new String[]{"sangre", "rojo_violeta", "sunset", "deep_sea", "forest", "ocean", "lavender", "hielo", "galaxia", "rojo_morado", "morado_rojo", "neon_vampire", "dark_sith"}
        ));
    }

    private final Random rand = new Random();

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

    // Director IA VJ
    private String seccion = "silencio";
    private String efectoAuto = "rotor";
    private String efectoBreak = "voz_centro";
    private String efectoActivoActual = "voz_centro";
    private int beatsSinKick = 0;
    private float tension = 0f;
    private int fraseBeats = 0;
    private int fraseContador = 0;
    private int forzarBeatsRestantes = 0;
    private String forzarEfecto = null;
    private long ultimoKickTime = 0;

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
    private float scannerPos = 0.0f;
    private float scannerDir = 1.0f;
    private float bpm = 128.0f;
    private int beat = 0;
    private float fase = 0.0f;
    private long lastBeatTime = 0;

    public LucesEngine(int leds) {
        setNumLeds(leds);
    }

    public synchronized void setNumLeds(int n) { this.numLeds = Math.max(10, n); }
    public synchronized int getNumLeds() { return numLeds; }

    public synchronized void setEfecto(String fx) {
        this.efecto = fx;
        if (!auto) this.efectoActivoActual = fx;
    }
    public synchronized String getEfecto() { return efecto; }
    public synchronized String getEfectoActivo() { return efectoActivoActual; }

    public synchronized void setPaleta(String pal) {
        this.paletaNombre = pal;
        this.curRndPaleta = pal;
    }
    public synchronized String getPaleta() { return paletaNombre; }
    public synchronized String getCurPaleta() { return curRndPaleta; }

    public synchronized void setGenero(String g) {
        this.genero = g;
        GeneroConfig cfg = GENEROS.getOrDefault(g, GENEROS.get("techno"));
        if (cfg != null && cfg.groove.length > 0) {
            this.efectoAuto = cfg.groove[0];
            this.efectoBreak = cfg.breaks[0];
        }
    }
    public synchronized String getGenero() { return genero; }
    public synchronized String getSeccion() { return seccion; }

    public synchronized void setRndT(float t) { this.rndT = Math.max(1.0f, t); }
    public synchronized float getRndT() { return rndT; }

    public synchronized void setBrillo(float b) { this.brillo = Math.max(0f, Math.min(1f, b)); }
    public synchronized float getBrillo() { return brillo; }

    public synchronized void setOffset(int off) { this.offset = off; }
    public synchronized void setInvertir(boolean inv) { this.invertir = inv; }

    public synchronized void setAuto(boolean a) {
        this.auto = a;
        if (!a) {
            this.efectoActivoActual = this.efecto;
        }
    }
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
        this.forzarEfecto = estroboAuto ? "explosion" : "pulso";
        this.forzarBeatsRestantes = 4 * Math.max(1, dropCompases);
        this.seccion = "drop";

        GeneroConfig cfg = GENEROS.getOrDefault(genero, GENEROS.get("techno"));
        if (paletaDrop && cfg != null && cfg.paletas.length > 0) {
            this.curRndPaleta = cfg.paletas[rand.nextInt(cfg.paletas.length)];
        }
    }

    public synchronized float getBpm() { return bpm; }
    public synchronized int getBeat() { return beat; }

    // =====================================================================
    // DIRECTOR DE RITMO E INTELIGENCIA VJ (PASO POR FRAME)
    // =====================================================================
    private void pasoDirector(AudioProcessor.AudioFeatures f, float dt, long now) {
        boolean nuevoBeat = false;

        if (f.kick) {
            long beatDt = now - lastBeatTime;
            if (beatDt > 200 && beatDt < 1200) {
                float instantBpm = 60000f / beatDt;
                bpm = bpm * 0.85f + instantBpm * 0.15f;
            }
            lastBeatTime = now;
            ultimoKickTime = now;
            beat = (beat + 1) % 4;
            fase = 0f;
            nuevoBeat = true;
            fraseBeats++;
            if (forzarBeatsRestantes > 0) forzarBeatsRestantes--;
        } else {
            fase = (fase + dt * (bpm / 60.0f)) % 1.0f;
        }

        GeneroConfig cfg = GENEROS.getOrDefault(genero, GENEROS.get("techno"));
        if (cfg == null) cfg = GENEROS.get("techno");

        // Lógica de Secciones Musicales
        boolean kickReciente = (now - ultimoKickTime) < 2200;
        if (f.total < 0.04f) {
            seccion = "silencio";
        } else if (kickReciente) {
            // Si veníamos de break y pasaron >= 8 beats sin kick -> ¡AUTO DROP!
            if ((seccion.equals("break") || seccion.equals("build")) && beatsSinKick >= 8) {
                if (auto) {
                    triggerDrop();
                }
            }
            if (!seccion.equals("drop") || forzarBeatsRestantes <= 0) {
                seccion = "groove";
            }
            beatsSinKick = 0;
            tension *= 0.92f;
        } else {
            if (nuevoBeat) beatsSinKick++;
            tension = Math.min(1.0f, Math.max(0.0f, tension + (f.vocal * 0.12f) + (f.hat ? 0.08f : 0f) - 0.02f));
            String anteriorSec = seccion;
            seccion = tension > 0.35f ? "build" : "break";

            // Al entrar a break, elegir un efecto de calma nuevo
            if (!anteriorSec.equals("break") && !anteriorSec.equals("build") && cfg.breaks.length > 0) {
                String nuevoBreak = cfg.breaks[rand.nextInt(cfg.breaks.length)];
                if (nuevoBreak.equals(efectoBreak) && cfg.breaks.length > 1) {
                    nuevoBreak = cfg.breaks[(rand.nextInt(cfg.breaks.length) + 1) % cfg.breaks.length];
                }
                efectoBreak = nuevoBreak;
            }
        }

        // Elección inteligente del efecto activo
        if (forzarBeatsRestantes > 0 && forzarEfecto != null) {
            efectoActivoActual = forzarEfecto;
        } else if (auto) {
            if (seccion.equals("break") || seccion.equals("silencio")) {
                efectoActivoActual = efectoBreak;
            } else if (seccion.equals("build")) {
                efectoActivoActual = estroboAuto ? "explosion" : efectoBreak;
            } else { // groove
                // Cada (4 * frase) beats, cambiar automáticamente de efecto de ritmo
                int umbralFrase = 4 * Math.max(2, frase);
                if (nuevoBeat && fraseBeats >= umbralFrase) {
                    fraseBeats = 0;
                    fraseContador++;
                    if (cfg.groove.length > 0) {
                        String nuevoGroove = cfg.groove[rand.nextInt(cfg.groove.length)];
                        if (nuevoGroove.equals(efectoAuto) && cfg.groove.length > 1) {
                            nuevoGroove = cfg.groove[(rand.nextInt(cfg.groove.length) + 1) % cfg.groove.length];
                        }
                        efectoAuto = nuevoGroove;
                    }
                    // Rotar paleta cada 2 frases si está activado
                    if (rotarPaleta && fraseContador % 2 == 0 && cfg.paletas.length > 0) {
                        curRndPaleta = cfg.paletas[rand.nextInt(cfg.paletas.length)];
                    }
                }
                efectoActivoActual = efectoAuto;
            }
        } else {
            // Modo Manual: efecto elegido por el usuario
            efectoActivoActual = efecto;
        }
    }

    // =====================================================================
    // RENDER PRINCIPAL DE CUADROS LED (40 FPS)
    // =====================================================================
    public synchronized byte[] render(AudioProcessor.AudioFeatures f, float dt) {
        int n = numLeds;
        byte[] rgb = new byte[n * 3];
        long now = System.currentTimeMillis();

        pasoDirector(f, dt, now);

        // Rotación de paleta aleatoria por tiempo
        if ("aleatoria".equals(paletaNombre)) {
            if (now - lastRndTime > (long)(rndT * 1000f)) {
                lastRndTime = now;
                List<String> keys = new ArrayList<>(PALETAS.keySet());
                curRndPaleta = keys.get(rand.nextInt(keys.size()));
            }
        } else if (!auto || !rotarPaleta) {
            curRndPaleta = paletaNombre;
        }

        Paleta pal = PALETAS.getOrDefault(curRndPaleta, PALETAS.get("techno"));
        if (pal == null) pal = PALETAS.get("techno");

        ang += dt * 1.5f;
        rotorAng = (rotorAng + dt * (0.8f + f.total * 2.0f)) % 1.0f;
        scannerPos += scannerDir * dt * (1.2f + f.total * 2.5f);
        if (scannerPos > 1.0f) { scannerPos = 1.0f; scannerDir = -1.0f; }
        else if (scannerPos < 0.0f) { scannerPos = 0.0f; scannerDir = 1.0f; }

        float[] r = new float[n];
        float[] g = new float[n];
        float[] b = new float[n];

        // 1. Efectos sostenidos de pads (Hold)
        if (holdEffect != null) {
            switch (holdEffect) {
                case "blackout":
                    return rgb;
                case "strobe":
                case "flash":
                    boolean on = (now % 60) < 30;
                    float flashVal = on ? 1.0f : 0.0f;
                    for (int i = 0; i < n; i++) { r[i] = flashVal; g[i] = flashVal; b[i] = flashVal; }
                    return finalizeRgb(r, g, b, rgb, n);
                case "blinder":
                    for (int i = 0; i < n; i++) { r[i] = 1.0f; g[i] = 0.9f; b[i] = 0.7f; }
                    return finalizeRgb(r, g, b, rgb, n);
            }
        }

        float vozVal = Math.min(f.vocal * 1.6f, 1.0f);
        String fx = efectoActivoActual != null ? efectoActivoActual : "voz_centro";

        // 2. Renderizado de Efecto Seleccionado (Auto o Manual)
        switch (fx) {
            case "voz_centro": { // Espectro Global: 1 sola onda gigante
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
            case "voz": { // Espectro Vocal: 4 cuadrantes / paredes
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
            case "voz_laser": { // Contorno Vocal: anillo con borde
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
            case "rotor": { // Cometas girando
                for (int i = 0; i < n; i++) {
                    float pos = (float) i / n;
                    float dist = Math.abs((pos - rotorAng + 0.5f) % 1.0f - 0.5f) * 4.0f;
                    float val = Math.max(0f, 1f - dist) + (f.kick ? 0.4f : 0f);
                    r[i] = pal.c0[0] * val; g[i] = pal.c1[1] * val; b[i] = pal.c2[2] * val;
                }
                break;
            }
            case "pulso": { // Pulso al kick
                float amp = 0.1f + f.kickEnergy * 0.9f;
                for (int i = 0; i < n; i++) {
                    r[i] = pal.c0[0] * amp; g[i] = pal.c0[1] * amp; b[i] = pal.c0[2] * amp;
                }
                break;
            }
            case "paredes": { // 1 pared por beat
                int activa = beat % 4;
                for (int i = 0; i < n; i++) {
                    int pared = (int) (((float) i / n) * 4.0f);
                    float val = (pared == activa) ? (1.0f - fase * 0.7f) : 0.1f;
                    r[i] = pal.c0[0] * val; g[i] = pal.c1[1] * val; b[i] = pal.c2[2] * val;
                }
                break;
            }
            case "ondas": { // Ondas que explotan con el kick
                for (int i = 0; i < n; i++) {
                    float pos = (float) i / n;
                    float dist = Math.abs(pos - 0.5f) * 2.0f;
                    float w = (float) Math.sin(dist * 12.0f - ang * 3.0f) * 0.5f + 0.5f;
                    float val = w * (0.3f + f.kickEnergy * 0.7f);
                    r[i] = pal.c0[0] * val; g[i] = pal.c1[1] * val; b[i] = pal.c2[2] * val;
                }
                break;
            }
            case "cortes": { // Segmentos que saltan
                int segs = 8;
                for (int i = 0; i < n; i++) {
                    int s = (int) (((float) i / n) * segs);
                    boolean on = ((s + beat) % 2 == 0);
                    float val = on ? (0.6f + f.kickEnergy * 0.4f) : 0.05f;
                    r[i] = pal.c0[0] * val; g[i] = pal.c1[1] * val; b[i] = pal.c2[2] * val;
                }
                break;
            }
            case "scanner": { // Haces cruzados
                for (int i = 0; i < n; i++) {
                    float pos = (float) i / n;
                    float dist1 = Math.abs(pos - scannerPos);
                    float dist2 = Math.abs(pos - (1.0f - scannerPos));
                    float val = (float) (Math.exp(-dist1 * 20.0f) + Math.exp(-dist2 * 20.0f));
                    r[i] = pal.c0[0] * val; g[i] = pal.c1[1] * val; b[i] = pal.c2[2] * val;
                }
                break;
            }
            case "espectro": { // 16 bandas distribuidas
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
            case "chispas": { // Destellos hi-hat
                for (int i = 0; i < n; i++) {
                    boolean chispa = (f.hat && rand.nextFloat() > 0.6f);
                    float val = chispa ? 1.0f : (rand.nextFloat() * 0.15f);
                    r[i] = pal.c2[0] * val; g[i] = pal.c2[1] * val; b[i] = pal.c2[2] * val;
                }
                break;
            }
            case "explosion": { // Drop explosión estroboscópica
                boolean flash = (now % 70) < 35 || f.kick;
                float flashVal = flash ? 1.0f : 0.3f;
                for (int i = 0; i < n; i++) {
                    r[i] = flashVal;
                    g[i] = flashVal * 0.5f;
                    b[i] = flashVal * 0.2f;
                }
                break;
            }
            case "matrix": { // Código Matrix hacker
                for (int i = 0; i < n; i++) {
                    float pos = (float) i / n;
                    float val = (float) ((Math.sin(pos * 30.0f - ang * 5.0f) * 0.5f + 0.5f) * (rand.nextFloat() > 0.3f ? 1.0f : 0.1f));
                    r[i] = pal.c0[0] * val * 0.3f;
                    g[i] = pal.c0[1] * val;
                    b[i] = pal.c0[2] * val * 0.3f;
                }
                break;
            }
            case "aurora":
            case "respira":
            default: { // Ondas melódicas de aurora / relajación
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
