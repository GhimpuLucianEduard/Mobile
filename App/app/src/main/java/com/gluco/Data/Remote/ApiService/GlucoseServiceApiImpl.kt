package com.gluco.Data.Remote.ApiService

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gluco.Data.Converter.GlucoseEntryConverter
import com.gluco.Data.Local.GlucoseEntry
import com.gluco.Data.Remote.GlucoseService
import com.gluco.Data.Remote.NoConnectivityException

class GlucoseServiceApiImpl(
    private val apiService: GlucoseApiService
) : GlucoseService {

    private val _data = MutableLiveData<List<GlucoseEntry>>()
    override val data: LiveData<List<GlucoseEntry>>
        get() = _data

    override suspend fun fetchEntries() {
        try {
            val data = apiService.getAllEntries().await()
            _data.postValue(data.map { entry -> GlucoseEntryConverter.convert(entry) })
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.")
        }
    }

    override suspend fun deleteEntry(id: String) {
        try {
            apiService.deleteEntry(id).await()
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.")
        }
    }

}