package com.gluco.Data.Remote.DataModels

data class TaskDataModel(
        val id: Int,
        val text: String,
        val updated: Long,
        val version: Int,
        val status: String
)