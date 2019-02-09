package com.kjipo.eartraining.score

import com.kjipo.scoregenerator.SequenceGenerator
import com.kjipo.scoregenerator.SimpleNoteSequence

sealed class ScoreActionResult {

    sealed class GenerateScoreResult : ScoreActionResult() {
        data class Success(val sequenceGenerator: SequenceGenerator, val targetSequence: SimpleNoteSequence) : GenerateScoreResult()
        data class Failure(val error: Throwable) : GenerateScoreResult()
    }

    sealed class PlayAction : ScoreActionResult() {
        object Success : PlayAction()
        data class Failure(val error: Throwable) : PlayAction()
        object InFlight : PlayAction()
    }

    sealed class TargetPlayAction: ScoreActionResult() {
        object Success: TargetPlayAction()
        data class Failure(val error: Throwable): TargetPlayAction()
        object InFlight: TargetPlayAction()
    }

    sealed class SubmitAction : ScoreActionResult() {
        data class Success(val sequenceGenerator: SequenceGenerator) : SubmitAction()
        data class Failure(val error: Throwable) : SubmitAction()
    }

}