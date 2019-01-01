package com.gluco.Data.Remote

import com.gluco.Data.Local.GlucoseEntry
import io.reactivex.Observable

interface GlucoseService {
    fun fetchEntries() : Observable<List<GlucoseEntry>>
    fun addEntry(entry: GlucoseEntry) : Observable<GlucoseEntry>
    fun updateEntry(entry: GlucoseEntry) : Observable<GlucoseEntry>
    fun deleteEntry(id: String) : Observable<Any>
}