package com.example.findingfalcone.ui.selection.vehicles_sheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.findingfalcone.R
import com.example.findingfalcone.databinding.LayoutVehiclesAdapterItemBinding
import com.example.findingfalcone.model.Vehicle

class VehiclesAdapter(
    private val data: List<Vehicle>,
    private val listener: Listener
) : RecyclerView.Adapter<VehiclesAdapter.ViewHolder>() {

    // Global
    private val TAG = VehiclesAdapter::class.java.simpleName

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder = ViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.layout_vehicles_adapter_item,
            parent, false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(
        private val binding: LayoutVehiclesAdapterItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(vehicle: Vehicle) = with(binding) {
            txtVehicleName.text = vehicle.name

            txtVehicleDetails.text = root.resources.getString(
                R.string.label_vehicle_details,
                vehicle.speed,
                vehicle.maxDistance
            )

            root.setOnClickListener { listener.onVehicleSelected(vehicle = vehicle) }
        }
    }

    interface Listener {
        fun onVehicleSelected(vehicle: Vehicle)
    }
}