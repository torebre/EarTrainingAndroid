package com.kjipo.eartraining.score

sealed class ScoreIntent {

    object InitialIntent : ScoreIntent()

    object PlayAction : ScoreIntent()

    object TargetAction: ScoreIntent()

    object GenerateIntent : ScoreIntent()

    object SubmitIntent : ScoreIntent()

}