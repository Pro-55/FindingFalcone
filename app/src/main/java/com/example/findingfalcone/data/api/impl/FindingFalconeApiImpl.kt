package com.example.findingfalcone.data.api.impl

import com.example.findingfalcone.BuildConfig
import com.example.findingfalcone.data.api.contract.FindingFalconeApi
import com.example.findingfalcone.data.repository.impl.FindingFalconeRepositoryImpl
import com.example.findingfalcone.model.network.FindFalconeRequest
import com.example.findingfalcone.model.network.FindFalconeResponse
import com.example.findingfalcone.model.network.NetworkPlanet
import com.example.findingfalcone.model.network.NetworkToken
import com.example.findingfalcone.model.network.NetworkVehicle
import com.example.findingfalcone.model.network.Response
import com.example.findingfalcone.util.wrappers.safeCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.client.utils.EmptyContent.contentType
import io.ktor.http.ContentType
import io.ktor.http.contentType

class FindingFalconeApiImpl(
    private val client: HttpClient
) : FindingFalconeApi {

    // Global
    private val TAG = FindingFalconeRepositoryImpl::class.java.simpleName

    override suspend fun fetchToken(): Response<NetworkToken> = safeCall {
        val response = client.post<NetworkToken> {
            url("${BuildConfig.BaseUrl}/token")
        }
        Response.Success(response)
    }

    override suspend fun fetchPlanets(): Response<List<NetworkPlanet>> = safeCall {
        val response = client.get<List<NetworkPlanet>> {
            url("${BuildConfig.BaseUrl}/planets")
        }
        Response.Success(response)
    }

    override suspend fun fetchVehicles(): Response<List<NetworkVehicle>> = safeCall {
        val response = client.get<List<NetworkVehicle>> {
            url("${BuildConfig.BaseUrl}/vehicles")
        }
        Response.Success(response)
    }

    override suspend fun findFalcone(
        request: FindFalconeRequest
    ): Response<FindFalconeResponse> = safeCall {
        val response = client.post<FindFalconeResponse> {
            url("${BuildConfig.BaseUrl}/find")
            contentType(ContentType.Application.Json)
            body = request
        }
        Response.Success(response)
    }
}