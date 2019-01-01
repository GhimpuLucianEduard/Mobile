package com.gluco.Data.Local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "glucoseEntries")
data class GlucoseEntry(
    var apiId: String? = "",
    val afterMeal: Boolean,
    val note: String?,
    val timestamp: Long?,
    val value: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}