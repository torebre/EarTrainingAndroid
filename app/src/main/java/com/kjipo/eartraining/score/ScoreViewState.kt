package com.kjipo.eartraining.score

import com.kjipo.score.Duration
import com.kjipo.scoregenerator.SequenceGenerator

data class ScoreViewState(val isPlaying: Boolean,
                          val sequenceGenerator: SequenceGenerator?,
                          val scoreCounter: Int?,
                          val submitted: Boolean,
                          val addTargetScore: Boolean = false,
                          val chooseTargetMenu: Boolean = false,
                          val activeDuration: Duration = Duration.QUARTER,
                          val isNote: Boolean = true,
                          val activeElement: String? = null) {

    companion object {
        fun idle(): ScoreViewState {
            return ScoreViewState(false, null, null, false, false, false)
        }
    }

}