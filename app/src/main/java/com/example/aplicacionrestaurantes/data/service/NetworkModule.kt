package com.example.aplicacionrestaurantes.data.service

import com.example.aplicacionrestaurantes.data.service.RestaurantService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRestaurantService(): RestaurantService {
        return RestaurantService() // Proporciona una instancia de RestaurantService
    }
}