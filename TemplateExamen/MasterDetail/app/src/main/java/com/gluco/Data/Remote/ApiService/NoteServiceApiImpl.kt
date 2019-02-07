package com.gluco.Data.Remote.ApiService

import com.gluco.Data.Converter.TodoEntryConverter
import com.gluco.Data.Local.TaskDomainModel
import io.reactivex.Observable
import retrofit2.Response

class NoteServiceApiImpl(private val apiService: ApiService) : NoteService {


    override fun fetchNotes(last: Long): Observable<List<TaskDomainModel>> {
        return apiService.getAllEntriesByPage(last)
            .flatMapIterable {list->list}
            .map { item -> TodoEntryConverter.convert(item) }
            .toList()
            .toObservable()
    }
}