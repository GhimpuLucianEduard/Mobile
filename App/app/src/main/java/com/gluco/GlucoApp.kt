package com.gluco

import android.app.Application
import android.content.Context
import com.gluco.Data.Local.GlucoseDatabase
import com.gluco.Data.Remote.ApiService.*
import com.gluco.Data.Remote.AuthService
import com.gluco.Data.Remote.GlucoseService
import com.gluco.Data.Repository.GlucoseRepository
import com.gluco.Data.Repository.GlucoseRepositoryImpl
import com.gluco.Presentation.Auth.AuthViewModelFactory
import com.gluco.Presentation.MainList.MainListViewModelFactory
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import kotlin.math.sign

class GlucoApp : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@GlucoApp))

        bind() from singleton { GlucoseDatabase(instance()) }
        bind() from singleton { instance<GlucoseDatabase>().glucoseEntryDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { GlucoseApiService(instance()) }
        bind<GlucoseService>() with singleton { GlucoseServiceApiImpl(instance()) }
        bind<AuthService>() with singleton { AuthServiceApiImpl(instance()) }
        bind<GlucoseRepository>() with singleton { GlucoseRepositoryImpl(instance(), instance()) }
        bind() from singleton { MainListViewModelFactory(instance()) }
        bind() from singleton { AuthViewModelFactory(instance()) }

    }

    init {
        instance = this
    }

    companion object {
        private var instance: GlucoApp? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        val context: Context = GlucoApp.applicationContext()
        AndroidThreeTen.init(this)
    }
}