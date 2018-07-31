package com.kjipo.eartraining;


import android.annotation.SuppressLint;
import android.content.res.AssetManager;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kjipo.eartraining.score.ScoreExample;
import com.kjipo.svg.RenderingSequence;
import com.kjipo.svg.SvgToolsKt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.CountDownLatch;

public class CustomWebViewClient extends WebChromeClient { // extends WebViewClient {
    private WebView webView;
    private AssetManager assetManager;


    @SuppressLint("SetJavaScriptEnabled")
    public void attachWebView(WebView webView, AssetManager assetManager) {
        this.webView = webView;
        this.assetManager = assetManager;

        webView.getSettings().setJavaScriptEnabled(true);

//        webView.getSettings().setDomStorageEnabled(true);
//        webView.getSettings().setLoadWithOverviewMode(true);
//        webView.getSettings().setAllowFileAccessFromFileURLs(true);
//        webView.getSettings().setOffscreenPreRaster(true);


        // This is to fit the entire page on the screen
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

//        webView.loadData(SvgToolsKt.createHtmlDocumentString(ScoreExample.INSTANCE.getTestScore()), "text/html", "UTF-8");

//        webView.setOnTouchListener((v, event) -> {
//
//            // TODO Just here for testing
//            Log.e("note", "Event: " + event);
//
//            return false;
//
//        });


        NoteVisualizerInterface visualizerInterface = new NoteVisualizerInterface();
        webView.addJavascriptInterface(visualizerInterface, "Backend");

//        webView.setWebViewClient(this);
        webView.setWebChromeClient(this);



    }

    public void loadNoteSequence(RenderingSequence renderingSequence) {
//        String htmlString = SvgToolsKt.createHtmlDocumentString(renderingSequence);

        try (InputStream data = assetManager.open("index.html");
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(data))) {
            StringBuilder inputData = new StringBuilder();
            String input;
            while ((input = bufferedReader.readLine()) != null) {
                inputData.append(input);
            }

            webView.loadDataWithBaseURL("file:///android_asset/web/", inputData.toString(), "text/html", "UTF-8", null);

//            webView.loadUrl("about:blank");
//            webView.loadData(inputData.toString(), "text/html", "UTF-8");

//            webView.loadData(htmlString, "text/html", "UTF-8");

        } catch (IOException e) {
            Log.e("Webscore", e.getMessage(), e);
        }



//        StringBuilder inputData = new StringBuilder();
//        for(String asset : new String[] {"webscore/kotlin.js", "webscore/kotlinx-html-js.js", "webscore/score-js.js", "webscore/webscore.js"}) {
////        for(String asset : new String[] {"webscore/webscore.js"}) {
//            try(InputStream data = assetManager.open(asset);
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(data))) {
//
//                String input;
//                while((input = bufferedReader.readLine()) != null) {
//                    inputData.append(input);
//                }
//                inputData.append("\n");
//
//
////                latch.wait();
//            } catch (IOException e) {
//                Log.e("Webscore", e.getMessage(), e);
//            }
//
//        }


//        Log.i("Webscore", "Loading: " +inputData);
//
////                CountDownLatch latch = new CountDownLatch(1);
//        webView.evaluateJavascript(inputData.toString(), value -> {
//            Log.i("Webscore", "Got value: " +value);
////                    latch.countDown();
//        });


    }



    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        Log.i("webview", consoleMessage.message());

        return true;
    }


    @Override
    public boolean onJsTimeout() {
        Log.i("webview", "Javascript timeout");

        return false;
    }



}
