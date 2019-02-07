package com.gluco.Data.Local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskDomainModel(
        @PrimaryKey(autoGenerate = false)
        var id: Int,
        var text: String,
        val updated: Long,
        val version: Int
)