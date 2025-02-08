package com.example.aplicacionrestaurantes.domain.usercase

import com.example.aplicacionrestaurantes.domain.models.Restaurant
import com.example.aplicacionrestaurantes.domain.repository.RepositoryInterface
import javax.inject.Inject

class AddRestaurantUseCase @Inject constructor(private val repository: RepositoryInterface<Restaurant>) {
    suspend operator fun invoke(restaurant: Restaurant) {
        repository.add(restaurant)
    }
}