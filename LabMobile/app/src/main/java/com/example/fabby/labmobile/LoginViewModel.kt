package com.example.fabby.labmobile

import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.fabby.labmobile.DomainModels.User
import com.example.fabby.labmobile.Utils.Constants

class LoginViewModel(var userModel: User = User()) : ViewModel() {

    init {
        Log.i(Constants.debugLogTag, "Login view model init()")
    }
}