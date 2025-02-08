package com.example.aplicacionrestaurantes.data.service

import com.example.aplicacionrestaurantes.data.datasource.RestaurantDataSource
import com.example.aplicacionrestaurantes.data.models.Restaurante

class RestaurantService {

    private val dataSource = RestaurantDataSource()

    suspend fun getRestaurants(): List<Restaurante> {
        return dataSource.getRestaurants()
    }

    suspend fun addRestaurant(restaurant: Restaurante) {
        dataSource.addRestaurant(restaurant)
    }

    suspend fun deleteRestaurant(position: Int) {
        dataSource.deleteRestaurant(position)
    }

    suspend fun editRestaurant(oldRestaurant: Restaurante, newRestaurant: Restaurante){
        dataSource.editRestaurant(oldRestaurant, newRestaurant)
    }
}