package com.example.findingfalcone.ui.selection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.findingfalcone.R
import com.example.findingfalcone.databinding.LayoutSelectionItemBinding
import com.example.findingfalcone.model.Planet
import com.example.findingfalcone.model.SelectionItem
import com.example.findingfalcone.util.extensions.getColorFromAttr
import com.example.findingfalcone.util.extensions.gone
import com.example.findingfalcone.util.extensions.visible
import com.google.android.material.R as mR

class SelectionAdapter(
    private val listener: Listener
) : ListAdapter<SelectionItem, SelectionAdapter.ViewHolder>(SelectionItemDC()) {

    // Global
    private val TAG = SelectionAdapter::class.java.simpleName
    private var isEnabled = true

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder = ViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.layout_selection_item,
            parent, false
        )
    )

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) = holder.bind(getItem(position))

    fun swapData(data: List<SelectionItem>) = submitList(data.toMutableList())

    fun setIsEnabled(isEnabled: Boolean) {
        this.isEnabled = isEnabled
    }

    inner class ViewHolder(
        private val binding: LayoutSelectionItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SelectionItem) = with(binding) {
            val context = root.context
            val planet = item.planet
            val vehicle = item.vehicle
            val isSelected = vehicle != null

            val planetDetails = context.resources.getString(
                R.string.label_planet_details,
                planet.name,
                planet.distance
            )

            if (isSelected) {
                txtPlanet.apply {
                    text = planetDetails
                    this.isSelected = true
                    setTextColor(context.getColorFromAttr(mR.attr.colorOnPrimary))
                }
                txtVehicle.apply {
                    visible()
                    text = vehicle!!.name
                    this.isSelected = true
                }
            } else {
                txtPlanet.apply {
                    text = planetDetails
                    this.isSelected = false
                    setTextColor(context.getColorFromAttr(mR.attr.colorOnSurface))
                }
                txtVehicle.apply {
                    gone()
                    text = ""
                    this.isSelected = false
                }
            }

            root.setOnClickListener {
                if (isEnabled) {
                    if (isSelected) {
                        listener.onUnselect(planet = planet)
                    } else {
                        listener.onSelect(planet = planet)
                    }
                }
            }
        }
    }

    interface Listener {
        fun onSelect(planet: Planet)
        fun onUnselect(planet: Planet)
    }

    private class SelectionItemDC : DiffUtil.ItemCallback<SelectionItem>() {
        override fun areItemsTheSame(
            oldItem: SelectionItem,
            newItem: SelectionItem
        ): Boolean = oldItem.planet.name == newItem.planet.name

        override fun areContentsTheSame(
            oldItem: SelectionItem,
            newItem: SelectionItem
        ): Boolean = oldItem == newItem
    }
}