package com.gluco.Data.Remote.ApiService

import androidx.preference.PreferenceManager
import com.gluco.Data.Remote.DataModels.GlucoseEntryDataModel
import com.gluco.Data.Remote.DataModels.UserDataModel
import com.gluco.Data.Remote.DataModels.UserWithTokenDataModel
import com.gluco.GlucoApp
import com.gluco.Utility.empty
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

    @POST("/user/login")
    fun login(@Body userData: UserDataModel) : Observable<UserWithTokenDataModel>

    @POST("/user/signup")
    fun register(@Body userDataModel: UserDataModel): Observable<Any>

    @POST("/glucose/sync")
    fun syncData(@Body data: List<GlucoseEntryDataModel>): Observable<Any>

    companion object {
        operator fun invoke(
                connectivityInterceptor: ConnectivityInterceptor
        ): GlucoseApiService {
            val requestInterceptor = Interceptor { chain ->

                val prefs = PreferenceManager.getDefaultSharedPreferences(GlucoApp.applicationContext())
                val token = prefs.getString("AUTH_TOKEN", String.empty())

                val request = chain.request()
                        .newBuilder()
                        .addHeader("Authorization", "Bearer $token")
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