package dev.sanskar.grocify.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.sanskar.grocify.data.model.Record

@Database(entities = [Record::class], version = 2)
abstract class GrocifyDatabase : RoomDatabase() {
    abstract fun recordDao(): RecordDao
}