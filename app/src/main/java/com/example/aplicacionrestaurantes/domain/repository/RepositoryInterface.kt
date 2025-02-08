package com.example.aplicacionrestaurantes.domain.repository

interface RepositoryInterface<T> {
    suspend fun getAll() : List<T>
    suspend fun delete(id : Int): Boolean
    suspend fun add(o: T)
    suspend fun edit(oldRestaurant: T, newRestaurant: T)

}