package com.example.aplicacionrestaurantes.data.repository

import com.example.aplicacionrestaurantes.data.models.Restaurante
import com.example.aplicacionrestaurantes.data.service.RestaurantService
import com.example.aplicacionrestaurantes.domain.models.Restaurant
import com.example.aplicacionrestaurantes.domain.repository.RepositoryInterface

class RestaurantInMemoryRepository(
    private val restaurantService: RestaurantService
) : RepositoryInterface<Restaurant> {

    override suspend fun getAll(): List<Restaurant> {
        val restaurants = restaurantService.getRestaurants()
        return restaurants.map { restaurant ->
            Restaurant(
                restaurant.titulo,
                restaurant.descripcion,
                restaurant.imagen
            )
        }
    }

    override suspend fun add(o: Restaurant) {
        val newRestaurant = Restaurante(
            o.titulo,
            o.descripcion,
            o.imagen
        )
        restaurantService.addRestaurant(newRestaurant)
    }

    override suspend fun delete(position: Int): Boolean {
        restaurantService.deleteRestaurant(position)
        return true;
    }

    override suspend fun edit(oldRestaurant: Restaurant, newRestaurant: Restaurant) {
        val oldRestaurant = Restaurante(
            oldRestaurant.titulo, oldRestaurant.descripcion, oldRestaurant.imagen
        )
        val newRestaurant = Restaurante(
            newRestaurant.titulo, newRestaurant.descripcion, newRestaurant.imagen
        )
        restaurantService.editRestaurant(oldRestaurant, newRestaurant)
    }
}
