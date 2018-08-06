package com.kjipo.eartraining


import android.annotation.SuppressLint
import android.content.res.AssetManager
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import com.kjipo.score.RenderingSequence
import kotlinx.serialization.json.JSON
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


class CustomWebViewClient : WebViewClient() {
    private var webView: WebView? = null
    private var assetManager: AssetManager? = null
    private var renderingSequence: RenderingSequence? = null


    @SuppressLint("SetJavaScriptEnabled")
    fun attachWebView(webView: WebView, assetManager: AssetManager) {
        this.webView = webView
        this.assetManager = assetManager

        webView.settings.javaScriptEnabled = true

        // This is to fit the entire page on the screen
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true

        val visualizerInterface = NoteVisualizerInterface()
        webView.addJavascriptInterface(visualizerInterface, "Backend")

        webView.webViewClient = this
    }

    fun loadNoteSequence(renderingSequence: RenderingSequence) {
        this.renderingSequence = renderingSequence
        try {
            assetManager!!.open("index.html").use { data ->
                BufferedReader(InputStreamReader(data)).use { bufferedReader ->
                    val inputData = StringBuilder()
                    var input = bufferedReader.readLine()

                    while (input != null) {
                        inputData.append(input)
                        input = bufferedReader.readLine()
                    }

                    webView!!.loadDataWithBaseURL("file:///android_asset/web/", inputData.toString(), "text/html", "UTF-8", null)

                    webView!!.loadUrl("javascript:" + "console.log(\"Test50\");\n")

                }
            }
        } catch (e: IOException) {
            Log.e("Webscore", e.message, e)
        }

    }


    override fun onPageFinished(view: WebView, url: String) {
        renderingSequence?.let {
            val javaScript = """webscore.loadJson_61zpoe$("${JSON.stringify(it).replace("\"", "\\\"")}");"""
            Log.i("Webscore", javaScript)
            webView!!.evaluateJavascript(javaScript, { value -> Log.i("Webscore", value) })
        }

    }
}
