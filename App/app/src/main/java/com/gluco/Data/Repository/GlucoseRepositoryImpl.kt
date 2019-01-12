package com.gluco.Data.Repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.gluco.Data.Local.GlucoseEntry
import com.gluco.Data.Local.GlucoseEntryDao
import com.gluco.Data.Remote.GlucoseService
import com.gluco.GlucoApp
import com.gluco.Utility.hasConnection
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Dispatcher
import org.jetbrains.anko.doAsync

class GlucoseRepositoryImpl(
    private val glucoseEntriesDao: GlucoseEntryDao,
    private val glucoseService: GlucoseService
) : GlucoseRepository {

    override fun syncData(): Observable<Any> {
        val debug = data.value
        if (debug != null) {
            return glucoseService.syncData(data.value!!)
        } else {
            return Observable.empty()
        }
    }

    val data: LiveData<List<GlucoseEntry>> = glucoseEntriesDao.getAllEntries()

    override fun getEntries(): LiveData<List<GlucoseEntry>> {
        if ((data.value?.size == 0 || data.value == null) && hasConnection(GlucoApp.applicationContext())) {
            Log.e("wow", "uganda")
            getEntriesFromServer()
        }
        Log.e("wow", "uganda2")
        Log.e("wow", "uganda3: ${data.value.toString()}")
        return data
    }

    fun getEntriesFromServer() {
        glucoseService.fetchEntries()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                persistFetchedGlucoseEntries(it)
            },{
                Log.e("Error", "Fetch data failed: ${it.message}")
            }, {
                Log.e("Error", "Fetched data finished")
            })
    }

    fun persistFetchedGlucoseEntries(data: List<GlucoseEntry>) {
        doAsync {
            glucoseEntriesDao.deleteAll()
            glucoseEntriesDao.insertAll(data)
        }
    }

    override fun deleteEntry(entry: GlucoseEntry): Observable<Any> {
        return glucoseService.deleteEntry(entry.apiId!!)
            .doOnComplete { doAsync { glucoseEntriesDao.deleteLocal(entry.id!!) } }
            .doOnError { doAsync { glucoseEntriesDao.deleteLocal(entry.id!!) } }
    }

    override fun addEntry(entry: GlucoseEntry): Observable<GlucoseEntry> {
        return glucoseService.addEntry(entry)
            .doOnNext { doAsync { glucoseEntriesDao.insertOne(it) } }
            .doOnError { doAsync { glucoseEntriesDao.insertOne(entry) } }
    }

    override fun updateEntry(entry: GlucoseEntry) : Observable<GlucoseEntry> {
        return glucoseService.updateEntry(entry)
            .doOnNext { doAsync { glucoseEntriesDao.update(it) } }
            .doOnError { doAsync { glucoseEntriesDao.update(entry) } }
    }
}