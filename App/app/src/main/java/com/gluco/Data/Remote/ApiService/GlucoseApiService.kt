package com.gluco.Data.Remote.ApiService

import com.gluco.Data.Remote.DataModels.GlucoseEntryDataModel
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface GlucoseApiService {

    @GET("/glucose")
    fun getAllEntries() : Deferred<List<GlucoseEntryDataModel>>

    @DELETE("/glucose/{id}")
    fun deleteEntry(@Path("id") id: String) : Deferred<Any>

    companion object {
        operator fun invoke(
                connectivityInterceptor: ConnectivityInterceptor
        ): GlucoseApiService {
            val requestInterceptor = Interceptor { chain ->

                val request = chain.request()
                        .newBuilder()
                        .addHeader("Authorization", "Barer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6Imx1Y2ZzZGZzZGlhbjIzQGEuY29tIiwidXNlcklkIjoiNWMyN2NlMmZmZGRjMGM0Mzc0ZDdhNGVlIiwiaWF0IjoxNTQ2MTE0NDI5LCJleHAiOjE2MzI1MTQ0Mjl9.g-kzQtnm3fnflXMsAbVFnrx4RajpV8wviareTsKPQw4")
                        .build()

                return@Interceptor chain.proceed(request)
            }

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC

            val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(requestInterceptor)
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(connectivityInterceptor)
                    .build()

            return Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://10.0.2.2:329")
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(GlucoseApiService::class.java)
        }
    }
}