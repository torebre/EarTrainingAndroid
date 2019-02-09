package com.kjipo.eartraining


import android.annotation.SuppressLint
import android.content.res.AssetManager
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import com.kjipo.eartraining.score.ScoreHandlerWrapper
import com.kjipo.handler.ScoreHandlerInterface
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


class CustomWebViewClient : WebViewClient() {
    private lateinit var webView: WebView
    private var inputData = ""
    private var assetManager: AssetManager? = null
    var scoreHandler: ScoreHandlerWrapper? = null
    private var webscoresToLoad = mutableMapOf(Pair("scoreHandler", "score"))


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
    }

    fun loadNoteSequence() {
        try {
            inputData = assetManager!!.open("index.html").use { data ->
                BufferedReader(InputStreamReader(data)).use { bufferedReader ->
                    val inputData = StringBuilder()
                    var input = bufferedReader.readLine()

                    while (input != null) {
                        inputData.append(input)
                        input = bufferedReader.readLine()
                    }

                    return@use inputData.toString()


                }
            }
        } catch (e: IOException) {
            Log.e("Webscore", e.message, e)
        }

        webView.let {
            it.addJavascriptInterface(scoreHandler, "scoreHandler")
            it.loadDataWithBaseURL("file:///android_asset/web/", inputData, "text/html", "UTF-8", null)
        }
    }

    override fun onPageFinished(view: WebView, url: String) {
        super.onPageFinished(view, url)

        val javaScriptToEvaluate = webscoresToLoad.map {
            """var test_${it.value} = new webscore.WebScore(${it.key}, "${it.value}");"""
        }.joinToString("")

        Log.i("Webscore", "JavaScript to evaluate: $javaScriptToEvaluate")

        view.evaluateJavascript(javaScriptToEvaluate) {
            Log.i("Webscore", it)
        }
    }


    fun updateWebscore() {
        webView.evaluateJavascript("""
               test.reload();
           """) {
            Log.i("Webscore", it)
        }

        webView.invalidate()
    }


    fun loadSecondScore(scoreHandler: ScoreHandlerWrapper, name: String) {
        webView.addJavascriptInterface(scoreHandler, "$name")
        webView.loadDataWithBaseURL("file:///android_asset/web/", inputData, "text/html", "UTF-8", null)

        webscoresToLoad[name] = name
    }

}
