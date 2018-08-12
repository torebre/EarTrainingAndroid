package com.kjipo.eartraining


import android.annotation.SuppressLint
import android.content.res.AssetManager
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import com.kjipo.eartraining.score.ScoreHandlerWrapper
import com.kjipo.eartraining.score.WebScoreCallback
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


class CustomWebViewClient : WebViewClient() {
    var webView: WebView? = null
    private var assetManager: AssetManager? = null
    var scoreHandler: ScoreHandlerWrapper? = null
    var webScoreCallback: WebScoreCallback? = null


    @SuppressLint("SetJavaScriptEnabled")
    fun attachWebView(webView: WebView, assetManager: AssetManager, scoreHandlerWrapper: ScoreHandlerWrapper) {
        this.webView = webView
        this.assetManager = assetManager
        this.scoreHandler = scoreHandlerWrapper

        webView.settings.javaScriptEnabled = true
        // This is to fit the entire page on the screen
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.webViewClient = this

        webScoreCallback = WebScoreCallback(webView)
    }

    fun loadNoteSequence() {
        try {
            assetManager!!.open("index.html").use { data ->
                BufferedReader(InputStreamReader(data)).use { bufferedReader ->
                    val inputData = StringBuilder()
                    var input = bufferedReader.readLine()

                    while (input != null) {
                        inputData.append(input)
                        input = bufferedReader.readLine()
                    }

                    webView?.let {
                        it.addJavascriptInterface(scoreHandler, "scoreHandler")
                        it.addJavascriptInterface(webScoreCallback, "webscoreCallback")
                        it.loadDataWithBaseURL("file:///android_asset/web/", inputData.toString(), "text/html", "UTF-8", null)
                    }
                }
            }
        } catch (e: IOException) {
            Log.e("Webscore", e.message, e)
        }
    }


    override fun onPageFinished(view: WebView, url: String) {
        super.onPageFinished(view, url)

        view.evaluateJavascript("""
                var test = new webscore.WebScore(scoreHandler);
                    """) {
            Log.i("Webscore", it)
        }
    }


    fun updateWebscore() {
        webView?.evaluateJavascript("""
               test.reload();
           """) {
            Log.i("Webscore", it)
        }

        webView?.invalidate()
    }

}
