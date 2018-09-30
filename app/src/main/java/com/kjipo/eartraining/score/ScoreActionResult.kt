package com.kjipo.eartraining.score

import com.kjipo.scoregenerator.SequenceGenerator

sealed class ScoreActionResult {

    sealed class GenerateScoreResult : ScoreActionResult() {
        data class Success(val sequenceGenerator: SequenceGenerator) : GenerateScoreResult()
        data class Failure(val error: Throwable) : GenerateScoreResult()
//        object InFlight : GenerateScoreResult()
    }

    sealed class PlayAction : ScoreActionResult() {
        object Success : PlayAction()
        data class Failure(val error: Throwable) : PlayAction()
        object InFlight : PlayAction()
    }

}