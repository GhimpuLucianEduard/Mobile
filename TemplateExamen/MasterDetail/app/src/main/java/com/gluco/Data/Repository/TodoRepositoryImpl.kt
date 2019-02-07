package com.gluco.Data.Repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import com.gluco.Data.Local.TaskDomainModel
import com.gluco.Data.Local.TasksEntityDao
import com.gluco.Data.Remote.ApiService.NoteService
import com.gluco.ExamenApp
import com.gluco.Utility.LogConstants
import com.gluco.Utility.hasConnection
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import retrofit2.Response

class TodoRepositoryImpl(
    private val tasksEntityDao: TasksEntityDao,
    private val service: NoteService
) : TodoRepository {


    override fun update(newVale: TaskDomainModel) {
        tasksEntityDao.update(newVale)
    }

    val data: LiveData<List<TaskDomainModel>> = tasksEntityDao.getAllEntries()

    @SuppressLint("CheckResult")
    override fun getEntries(): LiveData<List<TaskDomainModel>> {

        doAsync {
            var last : Long

            var hasRow = tasksEntityDao.hasValues()
            if (hasRow != 0) {
                last = tasksEntityDao.getMax()
            } else {
                last = 0
            }
            Log.i(LogConstants.INFO_TAG, "Data fetched: $data")

            service.fetchNotes(last)
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
        return data
    }


    fun persistFetchedGlucoseEntries(data: List<TaskDomainModel>) {
        doAsync {
            tasksEntityDao.deleteAll()
            tasksEntityDao.insertAll(data)
        }
    }

}