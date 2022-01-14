package dev.sanskar.grocify.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import dev.sanskar.grocify.data.db.GrocifyDatabase
import dev.sanskar.grocify.data.network.ApiService
import dev.sanskar.grocify.log
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val db = Room.databaseBuilder(application.applicationContext, GrocifyDatabase::class.java, "grocify-db")
    .allowMainThreadQueries()
    .fallbackToDestructiveMigration()
    .build()

    val records = db.recordDao().getAllRecords()

    init {
        viewModelScope.launch {
            val records = ApiService.retrofitService.getRecords().run {
                log("Recieved API Response: $this")
                records
            }
            log("Network response received, sample: ${records.subList(0, 5)}")
            // Mapping IDs to indexes so that Room's replace strategy works properly
            records.mapIndexed { index, record ->
                record.id = index
            }
            db.recordDao().insertRecord(records)
        }
    }
}