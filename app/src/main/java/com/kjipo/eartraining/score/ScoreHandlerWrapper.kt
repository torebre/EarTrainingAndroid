package com.kjipo.eartraining.score

import android.util.Log
import android.webkit.JavascriptInterface
import com.kjipo.handler.ScoreHandlerInterface
import com.kjipo.score.Accidental
import com.kjipo.score.Duration

class ScoreHandlerWrapper(var scoreHandler: ScoreHandlerInterface) : ScoreHandlerInterface {

    val listeners = mutableListOf<ScoreHandlerListener>()

    @JavascriptInterface
    override fun updateDuration(id: String, keyPressed: Int) =
            scoreHandler.updateDuration(id, keyPressed).also {
                listeners.forEach { it.pitchSequenceChanged() }
            }


    @JavascriptInterface
    override fun insertNote(activeElement: String, keyPressed: Int) =
            scoreHandler.insertNote(activeElement, keyPressed)?.also { idInsertedNote ->
                listeners.forEach { it.pitchSequenceChanged() }
            }

    @JavascriptInterface
    override fun getScoreAsJson() = scoreHandler.getScoreAsJson().also {
        Log.i("Webscore", "Returning score: $it")
    }


    @JavascriptInterface
    override fun moveNoteOneStep(id: String, up: Boolean) =
            scoreHandler.moveNoteOneStep(id, up).also {
                listeners.forEach { it.pitchSequenceChanged() }
            }


    @JavascriptInterface
    override fun getIdOfFirstSelectableElement() = scoreHandler.getIdOfFirstSelectableElement()

    @JavascriptInterface
    override fun getNeighbouringElement(activeElement: String, lookLeft: Boolean) = scoreHandler.getNeighbouringElement(activeElement, lookLeft)

    @JavascriptInterface
    override fun switchBetweenNoteAndRest(idOfElementToReplace: String, keyPressed: Int) = scoreHandler.switchBetweenNoteAndRest(idOfElementToReplace, keyPressed)

    @JavascriptInterface
    override fun deleteElement(id: String) = scoreHandler.deleteElement(id)

    @JavascriptInterface
    override fun insertNote(activeElement: String, duration: Duration, pitch: Int): String? = scoreHandler.insertNote(activeElement, duration, pitch)

    @JavascriptInterface
    override fun insertRest(activeElement: String, duration: Duration): String? = scoreHandler.insertRest(activeElement, duration)

    @JavascriptInterface
    override fun toggleExtra(id: String, extra: Accidental) = scoreHandler.toggleExtra(id, extra)

}


interface ScoreHandlerListener {

    fun pitchSequenceChanged()

}