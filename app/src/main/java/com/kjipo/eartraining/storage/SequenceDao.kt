package com.kjipo.eartraining.storage

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface SequenceDao {

    @Insert
    fun insertGeneratedSequence(storedSequence: StoredSequence): Long

    @Insert
    fun insertSubmittedSequence(submittedSequence: SubmittedSequence): Long

    @Query("SELECT * FROM generated_sequence")
    fun getAllStoredSequences(): List<StoredSequence>

    @Query("SELECT * FROM submitted_sequence")
    fun getAllSubmittedSequences(): List<SubmittedSequence>

}