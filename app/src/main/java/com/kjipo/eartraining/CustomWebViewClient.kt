package com.kjipo.eartraining


import android.annotation.SuppressLint
import android.content.res.AssetManager
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import com.kjipo.eartraining.score.ScoreHandlerWrapper
import com.kjipo.scoregenerator.SequenceGenerator
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


class CustomWebViewClient : WebViewClient() {
    private lateinit var webView: WebView
    private var inputData = ""
    private var assetManager: AssetManager? = null
    lateinit var scoreHandler: ScoreHandlerWrapper
    lateinit var targetScoreHandler: ScoreHandlerWrapper

    private val scoreHandlerName = "scoreHandler"
    private val targetSequenceName = "targetSequence"

    private val inputWebscoreSetup = Pair("score", true)
    private val targetWebscoreSetup = Pair("targetScore", true)


    @SuppressLint("SetJavaScriptEnabled")
    fun attachWebView(webView: WebView, assetManager: AssetManager, scoreHandlerWrapper: ScoreHandlerWrapper, targetScoreHandlerWrapper: ScoreHandlerWrapper) {
        this.webView = webView
        this.assetManager = assetManager
        this.scoreHandler = scoreHandlerWrapper
        this.targetScoreHandler = targetScoreHandlerWrapper


        with(webView) {
            settings.javaScriptEnabled = true
            // This is to fit the entire page on the screen
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
        }
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

        with(webView) {
            addJavascriptInterface(scoreHandler, scoreHandlerName)
            addJavascriptInterface(targetScoreHandler, targetSequenceName)
            loadDataWithBaseURL("file:///android_asset/web/", inputData, "text/html", "UTF-8", null)
        }
    }

    override fun onPageFinished(view: WebView, url: String) {
        super.onPageFinished(view, url)

        val javaScriptToEvaluate = """
            var ${scoreHandlerName}_score = new webscore.WebScore(${scoreHandlerName}, "${inputWebscoreSetup.first}", ${inputWebscoreSetup.second});
            var ${targetSequenceName}_score = new webscore.WebScore(${targetSequenceName}, "${targetWebscoreSetup.first}", ${targetWebscoreSetup.second});
            ${targetSequenceName}_score.setVisible(false);
        """.trimIndent()

        Log.i("Webscore", javaScriptToEvaluate)

        view.evaluateJavascript(javaScriptToEvaluate) {
            Log.i("Webscore", it)
        }
    }


    fun updateWebscore() {
        webView.evaluateJavascript("""
               ${scoreHandlerName}_score .reload();
               ${targetSequenceName}_score.setVisible(false);
           """) {
            Log.i("Webscore", it)
        }

        webView.invalidate()
    }

    fun loadSecondScore(scoreHandler: SequenceGenerator) {
        targetScoreHandler?.let {
            it.scoreHandler = scoreHandler
            webView.evaluateJavascript("""
               ${targetSequenceName}_score.reload();
               ${targetSequenceName}_score.setVisible(true);
           """) {
                Log.i("Webscore", it)
            }
        }
    }

    fun getIdOfActiveElement(callback: (String?) -> Unit) {
        webView.evaluateJavascript("""
            (function() { return ${scoreHandlerName}_score.activeElement; })();
        """.trimIndent()) {

            Log.i("Webscore", "Callback called: $it")

            callback(it.substring(1, it.lastIndex))
        }
    }

    fun setActiveElement(activeElement: String?) {
        webView.evaluateJavascript("""
            ${scoreHandlerName}_score.activeElement = ${activeElement?.let { """ "$it" """ } ?: null};
        """.trimIndent()) {
            // Do nothing
        }
    }

}
