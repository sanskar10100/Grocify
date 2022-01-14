package dev.sanskar.grocify.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecord(records: List<RecordEntity>)

    @Query("SELECT * FROM records")
    fun getAllRecords(): LiveData<List<RecordEntity>>

    @Query("SELECT * FROM records ORDER BY modal_price")
    suspend fun getRecordsSortedByPriceAsc(): List<RecordEntity>

    @Query("Select DISTINCT state FROM records")
    suspend fun getDistinctStates(): List<String>

    @Query("SELECT DISTINCT district FROM records WHERE state IN(:states)")
    suspend fun getDistrictsForState(states: List<String>): List<String>

    @Query("SELECT * FROM records WHERE district IN(:districts)")
    suspend fun getFilteredRecordsByDistrict(districts: List<String>): List<RecordEntity>
}