package dev.sanskar.grocify.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.sanskar.grocify.R
import dev.sanskar.grocify.data.model.Record
import dev.sanskar.grocify.data.network.ApiService
import dev.sanskar.grocify.databinding.ActivityMainBinding
import dev.sanskar.grocify.databinding.LayoutPriceItemBinding
import dev.sanskar.grocify.log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val adapter = PriceListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.listPrices.adapter = adapter

        GlobalScope.launch {
            val records = ApiService.retrofitService.getRecords().records
            runOnUiThread {
                adapter.submitList(records)
                binding.progressBarLoadingPrices.visibility = View.GONE
            }
        }
    }
}

class PriceListAdapter : ListAdapter<Record, PriceListAdapter.ViewHolder>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<Record>() {
        // Find better primary key
        override fun areItemsTheSame(oldItem: Record, newItem: Record) = oldItem.market == newItem.market

        override fun areContentsTheSame(oldItem: Record, newItem: Record) = oldItem == newItem
    }

    class ViewHolder(val binding: LayoutPriceItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutPriceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with (holder.binding) {
            val record = getItem(position)
            textViewLocation.text = "${record.market}, ${record.district}, ${record.state}"
            textVoewCommodity.text = record.commodity
            textViewPrice.text = record.modal_price
        }
    }


}