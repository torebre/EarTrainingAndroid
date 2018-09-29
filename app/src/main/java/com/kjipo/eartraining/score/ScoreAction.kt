package com.kjipo.eartraining.score

sealed class ScoreAction {

    data class GenerateNewScore(val taskId: String?) : ScoreAction()

    data class PlayScore(val taskId: String?) : ScoreAction()

    class Skip : ScoreAction()

}