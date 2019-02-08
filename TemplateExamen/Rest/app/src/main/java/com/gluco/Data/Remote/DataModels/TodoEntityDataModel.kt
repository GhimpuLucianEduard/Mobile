package com.gluco.Data.Remote.DataModels

data class TodoEntityDataModel(
        val id: Int,
        val text: String,
        val updated: Long,
        val status: String
)