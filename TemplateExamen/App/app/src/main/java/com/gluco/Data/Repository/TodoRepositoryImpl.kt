package com.gluco.Data.Repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import com.gluco.Data.Local.TodoEntity
import com.gluco.Data.Local.TodoEntityDao
import com.gluco.Data.Remote.ApiService.TodoService
import com.gluco.ExamenApp
import com.gluco.Utility.LogConstants
import com.gluco.Utility.hasConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync

class TodoRepositoryImpl(
        private val todoEntityDao: TodoEntityDao,
        private val service: TodoService
) : TodoRepository {

    val data: LiveData<List<TodoEntity>> = todoEntityDao.getAllEntries()

    override fun getEntries(): LiveData<List<TodoEntity>> {
        if ((data.value?.size == 0 || data.value == null) && hasConnection(ExamenApp.applicationContext())) {
            Log.i(LogConstants.INFO_TAG, "No data in local db, fetch from api...")
            getEntriesFromServer()
        }
        Log.i(LogConstants.INFO_TAG, "Data fetched: $data")
        return data
    }

    @SuppressLint("CheckResult")
    fun getEntriesFromServer() {
        service.fetchTodos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    persistFetchedGlucoseEntries(it)
                }, {
                    Log.e("Error", "Fetch data failed: ${it.message}")
                }, {
                    Log.e("Error", "Fetched data finished")
                })
    }

    fun persistFetchedGlucoseEntries(data: List<TodoEntity>) {
        doAsync {
            todoEntityDao.deleteAll()
            todoEntityDao.insertAll(data)
        }
    }

}