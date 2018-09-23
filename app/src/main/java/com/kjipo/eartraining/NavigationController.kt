package com.kjipo.eartraining


import android.content.Intent
import com.kjipo.eartraining.score.ScoreActivity

class NavigationController constructor(mainActivity: MainActivity) {
    private val mainActivity: MainActivity


    init {
        this.mainActivity = mainActivity
    }

    fun startScoreActivity() {
        val intent = Intent(mainActivity, ScoreActivity::class.java)
        mainActivity.startActivity(intent)
    }


}
