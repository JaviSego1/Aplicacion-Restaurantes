package com.example.aplicacionrestaurantes.domain.usercase

import com.example.aplicacionrestaurantes.domain.models.Restaurant
import com.example.aplicacionrestaurantes.domain.repository.RepositoryInterface
import javax.inject.Inject

class EditRestaurantUseCase @Inject constructor(private val repositoryInterface: RepositoryInterface) {
    suspend fun execute(id: Int, restaurant: Restaurant): Boolean {
        return repositoryInterface.edit(id, restaurant)  // No necesitamos un valor de retorno
    }
}
