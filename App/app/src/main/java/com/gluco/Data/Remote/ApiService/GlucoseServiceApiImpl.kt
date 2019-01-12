package com.gluco.Data.Remote.ApiService

import com.gluco.Data.Converter.GlucoseEntryConverter
import com.gluco.Data.Local.GlucoseEntry
import com.gluco.Data.Remote.GlucoseService
import io.reactivex.Observable

class GlucoseServiceApiImpl(private val apiService: GlucoseApiService) : GlucoseService {

    override fun syncData(data: List<GlucoseEntry>): Observable<Any> {
        return apiService.syncData(GlucoseEntryConverter.convertListToData(data))
    }

    override fun fetchEntries() : Observable<List<GlucoseEntry>> {
        return apiService.getAllEntries()
            .flatMapIterable{list->list}
            .map{item -> GlucoseEntryConverter.convert(item) }
            .toList()
            .toObservable()
    }

    override fun addEntry(entry: GlucoseEntry): Observable<GlucoseEntry> {
        return apiService.addEntry(GlucoseEntryConverter.convert(entry))
            .map{ item -> GlucoseEntryConverter.convert (item)}
    }

    override fun updateEntry(entry: GlucoseEntry): Observable<GlucoseEntry> {
        return apiService.updateEntry(GlucoseEntryConverter.convert(entry))
            .map{ item -> GlucoseEntryConverter.convert (item)}
    }

    override fun deleteEntry(id: String) : Observable<Any> {
        return apiService.deleteEntry(id)
    }

}