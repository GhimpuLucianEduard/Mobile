package com.gluco.Data.Local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TodoEntityDao {
    @Query("SELECT * FROM todoEntities")
    fun getAllEntries(): LiveData<List<TodoEntity>>

    @Insert
    fun insertAll(entry: List<TodoEntity>)

    @Query("DELETE FROM todoEntities")
    fun deleteAll()
}