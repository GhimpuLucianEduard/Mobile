package com.gluco.Data.Remote.ApiService

import com.gluco.Data.Local.NoteDomainModel
import io.reactivex.Observable
import retrofit2.Response

interface NoteService {
    fun fetchNotes() : Observable<List<NoteDomainModel>>
    fun fetchByPage(page: Int) : Observable<List<NoteDomainModel>>
    fun deleteEntry(id: Int): Observable<Response<Void>>
}