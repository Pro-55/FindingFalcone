package com.example.findingfalcone.ui.selection.vehicles_sheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.example.findingfalcone.R
import com.example.findingfalcone.databinding.FragmentVehiclesSheetBinding
import com.example.findingfalcone.model.Vehicle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class VehiclesSheetFragment : BottomSheetDialogFragment() {

    companion object {
        private val KEY_OFFICE_LOCATIONS = "key_vehicles"

        @JvmStatic
        private fun newInstance(
            vehicles: List<Vehicle>,
            listener: Listener?
        ) = VehiclesSheetFragment().apply {
            arguments = Bundle().apply {
                putParcelableArray(KEY_OFFICE_LOCATIONS, vehicles.toTypedArray())
            }
            this.listener = listener
        }

        @JvmStatic
        fun showDialog(
            vehicles: List<Vehicle>,
            listener: Listener?,
            fm: FragmentManager,
            tag: String
        ) {
            if (!fm.isStateSaved) {
                newInstance(vehicles, listener).apply {
                    isCancelable = false
                    show(fm, tag)
                }
            }
        }
    }

    // Global
    private val TAG = VehiclesSheetFragment::class.java.simpleName
    private lateinit var binding: FragmentVehiclesSheetBinding
    private val vehicles = mutableListOf<Vehicle>()
    private var listener: Listener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setStyle(STYLE_NORMAL, R.style.RoundedBottomSheetDialogStyle)
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            vehicles.clear()
            val data = args.getParcelableArray(KEY_OFFICE_LOCATIONS)!!.toList() as List<Vehicle>
            vehicles.addAll(data)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return (super.onCreateDialog(savedInstanceState) as BottomSheetDialog).apply {
            setOnShowListener {
                (it as? BottomSheetDialog)?.behavior?.apply {
                    state = BottomSheetBehavior.STATE_EXPANDED
                    skipCollapsed = true
                    isDraggable = false
                    peekHeight = 0
                    setCanceledOnTouchOutside(true)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_vehicles_sheet,
            container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.recyclerVehicles.adapter = VehiclesAdapter(
            data = vehicles,
            listener = object : VehiclesAdapter.Listener {
                override fun onVehicleSelected(vehicle: Vehicle) {
                    listener?.onVehicleSelected(vehicle = vehicle)
                    dismiss()
                }
            }
        )
    }

    interface Listener {
        fun onVehicleSelected(vehicle: Vehicle)
    }
}