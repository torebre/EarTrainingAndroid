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
    //    private var renderingSequence: RenderingSequence? = null
    var scoreHandler: ScoreHandlerWrapper? = null
    var webScoreCallback: WebScoreCallback? = null


    @SuppressLint("SetJavaScriptEnabled")
    fun attachWebView(webView: WebView, assetManager: AssetManager, scoreHandlerWrapper: ScoreHandlerWrapper) {
        this.webView = webView
        this.assetManager = assetManager

        webView.settings.javaScriptEnabled = true

        // This is to fit the entire page on the screen
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true

//        webView.clearCache(true)
//        val webSettings = webView.getSettings()
//        webSettings.setDomStorageEnabled(true)
//        webSettings.setAppCacheEnabled(false)

//        webView.clearCache(true)

        webView.webViewClient = this


        webScoreCallback = WebScoreCallback(webView)

        this.scoreHandler = scoreHandlerWrapper


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

                    Log.i("Webscore", "Rendering")

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

        Log.i("Webscore", "Score handler: ${scoreHandler}. Rendering2: $url")
//        view.let {
//            it.removeJavascriptInterface("scoreHandler")
//            it.addJavascriptInterface(scoreHandler, "scoreHandler")
//            it.removeJavascriptInterface("webscoreCallback")
//            it.addJavascriptInterface(webScoreCallback, "webscoreCallback")
//            it.evaluateJavascript("""var test = new webscore.WebScore(scoreHandler);
//                webscoreCallback.reload();
//                    """, {
//                Log.i("Webscore", it)
//            })
//        }

        view.let {
//            it.addJavascriptInterface(scoreHandler, "scoreHandler")
//            it.addJavascriptInterface(webScoreCallback, "webscoreCallback")

            it.evaluateJavascript("""
                var test = new webscore.WebScore(scoreHandler);

                console.log("Test23");
                    """, {
                Log.i("Webscore", it)
            })


        }



    }


    fun updateWebscore() {
        webView?.evaluateJavascript("""
               test.reload();
           """.trimIndent(), {
            Log.i("Webscore", it)
        })

        webView?.invalidate()


    }

}
