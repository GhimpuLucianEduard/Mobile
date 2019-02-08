package com.gluco.Presentation.MainList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gluco.Data.Local.GlucoseEntry
import com.gluco.Data.Local.TodoEntity
import com.gluco.Data.Repository.TodoRepository

class MainListViewModel(private val repository: TodoRepository) : ViewModel() {

    var selectedEntry: MutableLiveData<GlucoseEntry> = MutableLiveData()

    fun add(todo: TodoEntity) {
        repository.add(todo)
    }

    val entries = repository.getEntries()
}

