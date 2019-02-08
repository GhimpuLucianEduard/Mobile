package com.gluco.Data.Remote.ApiService

import com.gluco.Data.Local.TaskDomainModel
import com.gluco.Data.Remote.DataModels.TaskDataModel
import io.reactivex.Observable
import retrofit2.Response

interface NoteService {
    fun fetchNotes(last: Long) : Observable<List<TaskDomainModel>>
    fun update(id: Int, entry: TaskDomainModel) : Observable<Response<TaskDataModel>>
}