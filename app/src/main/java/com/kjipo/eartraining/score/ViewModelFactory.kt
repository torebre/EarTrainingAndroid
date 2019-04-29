package com.kjipo.eartraining.score

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.kjipo.eartraining.SchedulerProvider
import com.kjipo.eartraining.eartrainer.EarTrainer
import com.kjipo.eartraining.storage.EarTrainingDatabase

class ViewModelFactory(private val earTrainer: EarTrainer, private val database: EarTrainingDatabase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScoreViewModel::class.java)) {
            return ScoreViewModel(ScoreActionProcessorHolder(earTrainer, database, SchedulerProvider)) as T
        }
        throw IllegalArgumentException("Unknown model class: $modelClass")
    }


}