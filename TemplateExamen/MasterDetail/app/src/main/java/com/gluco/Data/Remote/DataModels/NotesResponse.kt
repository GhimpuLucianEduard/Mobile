package com.gluco.Data.Remote.DataModels

data class NotesResponse (
    val page: Integer,
    val tasks: List<TaskDataModel>,
    val more: Boolean
)