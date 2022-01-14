package dev.sanskar.grocify.ui

import android.app.Application
import androidx.lifecycle.*
import androidx.room.Room
import dev.sanskar.grocify.asDatabaseEntity
import dev.sanskar.grocify.data.db.GrocifyDatabase
import dev.sanskar.grocify.data.db.RecordDao
import dev.sanskar.grocify.data.db.RecordEntity
import dev.sanskar.grocify.data.model.Record
import dev.sanskar.grocify.data.network.ApiService
import dev.sanskar.grocify.log
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    fun sortByPriceAsc() {
        viewModelScope.launch {
            transformedRecords.value = db.recordDao().getRecordsSortedByPriceAsc()
        }
    }

    val db = Room.databaseBuilder(application.applicationContext, GrocifyDatabase::class.java, "grocify-db")
    .allowMainThreadQueries()
    .fallbackToDestructiveMigration()
    .build()

    val transformedRecords = MutableLiveData<List<RecordEntity>>()
    val records = db.recordDao().getAllRecords()

    init {
        viewModelScope.launch {
            val records = ApiService.retrofitService.getRecords().run {
                log("Recieved API Response: $this")
                records
            }
            log("Network response received, sample: ${records.subList(0, 5)}")
            db.recordDao().insertRecord(records.asDatabaseEntity())
        }
    }
}