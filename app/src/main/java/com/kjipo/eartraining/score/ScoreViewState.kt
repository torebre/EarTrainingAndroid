package com.kjipo.eartraining.score

import com.kjipo.scoregenerator.SequenceGenerator

data class ScoreViewState(val isPlaying: Boolean,
                          val sequenceGenerator: SequenceGenerator?,
                          val scoreCounter: Int?) {

    companion object {
        fun idle(): ScoreViewState {
            return ScoreViewState(false, null, null)
        }
    }

}