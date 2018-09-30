package com.kjipo.eartraining.score

sealed class ScoreIntent {

    data class InitialIntent(val taskId: String?) : ScoreIntent()

    data class PlayAction(val taskId: String?) : ScoreIntent()

    class GenerateIntent : ScoreIntent()

}