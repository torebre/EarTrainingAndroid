package com.kjipo.eartraining.score

import com.kjipo.scoregenerator.SequenceGenerator

data class ScoreViewState(val isPlaying: Boolean,
                          val sequenceGenerator: SequenceGenerator?,
                          val scoreCounter: Int?,
                          val submitted: Boolean,
                          val addTargetScore: Boolean = false) {

    companion object {
        fun idle(): ScoreViewState {
            return ScoreViewState(false, null, null, false)
        }
    }

}