package com.gluco.Data.Remote.ApiService

import com.gluco.Data.Remote.AuthService
import com.gluco.Data.Remote.DataModels.UserDataModel
import com.gluco.Data.Remote.DataModels.UserWithTokenDataModel
import io.reactivex.Observable

class AuthServiceApiImpl(private val apiService: ApiService) : AuthService {
    override fun register(email: String, password: String): Observable<Any> {
        return apiService.register(UserDataModel(email = email, password = password))
    }

    override fun login(email: String, password: String): Observable<UserWithTokenDataModel> {
        return apiService.login(UserDataModel(email = email, password = password))
    }
}