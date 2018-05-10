package com.kjipo.eartraining;


import android.annotation.SuppressLint;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kjipo.eartraining.score.ScoreExample;
import com.kjipo.svg.RenderingSequence;
import com.kjipo.svg.SvgToolsKt;

public class CustomWebViewClient extends WebViewClient {
    private WebView webView;


    @SuppressLint("SetJavaScriptEnabled")
    public void attachWebView(WebView webView) {
        this.webView = webView;

        webView.getSettings().setJavaScriptEnabled(true);

        // This is to fit the entire page on the screen
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

//        webView.loadUrl("file:///android_asset/noteView.html");

        webView.loadData(SvgToolsKt.createHtmlDocumentString(ScoreExample.INSTANCE.getTestScore()), "text/html", "UTF-8");

        webView.setOnTouchListener((v, event) -> {

            // TODO Just here for testing
            Log.e("note", "Event: " + event);

            return false;

        });

        NoteVisualizerInterface visualizerInterface = new NoteVisualizerInterface();
        webView.addJavascriptInterface(visualizerInterface, "Backend");

        webView.setWebViewClient(this);
    }

    public void loadNoteSequence(RenderingSequence renderingSequence) {
        String htmlString = SvgToolsKt.createHtmlDocumentString(renderingSequence);

        webView.loadUrl("about:blank");
        webView.loadData(htmlString, "text/html", "UTF-8");
    }




}
