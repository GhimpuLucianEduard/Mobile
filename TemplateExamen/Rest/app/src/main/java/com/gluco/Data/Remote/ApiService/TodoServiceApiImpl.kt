package com.gluco.Data.Remote.ApiService

import com.gluco.Data.Converter.TodoEntryConverter
import com.gluco.Data.Local.TodoEntity
import com.gluco.Data.Remote.DataModels.PagedResponseDataModel
import io.reactivex.Observable

class TodoServiceApiImpl(private val apiService: ApiService) : TodoService {
    override fun fetchTodos(last: Long, page: Int): Observable<PagedResponseDataModel> {
        return apiService.getAllEntries(last, page)
    }
}