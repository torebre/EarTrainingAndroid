package com.kjipo.eartrainingandroid;


import android.util.Log;
import android.view.InputEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class CustomWebViewClient extends WebViewClient {
    private WebView webView;
    private NoteVisualizerInterface noteVisualizerInterface;



    protected void attachWebView(WebView webView) {
        this.webView = webView;


        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/noteView.html");

        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // TODO Just here for testing
                Log.e("note", "Event: " +event);

                return false;

            }
        });

        NoteVisualizerInterface visualizerInterface = new NoteVisualizerInterface();
        webView.addJavascriptInterface(visualizerInterface, "Backend");
        webView.setWebViewClient(this);
    }


    @Override
    public void onUnhandledInputEvent(WebView view, InputEvent event) {
        super.onUnhandledInputEvent(view, event);

        // TODO Just here for testing
        Log.i("note", "Event: " +event);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }

    public void loadNoteSequence(String noteSequence) {
        webView.loadUrl("javascript:noteView.drawNotes(" + noteSequence + ")");
    }

}
