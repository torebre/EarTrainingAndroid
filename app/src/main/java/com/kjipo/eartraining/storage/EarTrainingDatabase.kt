package com.kjipo.eartraining.storage

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [StoredSequence::class, SubmittedSequence::class], version = 3)
abstract class EarTrainingDatabase : RoomDatabase() {

    abstract fun generatedSequenceDao(): SequenceDao

}