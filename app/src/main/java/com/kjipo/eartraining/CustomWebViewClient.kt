package com.kjipo.eartraining


import android.annotation.SuppressLint
import android.content.res.AssetManager
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.kjipo.eartraining.score.ScoreHandlerWrapper
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


class CustomWebViewClient : WebViewClient() { //, View.OnTouchListener {
    private lateinit var webView: WebView
    private var inputData = ""
    private var assetManager: AssetManager? = null
    var scoreHandler: ScoreHandlerWrapper? = null
    private var webscoresToLoad = mutableMapOf(Pair("scoreHandler", Pair("score", true)))


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

//        webView.setOnTouchListener(this)
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

        with(webView) {
            addJavascriptInterface(scoreHandler, "scoreHandler")
            loadDataWithBaseURL("file:///android_asset/web/", inputData, "text/html", "UTF-8", null)
        }
    }

    override fun onPageFinished(view: WebView, url: String) {
        super.onPageFinished(view, url)

        val javaScriptToEvaluate = webscoresToLoad.map {
            """var test_${it.value.first} = new webscore.WebScore(${it.key}, "${it.value}", ${it.value.second});"""
        }.joinToString("")

        Log.i("Webscore", "JavaScript to evaluate: $javaScriptToEvaluate")

        view.evaluateJavascript(javaScriptToEvaluate) {
            Log.i("Webscore", it)
        }
    }


    fun updateWebscore() {
        webView.evaluateJavascript("""
               test_score.reload();
           """) {
            Log.i("Webscore", it)
        }

        webView.invalidate()
    }

    fun loadSecondScore(scoreHandler: ScoreHandlerWrapper, name: String) {
        webView.addJavascriptInterface(scoreHandler, "$name")
        webView.loadDataWithBaseURL("file:///android_asset/web/", inputData, "text/html", "UTF-8", null)

        webscoresToLoad[name] = Pair(name, false)
    }


    fun getIdOfActiveElement(callback: (String?) -> Unit) {
        webView.evaluateJavascript("""
            (function() { return test_score.activeElement; })();
        """.trimIndent()) {

            Log.i("Webscore", "Callback called: $it")

            callback(it.substring(1, it.lastIndex))
        }
    }

    fun setActiveElement(activeElement: String?) {
        webView.evaluateJavascript("""
            test_score.activeElement = ${activeElement?.let { """ "$it" """ } ?: null};
        """.trimIndent()) {
            // Do nothing
        }
    }

//    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//        Log.i("WebClient", "Got event$event")
//
//        return true
//
//
//    }

}
