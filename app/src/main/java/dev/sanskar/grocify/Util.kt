package dev.sanskar.grocify

import android.util.Log
import dev.sanskar.grocify.data.db.RecordEntity
import dev.sanskar.grocify.data.model.Record

fun log(message: String) {
    Log.d("GrocifyApp", message)
}

fun List<Record>.asDatabaseEntity(): List<RecordEntity> {
    val dbRecords = mutableListOf<RecordEntity>()
    for (i in 0 until size) {
        dbRecords.add(
            RecordEntity(
                i,
                this[i].arrival_date,
                this[i].commodity,
                this[i].district,
                this[i].market,
                this[i].max_price,
                this[i].min_price,
                this[i].modal_price.toFloat(),
                this[i].state,
                this[i].variety
            )
        )
    }

    return dbRecords
}