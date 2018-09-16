package com.kjipo.eartraining


import android.content.Intent
import android.support.v4.app.FragmentManager
import com.kjipo.eartraining.score.ScoreActivity

import javax.inject.Inject

class NavigationController @Inject
constructor(mainActivity: MainActivity) {
//    private val containerId: Int = R.id.
    private val mainActivity: MainActivity
    private val fragmentManager: FragmentManager


    init {
        this.mainActivity = mainActivity
        fragmentManager = mainActivity.supportFragmentManager

    }


//    fun navigateToNoteView() {
//        val noteViewFragment = NoteViewFragment()
//        fragmentManager.beginTransaction()
//                .replace(containerId, noteViewFragment)
//                .commitAllowingStateLoss()
//    }

    fun startScoreActivity() {
        val intent = Intent(mainActivity, ScoreActivity::class.java)
        mainActivity.startActivity(intent)
    }


}
