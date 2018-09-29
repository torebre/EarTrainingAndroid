package com.kjipo.eartraining.score

data class ScoreViewState(val isPlaying: Boolean) {

    companion object {
        fun idle(): ScoreViewState {
            return ScoreViewState(false)
        }
    }

}