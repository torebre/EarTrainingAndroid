package com.kjipo.eartraining.score

import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.kjipo.handler.ScoreHandler


class WebScoreCallback(val webView: WebView) {
    var scoreHandler: ScoreHandler = ScoreHandler {}

    @JavascriptInterface
    fun moveNoteOneStep(noteId: String, up: Boolean) {
        scoreHandler.moveNoteOneStep(noteId, up)
    }

    @JavascriptInterface
    fun getCurrentScore(): String {
        return scoreHandler.getScoreAsJson()
    }

    @JavascriptInterface
    fun reload() {
        webView.post {
            webView.reload()
        }

        Log.i("Webscore", "Reloading")

    }

    external fun loadCurrentScore()



}