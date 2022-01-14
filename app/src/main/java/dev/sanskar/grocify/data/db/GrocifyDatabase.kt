package dev.sanskar.grocify.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.sanskar.grocify.data.model.Record

@Database(entities = [RecordEntity::class], version = 3)
abstract class GrocifyDatabase : RoomDatabase() {
    abstract fun recordDao(): RecordDao
}

object DBInstance {
    lateinit var db: GrocifyDatabase
}