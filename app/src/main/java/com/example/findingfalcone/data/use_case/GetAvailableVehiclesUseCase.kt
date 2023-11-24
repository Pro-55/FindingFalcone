package com.example.findingfalcone.data.use_case

import com.example.findingfalcone.data.repository.contract.FindingFalconeRepository
import com.example.findingfalcone.model.Planet
import com.example.findingfalcone.model.Resource
import com.example.findingfalcone.model.Vehicle
import kotlinx.coroutines.flow.Flow

class GetAvailableVehiclesUseCase(
    private val repository: FindingFalconeRepository
) {
    operator fun invoke(
        planet: Planet,
        vehicles: List<Vehicle>,
        selectionMap: Map<Planet, Vehicle>
    ): Flow<Resource<Pair<Planet, List<Vehicle>>>> = repository.getAvailableVehicles(
        planet = planet,
        vehicles = vehicles,
        selectionMap = selectionMap
    )
}