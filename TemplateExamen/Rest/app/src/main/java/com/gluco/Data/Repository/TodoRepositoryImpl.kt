package com.gluco.Data.Repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import com.gluco.Data.Converter.TodoEntryConverter
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

    override fun add(todo: TodoEntity) {
        doAsync { todoEntityDao.insert(todo) }
    }

    val data: LiveData<List<TodoEntity>> = todoEntityDao.getAllEntries()

    override fun getEntries(): LiveData<List<TodoEntity>> {

        doAsync {
            var last: Long

            var hasRow = todoEntityDao.hasValues()
            if (hasRow != 0) {
                last = todoEntityDao.getMax()
            } else {
                last = 0
            }

            service.fetchTodos(last, 1)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        doAsync {
                            persistFetchedGlucoseEntries(TodoEntryConverter.convertListToDomain(it.items))
                            if (it.more) {
                                fetchRemote(last, it.page + 1)
                            }
                        }
                    }, {
                        Log.e("Error", "Fetch data failed: ${it.message}")
                    }, {
                        Log.e("Error", "Fetched data finished")
                    })

        }
        Log.i(LogConstants.INFO_TAG, "Data fetched: $data")
        return data
    }


    @SuppressLint("CheckResult")
    fun fetchRemote(last: Long, page: Int) {
        service.fetchTodos(last, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    doAsync { todoEntityDao.insertAll(TodoEntryConverter.convertListToDomain(it.items)) }
                    if (it.more) {
                        fetchRemote(last, it.page + 1)
                    }

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