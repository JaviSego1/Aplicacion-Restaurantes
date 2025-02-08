package com.example.aplicacionrestaurantes.domain.usercase

import com.example.aplicacionrestaurantes.data.repository.RestaurantInMemoryRepository

class DeleteRestaurantUseCase(private val repository: RestaurantInMemoryRepository) {
    suspend operator fun invoke(restaurantId: Int) {
        repository.delete(restaurantId)
    }
}