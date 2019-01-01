package com.gluco.Data.Repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.gluco.Data.Local.GlucoseEntry
import com.gluco.Data.Local.GlucoseEntryDao
import com.gluco.Data.Remote.GlucoseService
import io.reactivex.Observable
import org.jetbrains.anko.doAsync

class GlucoseRepositoryImpl(
    private val glucoseEntriesDao: GlucoseEntryDao,
    private val glucoseService: GlucoseService
) : GlucoseRepository {

    val data: LiveData<List<GlucoseEntry>> = glucoseEntriesDao.getAllEntries()

    override fun getEntries(): LiveData<List<GlucoseEntry>> {
        return data
    }

    private fun persistFetchedGlucoseEntries(data: List<GlucoseEntry>) {
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