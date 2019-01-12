package com.gluco.Data.Repository

import androidx.lifecycle.LiveData
import com.gluco.Data.Local.GlucoseEntry
import io.reactivex.Observable

interface GlucoseRepository {
    fun getEntries(): LiveData<List<GlucoseEntry>>
    fun deleteEntry(entry: GlucoseEntry) : Observable<Any>
    fun addEntry(entry: GlucoseEntry) : Observable<GlucoseEntry>
    fun updateEntry(entry: GlucoseEntry) : Observable<GlucoseEntry>
    fun syncData() : Observable<Any>
}
