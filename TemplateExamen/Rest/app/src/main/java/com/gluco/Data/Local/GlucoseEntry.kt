package com.gluco.Data.Local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gluco.Utility.empty

@Entity(tableName = "glucoseEntries")
data class GlucoseEntry(
    var apiId: String? = String.empty(),
    var afterMeal: Boolean = false,
    var note: String? = String.empty(),
    var timestamp: Long? = System.currentTimeMillis(),
    var value: Int = 0
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}