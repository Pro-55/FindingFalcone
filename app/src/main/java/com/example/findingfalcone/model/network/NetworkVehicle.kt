package com.example.findingfalcone.model.network

import com.example.findingfalcone.model.Vehicle
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkVehicle(
    @SerialName("name") val name: String,
    @SerialName("total_no") val count: Int,
    @SerialName("max_distance") val maxDistance: Int,
    @SerialName("speed") val speed: Int
)

fun List<NetworkVehicle?>.parse(): List<Vehicle> = mapNotNull { it?.parse() }

fun NetworkVehicle.parse(): Vehicle = Vehicle(
    name = name,
    count = count,
    maxDistance = maxDistance,
    speed = speed
)