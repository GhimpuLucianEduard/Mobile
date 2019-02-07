package com.gluco.Data.Remote.ApiService

import com.gluco.Data.Local.TaskDomainModel
import io.reactivex.Observable
import retrofit2.Response

interface NoteService {
    fun fetchNotes(last: Long) : Observable<List<TaskDomainModel>>
}