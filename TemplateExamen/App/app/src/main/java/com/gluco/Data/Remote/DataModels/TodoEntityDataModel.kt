package com.gluco.Data.Remote.DataModels

data class TodoEntityDataModel(
        val id: Int,
        val userId: Int,
        val title: String,
        val completed: Boolean
)