package com.kjipo.eartraining.results

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "sequence")
data class GeneratedSequence(
        @PrimaryKey
        @ColumnInfo(name = "id") val id: Int,
        @ColumnInfo(name = "sequence")
        val sequence: String)