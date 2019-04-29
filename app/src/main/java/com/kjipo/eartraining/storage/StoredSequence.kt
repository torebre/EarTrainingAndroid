package com.kjipo.eartraining.storage

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity(tableName = "generated_sequence")
data class StoredSequence(@PrimaryKey
                          var id: Long?,
                          @ColumnInfo(name = "created") var created: Long = System.currentTimeMillis()) {
    constructor() : this(null)

}