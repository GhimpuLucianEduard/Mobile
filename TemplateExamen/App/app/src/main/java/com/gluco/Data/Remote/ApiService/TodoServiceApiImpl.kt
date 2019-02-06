package com.gluco.Data.Remote.ApiService

import com.gluco.Data.Converter.TodoEntryConverter
import com.gluco.Data.Local.TodoEntity
import io.reactivex.Observable

class TodoServiceApiImpl(private val apiService: ApiService) : TodoService {
    override fun fetchTodos(): Observable<List<TodoEntity>> {
        return apiService.getAllEntries()
                .flatMapIterable {list->list}
                .map { item -> TodoEntryConverter.convert(item) }
                .toList()
                .toObservable()
    }
}