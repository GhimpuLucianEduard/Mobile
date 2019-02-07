package com.gluco.Presentation.MainList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gluco.Data.Local.GlucoseEntry
import com.gluco.Data.Local.TaskDomainModel
import com.gluco.Data.Repository.TodoRepository
import io.reactivex.Observable
import retrofit2.Response

class MainListViewModel(private val repository: TodoRepository) : ViewModel() {

    var selectedEntry: MutableLiveData<TaskDomainModel> = MutableLiveData()

    fun update(value: TaskDomainModel) {
        repository.update(value)
    }

    val entries = repository.getEntries()
}

