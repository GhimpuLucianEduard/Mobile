package com.gluco.Data.Remote

import androidx.lifecycle.LiveData
import com.gluco.Data.Local.GlucoseEntry

interface GlucoseService {
    val data: LiveData<List<GlucoseEntry>>
    suspend fun fetchEntries()
    suspend fun deleteEntry(id: String)
}