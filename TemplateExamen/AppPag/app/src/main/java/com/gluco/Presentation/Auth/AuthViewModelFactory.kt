package com.gluco.Presentation.Auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gluco.Data.Remote.AuthService
import com.gluco.Presentation.MainList.MainListViewModel

class AuthViewModelFactory( private val authService: AuthService
) : ViewModelProvider.NewInstanceFactory() {

    var instance: AuthViewModel? = null

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (instance == null) {
            instance = AuthViewModel(authService)
        }
        return instance as T
    }
}