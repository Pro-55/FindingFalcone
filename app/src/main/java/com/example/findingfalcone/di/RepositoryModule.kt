package com.example.findingfalcone.di

import com.example.findingfalcone.data.repository.contract.FindingFalconeRepository
import com.example.findingfalcone.data.repository.impl.FindingFalconeRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideFindingFalconeRepository(): FindingFalconeRepository = FindingFalconeRepositoryImpl()

}