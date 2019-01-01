package com.gluco.Data.Repository

import androidx.lifecycle.LiveData
import com.gluco.Data.Local.GlucoseEntry

interface GlucoseRepository {
    suspend fun getEntries(): LiveData<List<GlucoseEntry>>
    suspend fun deleteEntry(id: Int)
}