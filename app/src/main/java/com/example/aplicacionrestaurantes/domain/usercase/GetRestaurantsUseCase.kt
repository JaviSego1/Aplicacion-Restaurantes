package com.example.aplicacionrestaurantes.domain.usercase

import com.example.aplicacionrestaurantes.domain.models.Restaurant
import com.example.aplicacionrestaurantes.domain.repository.RepositoryInterface
import javax.inject.Inject

class GetRestaurantsUseCase @Inject constructor(private val restaurantRepository: RepositoryInterface<Restaurant>) {
    suspend operator fun invoke(): List<Restaurant> {
        return restaurantRepository.getAll()
    }
}