package com.example.fabby.labmobile.Presentation

import android.app.Activity
import android.os.Bundle
import com.example.fabby.labmobile.LoginViewModel
import com.example.fabby.labmobile.R

class LoginActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var vm = LoginViewModel()
        setContentView(R.layout.activity_login)
    }

}