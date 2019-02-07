package com.gluco.Data.Local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import retrofit2.http.DELETE

@Dao
interface TodoEntityDao {
    @Query("SELECT * FROM todoEntities order by id desc")
    fun getAllEntries(): LiveData<List<NoteDomainModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(entry: List<NoteDomainModel>)

    @Query("DELETE FROM todoEntities")
    fun deleteAll()

    @Query("DELETE FROM todoEntities WHERE id = (:givenId)")
    fun deleteLocal(givenId: Int)
}