package com.gluco.Data.Local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TodoEntityDao {
    @Query("SELECT * FROM todoEntities order by updated desc")
    fun getAllEntries(): LiveData<List<TodoEntity>>

    @Insert
    fun insertAll(entry: List<TodoEntity>)

    @Query("DELETE FROM todoEntities")
    fun deleteAll()

    @Query("SELECT count(*) FROM todoEntities")
    fun hasValues() : Int

    @Query("Select max(updated) from todoEntities")
    fun getMax() : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entry: TodoEntity)
}