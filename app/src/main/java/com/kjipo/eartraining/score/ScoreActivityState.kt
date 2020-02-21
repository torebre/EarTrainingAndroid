package com.kjipo.eartraining.score

sealed class ScoreActivityState {
    object Playing: ScoreActivityState()
    object Submitted: ScoreActivityState()
}