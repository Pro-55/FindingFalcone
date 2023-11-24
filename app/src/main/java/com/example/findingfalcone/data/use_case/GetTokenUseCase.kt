package com.example.findingfalcone.data.use_case

import com.example.findingfalcone.data.repository.contract.FindingFalconeRepository
import com.example.findingfalcone.model.Resource
import kotlinx.coroutines.flow.Flow

class GetTokenUseCase(
    private val repository: FindingFalconeRepository
) {
    operator fun invoke(): Flow<Resource<Boolean>> = repository.getToken()
}