package com.gluco.Data.Local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todoEntities")
data class TodoEntity(
        @PrimaryKey(autoGenerate = false)
        var id: Int,
        val text: String,
        val updated: Long,
        val status: String
)