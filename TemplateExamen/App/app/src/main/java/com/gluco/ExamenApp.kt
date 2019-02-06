package com.gluco

import android.app.Application
import android.content.Context
import com.gluco.Data.Local.AppDatabase
import com.gluco.Data.Remote.ApiService.*
import com.gluco.Data.Remote.AuthService
import com.gluco.Data.Remote.GlucoseService
import com.gluco.Data.Repository.TodoRepository
import com.gluco.Data.Repository.TodoRepositoryImpl
import com.gluco.Presentation.Auth.AuthViewModelFactory
import com.gluco.Presentation.MainList.MainListViewModelFactory
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class ExamenApp : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@ExamenApp))

        bind() from singleton { AppDatabase(instance()) }
        bind() from singleton { instance<AppDatabase>().todoEntityDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { ApiService(instance()) }
        bind<TodoService>() with singleton { TodoServiceApiImpl(instance()) }
        bind<AuthService>() with singleton { AuthServiceApiImpl(instance()) }
        bind<TodoRepository>() with singleton { TodoRepositoryImpl(instance(), instance()) }
        bind() from singleton { MainListViewModelFactory(instance()) }
        bind() from singleton { AuthViewModelFactory(instance()) }
    }

    init {
        instance = this
    }

    companion object {
        private var instance: ExamenApp? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        val context: Context = ExamenApp.applicationContext()
        AndroidThreeTen.init(this)
    }
}