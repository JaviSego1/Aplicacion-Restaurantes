package com.example.aplicacionrestaurantes.domain.repository

import com.example.aplicacionrestaurantes.domain.models.Restaurant

interface RepositoryInterface {
    suspend fun getAll() : List<Restaurant>
    suspend fun delete(id : Int): Boolean
    suspend fun add(restaurante: Restaurant): Boolean
    suspend fun edit(id: Int, restaurante: Restaurant): Boolean

}