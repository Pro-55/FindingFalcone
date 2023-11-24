package com.example.findingfalcone.ui.selection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.example.findingfalcone.R
import com.example.findingfalcone.databinding.FragmentSelectionBinding
import com.example.findingfalcone.framework.BaseFragment
import com.example.findingfalcone.model.FalconeStatus
import com.example.findingfalcone.model.Planet
import com.example.findingfalcone.model.Resource
import com.example.findingfalcone.model.Vehicle
import com.example.findingfalcone.ui.selection.vehicles_sheet.VehiclesSheetFragment
import com.example.findingfalcone.util.extensions.gone
import com.example.findingfalcone.util.extensions.showConfirmationDialog
import com.example.findingfalcone.util.extensions.showShortSnackBar
import com.example.findingfalcone.util.extensions.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectionFragment : BaseFragment() {

    //Global
    private val TAG = SelectionFragment::class.java.simpleName
    private lateinit var binding: FragmentSelectionBinding
    private val viewModel by viewModels<SelectionViewModel>()
    private var adapter: SelectionAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_selection,
            container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateInfoView(0, 0)

        setupRecyclerView()

        setListeners()

        setObservers()
    }

    private fun updateInfoView(
        selectedCount: Int,
        estimatedTime: Int
    ) {
        binding.txtSelectionDetails.text = resources.getString(
            R.string.label_selection_details,
            selectedCount,
            selectedCount,
            estimatedTime
        )
    }

    private fun setupRecyclerView() {
        adapter = SelectionAdapter(object : SelectionAdapter.Listener {
            override fun onSelect(planet: Planet) {
                if (viewModel.getSelectionSize() > 3) {
                    return
                }
                viewModel.getAvailableVehicles(planet = planet)
            }

            override fun onUnselect(planet: Planet) {
                viewModel.unselectPlanetAndVehicle(planet)
            }
        })
        binding.recyclerSelection.layoutManager = GridLayoutManager(
            requireContext(),
            2,
            VERTICAL, false
        )
        binding.recyclerSelection.adapter = adapter
    }

    private fun setListeners() {
        binding.fabFind.setOnClickListener { viewModel.findFalcone() }
    }

    private fun setObservers() {
        viewModel.state.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    showLoader()
                    adapter?.setIsEnabled(false)
                }
                is Resource.Success -> {
                    val data = resource.data!!

                    adapter?.apply {
                        swapData(data = data.planets)
                        setIsEnabled(true)
                    }

                    val selectedCount = data.selectedCount
                    updateInfoView(selectedCount, data.estimatedTime)
                    binding.fabFind.isEnabled = selectedCount > 3
                    hideLoader()
                }
                is Resource.Error -> {
                    showShortSnackBar(message = resource.msg.getMessage(resources))
                    adapter?.setIsEnabled(true)
                    hideLoader()
                }
            }
        }

        viewModel.availableVehicles.observe(viewLifecycleOwner) { resource ->
            if (resource == null) return@observe
            when (resource) {
                is Resource.Loading -> adapter?.setIsEnabled(false)
                is Resource.Success -> {
                    viewModel.resetAvailableVehicles()
                    val data = resource.data!!
                    showVehiclesSheet(data.first, data.second)
                    adapter?.setIsEnabled(true)
                }
                is Resource.Error -> {
                    showShortSnackBar(message = resource.msg.getMessage(resources))
                    viewModel.resetAvailableVehicles()
                    adapter?.setIsEnabled(true)
                }
            }
        }

        viewModel.falconeStatus.observe(viewLifecycleOwner) { resource ->
            if (resource == null) return@observe
            when (resource) {
                is Resource.Loading -> adapter?.setIsEnabled(false)
                is Resource.Success -> {
                    viewModel.resetFalconeStatus()

                    when (val data = resource.data!!) {
                        is FalconeStatus.Success -> showConfirmationDialog(
                            icon = R.drawable.ic_success,
                            title = resources.getString(R.string.label_falcone_found),
                            message = resources.getString(
                                R.string.msg_falcone_found,
                                data.planet,
                                data.time
                            ),
                            positiveButtonClick = { viewModel.resetData() },
                            negativeButtonClick = { requireActivity().finish() }
                        )
                        is FalconeStatus.Failure -> showConfirmationDialog(
                            icon = R.drawable.ic_failure,
                            title = resources.getString(R.string.label_falcone_not_found),
                            message = resources.getString(R.string.msg_falcone_not_found),
                            negativeButtonClick = { requireActivity().finish() }
                        )
                        is FalconeStatus.InvalidToken -> showConfirmationDialog(
                            icon = R.drawable.ic_invalid_token,
                            title = resources.getString(R.string.label_invalid_token),
                            message = resources.getString(R.string.msg_invalid_token),
                            positiveButtonClick = { viewModel.getToken() },
                            negativeButtonClick = { requireActivity().finish() }
                        )
                    }

                    adapter?.setIsEnabled(true)
                }
                is Resource.Error -> {
                    showShortSnackBar(message = resource.msg.getMessage(resources))
                    viewModel.resetFalconeStatus()
                    adapter?.setIsEnabled(true)
                }
            }
        }
    }

    private fun showVehiclesSheet(
        planet: Planet,
        vehicles: List<Vehicle>
    ) {
        if (vehicles.isEmpty()) {
            showShortSnackBar(message = resources.getString(R.string.error_unknown))
            return
        }
        VehiclesSheetFragment.showDialog(
            vehicles = vehicles,
            listener = object : VehiclesSheetFragment.Listener {
                override fun onVehicleSelected(vehicle: Vehicle) {
                    viewModel.selectPlanetAndVehicle(
                        planet = planet,
                        vehicle = vehicle
                    )
                }
            },
            fm = childFragmentManager,
            tag = TAG
        )
    }

    private fun showLoader() {
        binding.cardInfoBar.gone()
        binding.parentLayoutLoader.visible()
    }

    private fun hideLoader() {
        binding.parentLayoutLoader.gone()
        binding.cardInfoBar.visible()
    }

}