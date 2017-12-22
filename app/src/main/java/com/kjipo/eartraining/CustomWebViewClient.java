package com.kjipo.eartraining;


import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CustomWebViewClient extends WebViewClient {
    private WebView webView;


    public void attachWebView(WebView webView) {
        this.webView = webView;

        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl("file:///android_asset/noteView.html");

        webView.setOnTouchListener((v, event) -> {

            // TODO Just here for testing
            Log.e("note", "Event: " + event);

            return false;

        });

        NoteVisualizerInterface visualizerInterface = new NoteVisualizerInterface();
        webView.addJavascriptInterface(visualizerInterface, "Backend");

        webView.setWebViewClient(this);
    }

    public void loadNoteSequence(String noteSequence) {
        webView.loadUrl("javascript:noteView.drawNotes(" + noteSequence + ")");
    }

}
