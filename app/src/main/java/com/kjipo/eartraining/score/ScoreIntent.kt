package com.kjipo.eartraining.score

import com.kjipo.score.Duration

sealed class ScoreIntent {

    object InitialIntent : ScoreIntent()

    object PlayAction : ScoreIntent()

    object TargetAction : ScoreIntent()

    object GenerateIntent : ScoreIntent()

    object SubmitIntent : ScoreIntent()

    sealed class ChangeActiveElementType : ScoreIntent() {
        object OpenMenu : ChangeActiveElementType()
        object CloseMenu : ChangeActiveElementType()
        data class UpdateValue(val duration: Duration?, val isNote: Boolean) : ChangeActiveElementType()
    }

    data class InsertElementIntent(val activeElement: String?) : ScoreIntent()

    data class ChangeActiveElement(val activeElement: String, val left: Boolean) : ScoreIntent()

    data class MoveNote(val activeElement: String, val up: Boolean) : ScoreIntent()

}