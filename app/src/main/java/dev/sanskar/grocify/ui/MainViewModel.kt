package dev.sanskar.grocify.ui

import android.app.Application
import androidx.lifecycle.*
import androidx.room.Room
import dev.sanskar.grocify.asDatabaseEntity
import dev.sanskar.grocify.data.db.DBInstance
import dev.sanskar.grocify.data.db.GrocifyDatabase
import dev.sanskar.grocify.data.db.RecordDao
import dev.sanskar.grocify.data.db.RecordEntity
import dev.sanskar.grocify.data.model.Record
import dev.sanskar.grocify.data.network.ApiService
import dev.sanskar.grocify.log
import kotlinx.coroutines.launch
import java.util.ArrayList

class MainViewModel(application: Application) : AndroidViewModel(application) {
    fun sortByPriceAsc() {
        viewModelScope.launch {
            transformedRecords.value = db.recordDao().getRecordsSortedByPriceAsc()
        }
    }

    val states = MutableLiveData<List<String>>()


    fun filterByDistricts(filterDistricts: ArrayList<String>?) {
        viewModelScope.launch {
            if (filterDistricts != null)
                transformedRecords.value = db.recordDao().getFilteredRecordsByDistrict(filterDistricts)
        }
    }

    fun sortByPriceDesc() {
        viewModelScope.launch {
            transformedRecords.value = db.recordDao().getRecordsSortedByPriceDesc()
        }
    }

    private val db = Room.databaseBuilder(application.applicationContext, GrocifyDatabase::class.java, "grocify-db")
    .allowMainThreadQueries()
    .fallbackToDestructiveMigration()
    .build()

    val transformedRecords = MutableLiveData<List<RecordEntity>>()
    val records = db.recordDao().getAllRecords()

    init {
        DBInstance.db = db
        viewModelScope.launch {
            val records = ApiService.retrofitService.getRecords().run {
                log("Received API Response: $this")
                records
            }
            log("Network response received, sample: ${records.subList(0, 5)}")
            db.recordDao().insertRecord(records.asDatabaseEntity())
        }
    }
}