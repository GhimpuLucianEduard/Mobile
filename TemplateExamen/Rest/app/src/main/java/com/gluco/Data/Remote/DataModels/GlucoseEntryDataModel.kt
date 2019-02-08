package com.gluco.Data.Remote.DataModels

import com.google.gson.annotations.SerializedName

data class GlucoseEntryDataModel(
        @SerializedName("_id")
        val id: String? = "",
        val afterMeal: Boolean,
        val note: String?,
        val timestamp: Long,
        val value: Int
)