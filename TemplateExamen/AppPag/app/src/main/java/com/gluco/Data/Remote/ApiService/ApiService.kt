package com.gluco.Data.Remote.ApiService

import com.gluco.Data.Remote.DataModels.NoteDataModel
import com.gluco.Data.Remote.DataModels.NotesResponse
import com.gluco.Data.Remote.DataModels.UserDataModel
import com.gluco.Data.Remote.DataModels.UserWithTokenDataModel
import com.gluco.Utility.ApiConstants
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface ApiService {

    @GET("/note")
    fun getAllEntries() : Observable<NotesResponse>

    @GET("/note")
    fun getAllEntriesByPage(@Query("page")pageNr: Int) : Observable<NotesResponse>

    @POST("/user/login")
    fun login(@Body userData: UserDataModel) : Observable<UserWithTokenDataModel>

    @POST("/user/signup")
    fun register(@Body userDataModel: UserDataModel): Observable<Any>

    companion object {
        operator fun invoke(
                connectivityInterceptor: ConnectivityInterceptor
        ): ApiService {

//            val requestInterceptor = Interceptor { chain ->
//
//                val prefs = PreferenceManager.getDefaultSharedPreferences(ExamenApp.applicationContext())
//                val token = prefs.getString("AUTH_TOKEN", String.empty())
//
//                val request = chain.request()
//                        .newBuilder()
//                        .addHeader("Authorization", "Bearer $token")
//                        .build()
//
//                return@Interceptor chain.proceed(request)
//            }

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC

            val okHttpClient = OkHttpClient.Builder()
                    //.addInterceptor(requestInterceptor)
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(connectivityInterceptor)
                    .build()

            return Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(ApiConstants.BASE_URL)
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .build()
                    .create(ApiService::class.java)
        }
    }
}