package dev.sanskar.grocify.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.sanskar.grocify.data.model.Record

@Dao
interface RecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecord(records: List<Record>)

    @Query("SELECT * FROM records")
    fun getAllRecords(): LiveData<List<Record>>

    @Query("DELETE FROM records")
    fun clear()
}