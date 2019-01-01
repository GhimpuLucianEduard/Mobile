package com.gluco.Presentation.MainList

import androidx.lifecycle.ViewModel;
import com.gluco.Data.Repository.GlucoseRepository
import com.gluco.Utility.lazyDeferred

class MainListViewModel(private val repository: GlucoseRepository) : ViewModel() {
    val entries by lazyDeferred {
        repository.getEntries()
    }

    suspend fun deleteEntry(id: Int) {
        repository.deleteEntry(id)
    }
}

