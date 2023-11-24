package com.example.findingfalcone.model.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FindFalconeRequest(
    @SerialName("token") val token: String,
    @SerialName("planet_names") val planets: List<String>,
    @SerialName("vehicle_names") val vehicles: List<String>
)