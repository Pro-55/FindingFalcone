package com.example.findingfalcone.data.repository.contract

import com.example.findingfalcone.model.FalconeStatus
import com.example.findingfalcone.model.Planet
import com.example.findingfalcone.model.PlanetsAndVehicles
import com.example.findingfalcone.model.Resource
import com.example.findingfalcone.model.Vehicle
import kotlinx.coroutines.flow.Flow

interface FindingFalconeRepository {
    fun getToken(): Flow<Resource<Boolean>>

    fun getPlanetsAndVehicles(): Flow<Resource<PlanetsAndVehicles>>

    fun getAvailableVehicles(
        planet: Planet,
        vehicles: List<Vehicle>,
        selectionMap: Map<Planet, Vehicle>
    ): Flow<Resource<Pair<Planet, List<Vehicle>>>>

    fun findFalcone(
        selectionMap: Map<Planet, Vehicle>
    ): Flow<Resource<FalconeStatus>>
}