package com.kjipo.eartraining.score

import android.webkit.JavascriptInterface
import com.kjipo.handler.ScoreHandler


class WebScoreCallback(val scoreHandler: ScoreHandler) {


    @JavascriptInterface
    fun moveNoteOneStep(noteId: String, up: Boolean) {
        scoreHandler.moveNoteOneStep(noteId, up)
    }

    @JavascriptInterface
    fun getCurrentScore(): String {
        return scoreHandler.getScoreAsJson()
    }

    external fun loadCurrentScore()

}