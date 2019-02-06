package com.gluco.Data.Repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import com.gluco.Data.Local.NoteDomainModel
import com.gluco.Data.Local.TodoEntityDao
import com.gluco.Data.Remote.ApiService.NoteService
import com.gluco.ExamenApp
import com.gluco.Utility.LogConstants
import com.gluco.Utility.hasConnection
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync

class TodoRepositoryImpl(
        private val todoEntityDao: TodoEntityDao,
        private val service: NoteService
) : TodoRepository {

    @SuppressLint("CheckResult")
    override fun getEntriesByPage(page: Int): Observable<List<NoteDomainModel>> {
        return service.fetchByPage(page)
                .doOnNext { doAsync { todoEntityDao.insertAll(it) } }
    }

    val data: LiveData<List<NoteDomainModel>> = todoEntityDao.getAllEntries()

    override fun getEntries(): LiveData<List<NoteDomainModel>> {
        if ((data.value?.size == 0 || data.value == null) && hasConnection(ExamenApp.applicationContext())) {
            Log.i(LogConstants.INFO_TAG, "No data in local db, fetch from api...")
            getEntriesFromServer()
        }
        Log.i(LogConstants.INFO_TAG, "Data fetched: $data")
        val c = data.value
        return data
    }

    @SuppressLint("CheckResult")
    fun getEntriesFromServer() {
        service.fetchNotes()
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

    fun persistFetchedGlucoseEntries(data: List<NoteDomainModel>) {
        doAsync {
            todoEntityDao.deleteAll()
            todoEntityDao.insertAll(data)
        }
    }

}