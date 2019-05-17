package com.kjipo.eartraining.score

import com.kjipo.score.Duration


sealed class ScoreAction {

    object GenerateNewScore : ScoreAction()

    object PlayScore : ScoreAction()

    object TargetPlay : ScoreAction()

    object Submit : ScoreAction()

    object Skip : ScoreAction()

    sealed class ChangeActiveElementType : ScoreAction() {
        object ShowMenu : ChangeActiveElementType()
        object HideMenu : ChangeActiveElementType()
        class UpdateValue(val selectedElement: Duration?, val isNote: Boolean) : ChangeActiveElementType()
    }

    data class InsertElement(val activeElement: String) : ScoreAction()

    data class ActiveElementSelect(val activeElement: String, val left: Boolean): ScoreAction()

}