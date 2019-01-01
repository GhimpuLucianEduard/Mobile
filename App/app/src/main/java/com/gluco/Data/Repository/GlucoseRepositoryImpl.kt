package com.gluco.Data.Repository

import androidx.lifecycle.LiveData
import com.gluco.Data.Local.GlucoseEntry
import com.gluco.Data.Local.GlucoseEntryDao
import com.gluco.Data.Remote.GlucoseService
import kotlinx.coroutines.*

class GlucoseRepositoryImpl(
    private val glucoseEntrieyDao: GlucoseEntryDao,
    private val glucoseService: GlucoseService
) : GlucoseRepository {
    init {
        glucoseService.data.observeForever { newData ->
            persistFetchedGlucoseEntries(newData)
        }
    }

    override suspend fun getEntries(): LiveData<List<GlucoseEntry>> {
        return withContext(Dispatchers.IO) {
            getEntriesRemote()
            return@withContext glucoseEntrieyDao.getAllEntries()
        }
    }

    private fun persistFetchedGlucoseEntries(data: List<GlucoseEntry>) {
        GlobalScope.launch(Dispatchers.IO) {
            glucoseEntrieyDao.deleteAll()
            glucoseEntrieyDao.insertAll(data)
        }
    }

    private suspend fun getEntriesRemote() {
        glucoseService.fetchEntries()
    }

    override suspend fun deleteEntry(id: Int) {
        glucoseService.deleteEntry(glucoseEntrieyDao.getOneById(id).apiId!!)
        glucoseEntrieyDao.deleteLocal(id)
    }

}