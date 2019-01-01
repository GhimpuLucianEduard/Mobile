package com.gluco.Data.Local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GlucoseEntryDao {
    @Query("SELECT * FROM glucoseEntries ORDER BY timestamp")
    fun getAllEntries(): LiveData<List<GlucoseEntry>>

    @Query("SELECT * FROM glucoseEntries WHERE id= (:givenId)")
    fun getOneById(givenId: Int) : GlucoseEntry

    @Insert
    fun insertAll(orders: List<GlucoseEntry>)

    @Query("DELETE FROM glucoseEntries")
    fun deleteAll()

    @Query("DELETE FROM glucoseEntries WHERE id = (:givenId)")
    fun deleteLocal(givenId: Int)
}