package com.gluco.Data.Remote.DataModels

import com.gluco.Utility.empty
import com.google.gson.annotations.SerializedName

data class UserDataModel(
    @SerializedName("_id")
    val id: String? = String.empty(),
    val email: String = String.empty(),
    val password: String = String.empty()
)