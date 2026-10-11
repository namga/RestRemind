package com.restremind.app;

import android.app.Activity;
import android.os.Bundle;
import android.os.Build;
import android.Manifest;
import android.content.pm.PackageManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.JavascriptInterface;
import android.content.Context;
import android.os.Vibrator;
import android.os.VibrationEffect;
import android.view.View;
import android.graphics.Color;

public class MainActivity extends Activity {
    private WebView webView;
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.rgb(14,165,233));
        getWindow().setNavigationBarColor(Color.BLACK);
        if (Build.VERSION.SDK_INT >= 23 && Build.VERSION.SDK_INT <= 28) {
            // Vibrate permission is granted at install time on these Android versions.
        }
        webView = new WebView(this);
        webView.setBackgroundColor(Color.rgb(240,249,255));
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new AndroidBridge(this), "RestRemindAndroid");
        setContentView(webView);
        webView.loadUrl("file:///android_asset/index.html");
        if (Build.VERSION.SDK_INT >= 33 && checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
        }
    }
    @Override public void onBackPressed() {
        if (webView != null && webView.canGoBack()) webView.goBack(); else super.onBackPressed();
    }
    public static class AndroidBridge {
        private final Context context;
        AndroidBridge(Context c) { context = c; }
        @JavascriptInterface public boolean isAndroidApp() { return true; }
        @JavascriptInterface public void vibrate(long ms) {
            try {
                Vibrator v = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
                if (v != null && v.hasVibrator()) {
                    if (Build.VERSION.SDK_INT >= 26) v.vibrate(VibrationEffect.createOneShot(Math.max(1, Math.min(ms, 5000)), VibrationEffect.DEFAULT_AMPLITUDE));
                    else v.vibrate(ms);
                }
            } catch (Exception ignored) {}
        }
    }
}
