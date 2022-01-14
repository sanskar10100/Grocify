package dev.sanskar.grocify.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import dev.sanskar.grocify.R
import dev.sanskar.grocify.data.network.ApiService
import dev.sanskar.grocify.log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GlobalScope.launch {
            val result = ApiService.retrofitService.getRecords()
            log(result.toString())
        }
    }
}