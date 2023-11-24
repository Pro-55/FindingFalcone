package com.example.findingfalcone.di

import com.example.findingfalcone.data.api.contract.FindingFalconeApi
import com.example.findingfalcone.data.api.impl.FindingFalconeApiImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Singleton
    @Provides
    fun provideFindingFalconeApi(
        client: HttpClient
    ): FindingFalconeApi = FindingFalconeApiImpl(client)
}