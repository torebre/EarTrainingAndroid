package com.kjipo.eartraining.storage

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey


@Entity(tableName = "submitted_sequence")
@ForeignKey(entity = StoredSequence::class, parentColumns = ["id"], childColumns = ["referencedSequence"])
data class SubmittedSequence(@PrimaryKey var id: Long?,
                             @ColumnInfo(name = "referencedSequence") var referencedSequence: Long)