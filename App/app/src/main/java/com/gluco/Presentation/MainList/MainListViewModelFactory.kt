package com.gluco.Presentation.MainList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gluco.Data.Repository.GlucoseRepository

@Suppress("UNCHECKED_CAST")
class MainListViewModelFactory(
    private val repository: GlucoseRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainListViewModel(repository) as T
    }
}