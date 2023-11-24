package com.example.findingfalcone.data.repository.impl

import android.content.SharedPreferences
import com.example.findingfalcone.R
import com.example.findingfalcone.data.api.contract.FindingFalconeApi
import com.example.findingfalcone.data.repository.contract.FindingFalconeRepository
import com.example.findingfalcone.model.FalconeStatus
import com.example.findingfalcone.model.Planet
import com.example.findingfalcone.model.PlanetsAndVehicles
import com.example.findingfalcone.model.Resource
import com.example.findingfalcone.model.UIMessage
import com.example.findingfalcone.model.Vehicle
import com.example.findingfalcone.model.network.FindFalconeRequest
import com.example.findingfalcone.model.network.Response
import com.example.findingfalcone.model.network.parse
import com.example.findingfalcone.util.Constants
import com.example.findingfalcone.util.wrappers.resourceFlow
import kotlinx.coroutines.flow.Flow

class FindingFalconeRepositoryImpl(
    private val api: FindingFalconeApi,
    private val sp: SharedPreferences
) : FindingFalconeRepository {

    // Global
    private val TAG = FindingFalconeRepositoryImpl::class.java.simpleName

    override fun getToken(): Flow<Resource<Boolean>> = resourceFlow {
        when (val response = api.fetchToken()) {
            is Response.Success -> {
                sp.edit()
                    .putString(Constants.KEY_API_TOKEN, response.data.token)
                    .apply()
                emit(Resource.Success(data = true))
            }
            is Response.InvalidPathException -> emit(Resource.Error(msg = response.msg))
            is Response.InvalidRequestException -> emit(Resource.Error(msg = response.msg))
            is Response.ServerException -> emit(Resource.Error(msg = response.msg))
            is Response.UnknownHostException -> emit(Resource.Error(msg = response.msg))
            is Response.UnknownException -> emit(Resource.Error(msg = response.msg))
        }
    }

    override fun getPlanetsAndVehicles(): Flow<Resource<PlanetsAndVehicles>> = resourceFlow {
        val planetsResponse = api.fetchPlanets()
        if (planetsResponse !is Response.Success) {
            when (planetsResponse) {
                is Response.InvalidPathException -> emit(Resource.Error(msg = planetsResponse.msg))
                is Response.InvalidRequestException -> emit(Resource.Error(msg = planetsResponse.msg))
                is Response.ServerException -> emit(Resource.Error(msg = planetsResponse.msg))
                is Response.UnknownHostException -> emit(Resource.Error(msg = planetsResponse.msg))
                is Response.UnknownException -> emit(Resource.Error(msg = planetsResponse.msg))
                else -> Unit
            }
            return@resourceFlow
        }
        val planets = planetsResponse.data
            .parse()
        if (planets.isEmpty()) {
            emit(Resource.Error(msg = UIMessage.StringResource(resId = R.string.error_unknown)))
            return@resourceFlow
        }

        val vehiclesResponse = api.fetchVehicles()
        if (vehiclesResponse !is Response.Success) {
            when (vehiclesResponse) {
                is Response.InvalidPathException -> emit(Resource.Error(msg = vehiclesResponse.msg))
                is Response.InvalidRequestException -> emit(Resource.Error(msg = vehiclesResponse.msg))
                is Response.ServerException -> emit(Resource.Error(msg = vehiclesResponse.msg))
                is Response.UnknownHostException -> emit(Resource.Error(msg = vehiclesResponse.msg))
                is Response.UnknownException -> emit(Resource.Error(msg = vehiclesResponse.msg))
                else -> Unit
            }
            return@resourceFlow
        }
        val vehicles = vehiclesResponse.data
            .parse()
        if (vehicles.isEmpty()) {
            emit(Resource.Error(msg = UIMessage.StringResource(resId = R.string.error_unknown)))
            return@resourceFlow
        }

        emit(
            Resource.Success(
                data = PlanetsAndVehicles(
                    planets = planets,
                    vehicles = vehicles
                )
            )
        )
    }

    override fun getAvailableVehicles(
        planet: Planet,
        vehicles: List<Vehicle>,
        selectionMap: Map<Planet, Vehicle>
    ): Flow<Resource<Pair<Planet, List<Vehicle>>>> = resourceFlow {
        val data = vehicles.filter { vehicle ->
            vehicle.count > selectionMap.values.count { vehicle == it }
                    && vehicle.maxDistance >= planet.distance
        }

        emit(Resource.Success(data = Pair(planet, data)))
    }

    override fun findFalcone(
        selectionMap: Map<Planet, Vehicle>
    ): Flow<Resource<FalconeStatus>> = resourceFlow {
        if (selectionMap.isEmpty()) {
            emit(Resource.Error(msg = UIMessage.StringResource(resId = R.string.error_invalid_request)))
            return@resourceFlow
        }

        val request = FindFalconeRequest(
            token = sp.getString(Constants.KEY_API_TOKEN, null) ?: "",
            planets = selectionMap.keys.map { it.name },
            vehicles = selectionMap.values.map { it.name }
        )
        when (val response = api.findFalcone(request = request)) {
            is Response.Success -> {
                val responseData = response.data
                val status = responseData.status ?: ""
                val data = if (status.equals(Constants.STATUS_SUCCESS, true)) {
                    val planet = responseData.planet
                    if (planet.isNullOrEmpty()) {
                        FalconeStatus.Failure
                    } else {
                        FalconeStatus.Success(
                            planet = planet,
                            time = getTime(selectionMap)
                        )
                    }
                } else if (status.equals(Constants.STATUS_FAILURE, true)) {
                    FalconeStatus.Failure
                } else {
                    FalconeStatus.InvalidToken
                }
                emit(Resource.Success(data = data))
            }
            is Response.InvalidPathException -> emit(Resource.Error(msg = response.msg))
            is Response.InvalidRequestException -> emit(Resource.Error(msg = response.msg))
            is Response.ServerException -> emit(Resource.Error(msg = response.msg))
            is Response.UnknownHostException -> emit(Resource.Error(msg = response.msg))
            is Response.UnknownException -> emit(Resource.Error(msg = response.msg))
        }
    }

    private fun getTime(selectionMap: Map<Planet, Vehicle>): Int {
        var estimatedTime = 0
        for ((planet, vehicle) in selectionMap) {
            estimatedTime += planet.distance / vehicle.speed
        }
        return estimatedTime
    }
}