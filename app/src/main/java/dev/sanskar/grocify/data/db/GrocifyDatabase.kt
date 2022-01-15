package dev.sanskar.grocify.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.sanskar.grocify.data.model.Record

@Database(entities = [RecordEntity::class], version = 1)
abstract class GrocifyDatabase : RoomDatabase() {
    abstract fun recordDao(): RecordDao
}

// DB Instance is created and assigned on app startup
object DBInstance {
    lateinit var db: GrocifyDatabase
}