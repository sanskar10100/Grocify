package dev.sanskar.grocify.ui.filter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.sanskar.grocify.data.db.DBInstance
import kotlinx.coroutines.launch

class FilterViewModel : ViewModel() {
    private val db = DBInstance.db
    val states = MutableLiveData<List<String>>()

    /**
     * Fetch a list of all the distinct states from the db
     */
    fun getDistinctStates() {
        viewModelScope.launch {
            states.value = db.recordDao().getDistinctStates()
        }
    }

    var selectedStates = mutableListOf<String>()
    var selectedDistricts = mutableListOf<String>()

    /**
     * Fetch a list of all the districts for the selected states
     */
    val districts = MutableLiveData<List<String>>()
    fun getDistricts() {
        viewModelScope.launch {
            districts.value = db.recordDao().getDistrictsForState(selectedStates)
        }
    }
}