package com.example.findingfalcone.model.network

import com.example.findingfalcone.model.Planet
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkPlanet(
    @SerialName("name") val name: String,
    @SerialName("distance") val distance: Int
)

fun List<NetworkPlanet?>.parse(): List<Planet> = mapNotNull { it?.parse() }

fun NetworkPlanet.parse(): Planet = Planet(
    name = name,
    distance = distance
)