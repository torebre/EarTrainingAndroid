package com.kjipo.eartraining.score

sealed class ScoreAction {

    object GenerateNewScore : ScoreAction()

    object PlayScore : ScoreAction()

    object TargetPlay: ScoreAction()

    object Submit : ScoreAction()

    object Skip : ScoreAction()

}