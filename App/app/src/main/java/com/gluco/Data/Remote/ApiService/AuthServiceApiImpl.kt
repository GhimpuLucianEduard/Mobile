package com.gluco.Data.Remote.ApiService

import com.gluco.Data.Remote.AuthService
import com.gluco.Data.Remote.DataModels.UserDataModel
import com.gluco.Data.Remote.DataModels.UserWithTokenDataModel
import io.reactivex.Observable

class AuthServiceApiImpl(private val apiService: GlucoseApiService) : AuthService {
    override fun login(email: String, password: String): Observable<UserWithTokenDataModel> {
        return apiService.login(UserDataModel(email = email, password = password))
    }
}