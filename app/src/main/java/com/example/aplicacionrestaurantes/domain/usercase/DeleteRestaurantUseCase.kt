package com.example.aplicacionrestaurantes.domain.usercase

import com.example.aplicacionrestaurantes.domain.repository.RepositoryInterface
import javax.inject.Inject

class DeleteRestaurantUseCase @Inject constructor(private val repositoryInterface: RepositoryInterface) {
    suspend fun execute(restaurantId: Int): Boolean {
        return repositoryInterface.delete(restaurantId)
    }
}
