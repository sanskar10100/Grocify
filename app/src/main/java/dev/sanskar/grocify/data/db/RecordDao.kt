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
}