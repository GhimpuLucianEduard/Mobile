package com.gluco.Data.Repository

import androidx.lifecycle.LiveData
import com.gluco.Data.Local.TodoEntity

interface TodoRepository {
    fun getEntries(): LiveData<List<TodoEntity>>
    fun add(todo: TodoEntity)
}
