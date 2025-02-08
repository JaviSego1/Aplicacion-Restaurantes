package com.example.aplicacionrestaurantes.domain.usercase

import com.example.aplicacionrestaurantes.domain.models.Restaurant
import com.example.aplicacionrestaurantes.domain.repository.RepositoryInterface

class GetRestaurantsUseCase(private val restaurantRepository: RepositoryInterface<Restaurant>) {
    suspend operator fun invoke(): List<Restaurant> {
        return restaurantRepository.getAll()
    }
}