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

            if (obj.has("efecto")) s.getEngine().setEfecto(obj.getString("efecto"));
            if (obj.has("paleta")) s.getEngine().setPaleta(obj.getString("paleta"));
            if (obj.has("brillo")) s.getEngine().setBrillo((float) obj.getDouble("brillo"));
            if (obj.has("sens")) s.getAudioProcessor().setSens((float) obj.getDouble("sens"));
            if (obj.has("rnd_t")) s.getEngine().setRndT((float) obj.getDouble("rnd_t"));
            if (obj.has("offset")) s.getEngine().setOffset(obj.getInt("offset"));
            if (obj.has("invertir")) s.getEngine().setInvertir(obj.getBoolean("invertir"));
            if (obj.has("auto")) s.getEngine().setAuto(obj.getBoolean("auto"));
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
    public String getState() {
        LightingService s = LightingService.getInstance();
        if (s == null) return "{}";
        try {
            JSONObject obj = new JSONObject();
            obj.put("efecto", s.getEngine().getEfecto());
            obj.put("paleta", s.getEngine().getPaleta());
            obj.put("brillo", s.getEngine().getBrillo());
            obj.put("rnd_t", s.getEngine().getRndT());
            obj.put("ip", s.getUdpSender().getIp());
            obj.put("leds", s.getUdpSender().getNumLeds());
            obj.put("auto", s.getEngine().isAuto());
            return obj.toString();
        } catch (Exception e) {
            return "{}";
        }
    }
}
