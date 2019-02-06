package com.gluco.Presentation.MainList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gluco.Data.Local.GlucoseEntry
import com.gluco.Data.Local.NoteDomainModel
import com.gluco.Data.Repository.TodoRepository
import io.reactivex.Observable

class MainListViewModel(private val repository: TodoRepository) : ViewModel() {

    var selectedEntry: MutableLiveData<GlucoseEntry> = MutableLiveData()
    public fun getEntriesByPage(page: Int) : Observable<List<NoteDomainModel>> {
        return repository.getEntriesByPage(page)
    }
    val entries = repository.getEntries()
}

