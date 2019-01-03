package com.gluco.Presentation.Auth

import androidx.lifecycle.ViewModel;
import com.gluco.Data.Remote.AuthService
import com.gluco.Data.Remote.DataModels.UserWithTokenDataModel
import io.reactivex.Observable

class AuthViewModel(private val authService: AuthService) : ViewModel() {
    fun login(email: String, password: String) : Observable<UserWithTokenDataModel>{
        return authService.login(email, password)
    }
}
