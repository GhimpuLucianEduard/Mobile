package com.gluco.Presentation.MainList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gluco.Data.Local.GlucoseEntry
import com.gluco.Data.Repository.GlucoseRepository
import io.reactivex.Observable

class MainListViewModel(private val repository: GlucoseRepository) : ViewModel() {

    var selectedEntry: MutableLiveData<GlucoseEntry> = MutableLiveData()

    val entries = repository.getEntries()

    fun deleteEntry(entry: GlucoseEntry) : Observable<Any> {
        return repository.deleteEntry(entry)
    }

    fun addEntry(entry: GlucoseEntry) : Observable<GlucoseEntry> {
        return repository.addEntry(entry)
    }

    fun updateEntry(entry: GlucoseEntry) : Observable<GlucoseEntry> {
        return repository.updateEntry(entry)
    }
}

