package com.example.findingfalcone.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Vehicle(
    val name: String,
    val count: Int,
    val maxDistance: Int,
    val speed: Int
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Vehicle

        if (name != other.name) return false
        if (count != other.count) return false
        if (maxDistance != other.maxDistance) return false
        if (speed != other.speed) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + count
        result = 31 * result + maxDistance
        result = 31 * result + speed
        return result
    }
}