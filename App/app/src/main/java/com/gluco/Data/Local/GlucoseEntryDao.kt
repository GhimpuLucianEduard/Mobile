package com.gluco.Data.Local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface GlucoseEntryDao {
    @Query("SELECT * FROM glucoseEntries ORDER BY timestamp")
    fun getAllEntries(): LiveData<List<GlucoseEntry>>

    @Query("SELECT * FROM glucoseEntries WHERE id= (:givenId)")
    fun getOneById(givenId: Int) : GlucoseEntry

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(entry: GlucoseEntry)

    @Insert
    fun insertAll(entry: List<GlucoseEntry>)

    @Update
    fun update(entry: GlucoseEntry)

    @Query("DELETE FROM glucoseEntries")
    fun deleteAll()

    @Query("DELETE FROM glucoseEntries WHERE id = (:givenId)")
    fun deleteLocal(givenId: Int)
}