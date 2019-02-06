package com.gluco.Presentation.MainList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gluco.Data.Local.GlucoseEntry
import com.gluco.Data.Repository.TodoRepository

class MainListViewModel(private val repository: TodoRepository) : ViewModel() {

    var selectedEntry: MutableLiveData<GlucoseEntry> = MutableLiveData()

    val entries = repository.getEntries()
}

