package com.gluco.Data.Local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.security.AccessControlContext


@Database(
        entities = [GlucoseEntry::class],
        version = 1
)
abstract class GlucoseDatabase : RoomDatabase() {
    abstract fun glucoseEntryDao(): GlucoseEntryDao

    companion object {
        @Volatile private var instance: GlucoseDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext, GlucoseDatabase::class.java, "glucose.db").build()
    }
}