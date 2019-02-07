package com.gluco.Data.Local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
        entities = [TaskDomainModel::class],
        version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoEntityDao(): TasksEntityDao

    companion object {
        @Volatile private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "examenApp23.db").build()
    }
}