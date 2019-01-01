package com.gluco.Data.Remote.ApiService

import com.gluco.Data.Remote.DataModels.GlucoseEntryDataModel
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface GlucoseApiService {

    @GET("/glucose")
    fun getAllEntries() : Observable<List<GlucoseEntryDataModel>>

    @DELETE("/glucose/{id}")
    fun deleteEntry(@Path("id") id: String) : Observable<Any>

    @POST("/glucose")
    fun addEntry(@Body entry: GlucoseEntryDataModel) : Observable<GlucoseEntryDataModel>

    @PATCH("/glucose")
    fun updateEntry(@Body entry: GlucoseEntryDataModel) : Observable<GlucoseEntryDataModel>

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
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .build()
                    .create(GlucoseApiService::class.java)
        }
    }
}