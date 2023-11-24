package com.example.findingfalcone.model.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FindFalconeResponse(
    @SerialName("planet_name") val planet: String? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("error") val error: String? = null
)