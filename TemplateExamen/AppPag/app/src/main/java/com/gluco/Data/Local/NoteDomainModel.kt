package com.gluco.Data.Local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todoEntities")
data class NoteDomainModel(
        @PrimaryKey(autoGenerate = false)
        var id: Int,
        var text: String,
        var date: String
)