package com.kjipo.eartraining.score

sealed class ScoreAction {

    object GenerateNewScore : ScoreAction()

    data class PlayScore(val taskId: String?) : ScoreAction()

    object Submit : ScoreAction()

    object Skip : ScoreAction()

}