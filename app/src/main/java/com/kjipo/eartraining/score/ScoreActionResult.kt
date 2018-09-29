package com.kjipo.eartraining.score

sealed class ScoreActionResult {

    sealed class GenerateScoreResult : ScoreActionResult() {
        class Success : GenerateScoreResult()
        data class Failure(val error: Throwable) : GenerateScoreResult()
    }

    sealed class PlayAction : ScoreActionResult() {
        object Success : PlayAction()
        data class Failure(val error: Throwable) : PlayAction()
        object InFlight : PlayAction()
    }

}