package com.example.findingfalcone.data.api.contract

import com.example.findingfalcone.model.network.FindFalconeRequest
import com.example.findingfalcone.model.network.FindFalconeResponse
import com.example.findingfalcone.model.network.NetworkPlanet
import com.example.findingfalcone.model.network.NetworkToken
import com.example.findingfalcone.model.network.NetworkVehicle
import com.example.findingfalcone.model.network.Response

interface FindingFalconeApi {
    suspend fun fetchToken(): Response<NetworkToken>

    suspend fun fetchPlanets(): Response<List<NetworkPlanet>>

    suspend fun fetchVehicles(): Response<List<NetworkVehicle>>

    suspend fun findFalcone(
        request: FindFalconeRequest
    ): Response<FindFalconeResponse>
}