package dev.sanskar.grocify.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Separate db entity was created to properly hold the id and the modal_price
 */
@Entity(tableName = "records")
data class RecordEntity(
    @PrimaryKey var id: Int = 0,
    val arrival_date: String,
    val commodity: String,
    val district: String,
    val market: String,
    val max_price: String,
    val min_price: String,
    val modal_price: Float,
    val state: String,
    val variety: String
)