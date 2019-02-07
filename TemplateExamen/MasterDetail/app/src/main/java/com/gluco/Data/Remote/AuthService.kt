package com.gluco.Data.Remote

import com.gluco.Data.Remote.DataModels.UserWithTokenDataModel
import io.reactivex.Observable

interface AuthService {
    fun login(email: String, password: String) : Observable<UserWithTokenDataModel>
    fun register(email: String, password: String) : Observable<Any>
}