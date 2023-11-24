package com.example.findingfalcone.di

import com.example.findingfalcone.data.repository.contract.FindingFalconeRepository
import com.example.findingfalcone.data.use_case.FindFalconeUseCase
import com.example.findingfalcone.data.use_case.GetAvailableVehiclesUseCase
import com.example.findingfalcone.data.use_case.GetPlanetsAndVehiclesUseCase
import com.example.findingfalcone.data.use_case.GetTokenUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @ViewModelScoped
    @Provides
    fun provideGetTokenUseCase(
        repository: FindingFalconeRepository
    ): GetTokenUseCase = GetTokenUseCase(repository)

    @ViewModelScoped
    @Provides
    fun provideGetPlanetsAndVehiclesUseCase(
        repository: FindingFalconeRepository
    ): GetPlanetsAndVehiclesUseCase = GetPlanetsAndVehiclesUseCase(repository)

    @ViewModelScoped
    @Provides
    fun provideFindFalconeUseCase(
        repository: FindingFalconeRepository
    ): FindFalconeUseCase = FindFalconeUseCase(repository)

    @ViewModelScoped
    @Provides
    fun provideGetAvailableUseCase(
        repository: FindingFalconeRepository
    ): GetAvailableVehiclesUseCase = GetAvailableVehiclesUseCase(repository)
}