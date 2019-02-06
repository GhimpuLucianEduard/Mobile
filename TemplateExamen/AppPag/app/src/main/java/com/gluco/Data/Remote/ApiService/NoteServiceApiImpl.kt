package com.gluco.Data.Remote.ApiService

import com.gluco.Data.Converter.TodoEntryConverter
import com.gluco.Data.Local.NoteDomainModel
import io.reactivex.Observable

class NoteServiceApiImpl(private val apiService: ApiService) : NoteService {
    override fun fetchByPage(page: Int): Observable<List<NoteDomainModel>> {
        return apiService.getAllEntriesByPage(page)
                .map { item -> TodoEntryConverter.convertPageResponse(item) }
    }

    override fun fetchNotes(): Observable<List<NoteDomainModel>> {
        return apiService.getAllEntries()
                .map { item -> TodoEntryConverter.convertPageResponse(item) }
    }
}