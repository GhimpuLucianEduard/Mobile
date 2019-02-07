package com.gluco.Data.Repository

import androidx.lifecycle.LiveData
import com.gluco.Data.Local.TaskDomainModel
import io.reactivex.Observable
import retrofit2.Response

interface TodoRepository {
    fun getEntries(): LiveData<List<TaskDomainModel>>
    fun update(newVale: TaskDomainModel)
}
