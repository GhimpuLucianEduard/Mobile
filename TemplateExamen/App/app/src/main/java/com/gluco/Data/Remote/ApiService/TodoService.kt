package com.gluco.Data.Remote.ApiService

import com.gluco.Data.Local.TodoEntity
import io.reactivex.Observable

interface TodoService {
    fun fetchTodos() : Observable<List<TodoEntity>>
}