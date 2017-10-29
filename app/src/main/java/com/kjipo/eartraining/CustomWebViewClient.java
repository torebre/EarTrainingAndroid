package com.kjipo.eartraining;


import android.databinding.BindingAdapter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CustomWebViewClient extends WebViewClient {
    private WebView webView;


    public void attachWebView(WebView webView) {
        this.webView = webView;

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/noteView.html");

//        webView.loadUrl("file:///android_asset/test_output3.html");
//        String script = "var s2 = Snap(1000, 1000);\n" +
//                "var bigCircle = s2.circle(10, 10, 10);\n" +
//                "bigCircle.attr({\n" +
//                "    fill: \"#bada55\",\n" +
//                "    stroke: \"#000\",\n" +
//                "    strokeWidth: 5\n" +
//                "});" +
//                "console.log(\"Test20\");";

//        Log.i("Test", "Evaluating Javascript");
//        webView.evaluateJavascript(script, (ValueCallback<String>) value -> Log.i("Test", "Received value: " +value));


        webView.setOnTouchListener((v, event) -> {

            // TODO Just here for testing
            Log.e("note", "Event: " + event);

            return false;

        });

        NoteVisualizerInterface visualizerInterface = new NoteVisualizerInterface();
        webView.addJavascriptInterface(visualizerInterface, "Backend");

        webView.setWebViewClient(this);
    }


//    @Override
//    public boolean shouldOverrideUrlLoading(WebView view, String url) {
//        view.loadUrl(url);
//        return true;
//    }

    public void loadNoteSequence(String noteSequence) {
        webView.loadUrl("javascript:noteView.drawNotes(" + noteSequence + ")");
    }

}
