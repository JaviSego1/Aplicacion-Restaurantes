package com.example.aplicacionrestaurantes.domain.usercase

import com.example.aplicacionrestaurantes.data.repository.RestaurantInMemoryRepository
import javax.inject.Inject

class DeleteRestaurantUseCase @Inject constructor(private val repository: RestaurantInMemoryRepository) {
    suspend operator fun invoke(restaurantId: Int) {
        repository.delete(restaurantId)
    }
}