package dev.sanskar.grocify.ui.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import dev.sanskar.grocify.R
import dev.sanskar.grocify.databinding.FragmentFilterParameterBinding
import dev.sanskar.grocify.log

class FilterParameterFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentFilterParameterBinding
    private val model by viewModels<FilterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFilterParameterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        log("Filter Dialog created")
        model.states.observe(viewLifecycleOwner) {
            log("States count: ${it.size}")
            it.forEach { state ->
                val stateChip = layoutInflater.inflate(R.layout.layout_district_filter_chip, binding.chipGroupFilter, false) as Chip
                stateChip.text = state
                binding.chipGroupFilter.addView(stateChip)
            }
        }
        model.getDistinctStates()

        binding.buttonToDistrict.setOnClickListener {
            // Now we get the list of districts; find checked states
            model.selectedStates = binding.chipGroupFilter.children
                .filter {
                    (it as Chip).isChecked
                }
                .map {
                    (it as Chip).text.toString()
                }.toMutableList()
            log("Selected states: ${model.selectedStates}")
            if (model.selectedStates.isNotEmpty()) {
                // Get list of districts
                binding.progressBarLoadingDistricts.visibility = View.VISIBLE
                model.getDistricts()
            } else {
                Toast.makeText(context, "Please select at least one state!", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        model.districts.observe(viewLifecycleOwner) {
            log("Districts count: ${it.size}")
            binding.chipGroupFilter.visibility = View.GONE
            binding.buttonToDistrict.visibility = View.GONE
            binding.chipGroupFilterDistricts.visibility = View.VISIBLE
            binding.buttonGenerateFilterResult.visibility = View.VISIBLE
            binding.textViewFilterHint.text = "Please select one or more districts"
            it.forEach { district ->
                val districtChip = layoutInflater.inflate(R.layout.layout_district_filter_chip, binding.chipGroupFilterDistricts, false) as Chip
                districtChip.text = district
                binding.chipGroupFilterDistricts.addView(districtChip)
            }
            binding.progressBarLoadingDistricts.visibility = View.GONE
        }

        binding.buttonGenerateFilterResult.setOnClickListener {
            model.selectedDistricts = binding.chipGroupFilterDistricts.children
                .filter {
                    (it as Chip).isChecked
                }
                .map {
                    (it as Chip).text.toString()
                }.toMutableList()
            log("Selected districts: ${model.selectedDistricts}")
            if (model.selectedDistricts.isNotEmpty()) {
                setFragmentResult("FILTER", bundleOf("DISTRICTS" to model.selectedDistricts))
                dismiss()
            } else {
                Toast.makeText(context, "Please select at least one district!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}