package dev.sanskar.grocify.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import dev.sanskar.grocify.R
import dev.sanskar.grocify.data.db.GrocifyDatabase
import dev.sanskar.grocify.data.db.RecordEntity
import dev.sanskar.grocify.data.model.Record
import dev.sanskar.grocify.data.network.ApiService
import dev.sanskar.grocify.databinding.ActivityMainBinding
import dev.sanskar.grocify.databinding.LayoutPriceItemBinding
import dev.sanskar.grocify.log
import dev.sanskar.grocify.ui.filter.FilterParameterFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val adapter = PriceListAdapter()
    private val model by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.listPrices.adapter = adapter

        model.records.observe(this) {
            if (it.isNotEmpty()) {
                log("Received data from room, sample: ${it.subList(0, 5)}")
                adapter.submitList(it)
                binding.progressBarLoadingPrices.visibility = View.GONE
            }
        }

        binding.buttonSort.setOnClickListener {
            model.sortByPriceAsc()
        }

        binding.buttonFilter.setOnClickListener {
            FilterParameterFragment().show(supportFragmentManager, "filter")
        }

        supportFragmentManager.setFragmentResultListener("FILTER", this) { _, bundle ->
            val filterDistricts = bundle.getStringArrayList("DISTRICTS")
            log("Filtered Districts: $filterDistricts")
            model.filterByDistricts(filterDistricts)
        }

        model.transformedRecords.observe(this) {
            adapter.submitList(it)
        }
    }
}

class PriceListAdapter : ListAdapter<RecordEntity, PriceListAdapter.ViewHolder>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<RecordEntity>() {
        // TODO Find better primary key
        override fun areItemsTheSame(oldItem: RecordEntity, newItem: RecordEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RecordEntity, newItem: RecordEntity): Boolean {
            return oldItem == newItem
        }
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
            textViewPrice.text = record.modal_price.toString()
        }
    }
}