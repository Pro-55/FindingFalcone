package com.example.findingfalcone.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SelectionItem(
    val planet: Planet,
    val vehicle: Vehicle? = null,
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SelectionItem

        if (planet != other.planet) return false
        if (vehicle != other.vehicle) return false

        return true
    }

    override fun hashCode(): Int {
        var result = planet.hashCode()
        result = 31 * result + (vehicle?.hashCode() ?: 0)
        return result
    }
}