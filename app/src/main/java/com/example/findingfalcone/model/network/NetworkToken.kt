package com.example.findingfalcone.model.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkToken(
    @SerialName("token") val token: String
)