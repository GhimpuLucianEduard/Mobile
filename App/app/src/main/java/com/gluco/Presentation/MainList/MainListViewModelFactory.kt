package com.gluco.Presentation.MainList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gluco.Data.Repository.GlucoseRepository

@Suppress("UNCHECKED_CAST")
class MainListViewModelFactory(
    private val repository: GlucoseRepository
) : ViewModelProvider.NewInstanceFactory() {

    var instance: MainListViewModel? = null

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (instance == null) {
            instance = MainListViewModel(repository)
        }
        return instance as T
    }
}