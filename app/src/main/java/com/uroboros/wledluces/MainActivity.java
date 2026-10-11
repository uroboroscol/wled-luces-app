package com.uroboros.wledluces;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.*;
import android.util.Base64;
import android.webkit.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static final int REQ_MIC = 101;
    private WebView webView;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = new WebView(this);
        setContentView(webView);

        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setAllowFileAccess(true);

        webView.addJavascriptInterface(new AndroidBridge(this), "AndroidBridge");
        webView.setWebViewClient(new WebViewClient());

        checkPermissionsAndStart();
        webView.loadUrl("file:///android_asset/index.html");

        startUiSyncLoop();
    }

    private void checkPermissionsAndStart() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO}, REQ_MIC);
        } else {
            startLightingService();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_MIC && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLightingService();
        }
    }

    private void startLightingService() {
        Intent intent = new Intent(this, LightingService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

    private void startUiSyncLoop() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                syncLiveUi();
                handler.postDelayed(this, 50); // 20 FPS para la vista previa 2D
            }
        }, 100);
    }

    private void syncLiveUi() {
        LightingService s = LightingService.getInstance();
        if (s == null || webView == null) return;
        try {
            AudioProcessor.AudioFeatures f = s.getLatestFeatures();
            LucesEngine engine = s.getEngine();
            JSONObject d = new JSONObject();

            d.put("bpm", (double) engine.getBpm());
            d.put("beat", engine.getBeat());
            d.put("volumen", (double) f.total);
            d.put("kick", f.kick);
            d.put("hat", f.hat);
            d.put("vocal", (double) f.vocal);
            d.put("paleta", engine.getPaleta());
            d.put("efecto", engine.getEfecto());
            d.put("genero", engine.getGenero());
            d.put("conectado", s.getUdpSender().isConectado());

            JSONArray spec = new JSONArray();
            for (float v : f.espectro) spec.put((double) v);
            d.put("espectro", spec);

            String js = "if(window.onLiveStream)window.onLiveStream(" + d.toString() + ");";
            webView.evaluateJavascript(js, null);
        } catch (Exception ignored) {}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
