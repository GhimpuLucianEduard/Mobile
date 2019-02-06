package com.gluco.Data.Local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todoEntities")
data class TodoEntity(
        @PrimaryKey(autoGenerate = false)
        var id: Int,
        var userId: Int,
        var title: String,
        var completed: Boolean
)