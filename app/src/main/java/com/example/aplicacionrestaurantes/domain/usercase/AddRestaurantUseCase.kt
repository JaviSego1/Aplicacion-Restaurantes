package com.example.aplicacionrestaurantes.domain.usercase

import com.example.aplicacionrestaurantes.domain.models.Restaurant
import com.example.aplicacionrestaurantes.domain.repository.RepositoryInterface
import javax.inject.Inject

class AddRestaurantUseCase @Inject constructor(private val repositoryInterface: RepositoryInterface) {
    suspend fun execute(restaurant: Restaurant): Boolean {
        return repositoryInterface.add(restaurant)  // No necesitamos un valor de retorno
    }
}
