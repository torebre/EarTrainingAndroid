package com.kjipo.eartraining.score

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.kjipo.eartraining.eartrainer.EarTrainer

class ViewModelFactory(private val earTrainer: EarTrainer) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScoreViewModel::class.java)) {
            return ScoreViewModel(earTrainer) as T
        }
        throw IllegalArgumentException("Unknown model class: $modelClass")
    }


}