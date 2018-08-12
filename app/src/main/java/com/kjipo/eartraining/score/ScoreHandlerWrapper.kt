package com.kjipo.eartraining.score

import android.util.Log
import android.webkit.JavascriptInterface
import com.kjipo.handler.ScoreHandler
import com.kjipo.handler.ScoreHandlerInterface

class ScoreHandlerWrapper(var scoreHandler: ScoreHandler) : ScoreHandlerInterface {
    val listeners = mutableListOf<ScoreHandlerListener>()

    @JavascriptInterface
    override fun getScoreAsJson(): String {
        val score = scoreHandler.getScoreAsJson()
        Log.i("Webscore", "Returning score: $score")
        return score
    }

    @JavascriptInterface
    override fun moveNoteOneStep(id: String, up: Boolean) {
        scoreHandler.moveNoteOneStep(id, up)
        listeners.forEach { it.moveNoteOneStep(id, up) }
    }

    @JavascriptInterface
    override fun getIdOfFirstSelectableElement() = scoreHandler.getIdOfFirstSelectableElement()

    @JavascriptInterface
    override fun getNeighbouringElement(activeElement: String, lookLeft: Boolean) = scoreHandler.getNeighbouringElement(activeElement, lookLeft)

}


interface ScoreHandlerListener {

    fun moveNoteOneStep(id: String, up: Boolean)

}