package com.example.findingfalcone.data.use_case

import com.example.findingfalcone.data.repository.contract.FindingFalconeRepository
import com.example.findingfalcone.model.FalconeStatus
import com.example.findingfalcone.model.Planet
import com.example.findingfalcone.model.Resource
import com.example.findingfalcone.model.Vehicle
import kotlinx.coroutines.flow.Flow

class FindFalconeUseCase(
    private val repository: FindingFalconeRepository
) {
    operator fun invoke(
        selectionMap: Map<Planet, Vehicle>
    ): Flow<Resource<FalconeStatus>> = repository.findFalcone(
        selectionMap = selectionMap
    )
}