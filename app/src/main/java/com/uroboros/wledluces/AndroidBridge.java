package com.uroboros.wledluces;

import android.webkit.JavascriptInterface;
import org.json.JSONObject;

public class AndroidBridge {
    private final MainActivity activity;

    public AndroidBridge(MainActivity act) {
        this.activity = act;
    }

    @JavascriptInterface
    public void set(String jsonStr) {
        try {
            JSONObject obj = new JSONObject(jsonStr);
            LightingService s = LightingService.getInstance();
            if (s == null) return;
            LucesEngine engine = s.getEngine();

            if (obj.has("efecto")) engine.setEfecto(obj.getString("efecto"));
            if (obj.has("paleta")) engine.setPaleta(obj.getString("paleta"));
            if (obj.has("genero")) engine.setGenero(obj.getString("genero"));
            if (obj.has("brillo")) engine.setBrillo((float) obj.getDouble("brillo"));
            if (obj.has("sens")) s.getAudioProcessor().setSens((float) obj.getDouble("sens"));
            if (obj.has("rnd_t")) engine.setRndT((float) obj.getDouble("rnd_t"));
            if (obj.has("offset")) engine.setOffset(obj.getInt("offset"));
            if (obj.has("invertir")) engine.setInvertir(obj.getBoolean("invertir"));
            if (obj.has("auto")) engine.setAuto(obj.getBoolean("auto"));

            if (obj.has("frase")) engine.setFrase(obj.getInt("frase"));
            if (obj.has("div")) engine.setDiv(obj.getInt("div"));
            if (obj.has("drop_compases")) engine.setDropCompases(obj.getInt("drop_compases"));
            if (obj.has("drop_max")) engine.setDropMax(obj.getInt("drop_max"));
            if (obj.has("apagon")) engine.setApagon((float) obj.getDouble("apagon"));
            if (obj.has("adelanto")) engine.setAdelanto(obj.getInt("adelanto"));
            if (obj.has("estrobo_auto")) engine.setEstroboAuto(obj.getBoolean("estrobo_auto"));
            if (obj.has("chispas")) engine.setChispas(obj.getBoolean("chispas"));
            if (obj.has("paleta_drop")) engine.setPaletaDrop(obj.getBoolean("paleta_drop"));
            if (obj.has("rotar_paleta")) engine.setRotarPaleta(obj.getBoolean("rotar_paleta"));

            if (obj.has("hold")) {
                if (obj.isNull("hold")) engine.setHold(null);
                else engine.setHold(obj.getString("hold"));
            }
            if (obj.has("evento")) {
                String ev = obj.getString("evento");
                if ("drop".equals(ev)) engine.triggerDrop();
            }
        } catch (Exception ignored) {}
    }

    @JavascriptInterface
    public void connect(String ip, int leds) {
        LightingService s = LightingService.getInstance();
        if (s != null) {
            s.getUdpSender().reconectar(ip, leds);
            s.getEngine().setNumLeds(leds);
        }
    }

    @JavascriptInterface
    public void disconnect() {
        LightingService s = LightingService.getInstance();
        if (s != null) {
            s.getUdpSender().desconectar();
        }
    }

    @JavascriptInterface
    public String getState() {
        LightingService s = LightingService.getInstance();
        if (s == null) return "{}";
        try {
            JSONObject obj = new JSONObject();
            LucesEngine engine = s.getEngine();
            obj.put("efecto", engine.getEfecto());
            obj.put("paleta", engine.getPaleta());
            obj.put("genero", engine.getGenero());
            obj.put("brillo", engine.getBrillo());
            obj.put("sens", s.getAudioProcessor().getSens());
            obj.put("rnd_t", engine.getRndT());
            obj.put("ip", s.getUdpSender().getIp());
            obj.put("leds", s.getUdpSender().getNumLeds());
            obj.put("auto", engine.isAuto());
            obj.put("conectado", s.getUdpSender().isConectado());
            return obj.toString();
        } catch (Exception e) {
            return "{}";
        }
    }
}
