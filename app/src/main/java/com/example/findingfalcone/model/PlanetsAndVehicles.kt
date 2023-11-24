package com.example.findingfalcone.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlanetsAndVehicles(
    val planets: List<Planet>,
    val vehicles: List<Vehicle>
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlanetsAndVehicles

        if (planets != other.planets) return false
        if (vehicles != other.vehicles) return false

        return true
    }

    override fun hashCode(): Int {
        var result = planets.hashCode()
        result = 31 * result + vehicles.hashCode()
        return result
    }
}