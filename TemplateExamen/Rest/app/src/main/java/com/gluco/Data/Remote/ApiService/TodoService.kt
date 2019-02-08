package com.gluco.Data.Remote.ApiService

import com.gluco.Data.Local.TodoEntity
import com.gluco.Data.Remote.DataModels.PagedResponseDataModel
import io.reactivex.Observable

interface TodoService {
    fun fetchTodos(last: Long, page: Int) : Observable<PagedResponseDataModel>
}