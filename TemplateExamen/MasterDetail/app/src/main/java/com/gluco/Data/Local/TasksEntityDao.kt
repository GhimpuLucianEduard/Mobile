package com.gluco.Data.Local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TasksEntityDao {
    @Query("SELECT * FROM tasks order by updated desc")
    fun getAllEntries(): LiveData<List<TaskDomainModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(entry: List<TaskDomainModel>)

    @Query("DELETE FROM tasks")
    fun deleteAll()

    @Query("SELECT count(*) FROM tasks")
    fun hasValues() : Int

    @Query("Select max(updated) from tasks")
    fun getMax() : Long

    @Update
    fun update(entry: TaskDomainModel)

//    @Query("DELETE FROM todoEntities WHERE id = (:givenId)")
//    fun deleteLocal(givenId: Int)
}