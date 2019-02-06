package com.gluco.Data.Repository

import androidx.lifecycle.LiveData
import com.gluco.Data.Local.NoteDomainModel
import io.reactivex.Observable

interface TodoRepository {
    fun getEntries(): LiveData<List<NoteDomainModel>>
    fun getEntriesByPage(page: Int): Observable<List<NoteDomainModel>>
}
