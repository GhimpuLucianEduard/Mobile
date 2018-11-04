package com.example.fabby.labmobile.DomainModels

import com.example.fabby.labmobile.Utils.empty

data class User(
        val email: String = String.empty(),
        val password: String = String.empty(),
        override var id: String = String.empty()) : BaseModel() {
}