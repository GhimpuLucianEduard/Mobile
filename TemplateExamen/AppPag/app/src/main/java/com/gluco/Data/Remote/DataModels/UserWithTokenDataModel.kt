package com.gluco.Data.Remote.DataModels

import com.gluco.Utility.empty

data class UserWithTokenDataModel(
    val token: String = String.empty(),
    val user: UserDataModel?
)