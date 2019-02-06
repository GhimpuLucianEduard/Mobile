package com.gluco.Data.Remote.DataModels

data class NotesResponse (
    val page: Integer,
    val notes: List<NoteDataModel>,
    val more: Boolean
)