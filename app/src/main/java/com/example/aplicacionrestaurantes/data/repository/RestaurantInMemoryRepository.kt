package com.example.aplicacionrestaurantes.data.repository

import com.example.aplicacionrestaurantes.data.models.Restaurante as DataRestaurante
import com.example.aplicacionrestaurantes.domain.models.Restaurant as DomainRestaurante
import com.example.aplicacionrestaurantes.data.service.ApiService
import com.example.aplicacionrestaurantes.domain.repository.RepositoryInterface
import javax.inject.Inject

class RestaurantInMemoryRepository @Inject constructor(
    private val apiService: ApiService  // Usamos ApiService para hacer las peticiones
) : RepositoryInterface {

    override suspend fun getAll(): List<DomainRestaurante> {
        // Llamamos al método GET de ApiService para obtener la lista de restaurantes
        val response = apiService.getRestaurantes()
        return if (response.isSuccessful) {
            val dataRestaurantes = response.body() ?: emptyList()
            dataRestaurantes.map { it.toDomainRestaurante() }
        } else {
            emptyList() // Devuelve una lista vacía si no hay respuesta exitosa
        }
    }

    override suspend fun add(restaurante: DomainRestaurante): Boolean {
        // Llamamos al método POST de ApiService para agregar un nuevo restaurante
        val dataRestaurante = restaurante.toDataRestaurante()
        val response = apiService.addRestaurante(dataRestaurante)
        return response.isSuccessful // Devuelve true si la operación fue exitosa
    }

    override suspend fun delete(id: Int): Boolean {
        // Llamamos al método DELETE de ApiService para eliminar un restaurante por ID
        val response = apiService.deleteRestaurante(id)
        return response.isSuccessful // Devuelve true si la operación fue exitosa
    }

    override suspend fun edit(id: Int, restaurante: DomainRestaurante): Boolean {
        // Llamamos al método PATCH de ApiService para editar un restaurante
        val dataRestaurante = restaurante.toDataRestaurante()
        val response = apiService.editRestaurante(id, dataRestaurante)
        return response.isSuccessful // Devuelve true si la operación fue exitosa
    }

    // Función para convertir el modelo de datos a dominio
    private fun DataRestaurante.toDomainRestaurante(): DomainRestaurante {
        return DomainRestaurante(
            id = this.id,
            titulo = this.titulo,
            descripcion = this.descripcion,
            imagen = this.imagen
        )
    }

    // Función para convertir el modelo de dominio a datos
    private fun DomainRestaurante.toDataRestaurante(): DataRestaurante {
        return DataRestaurante(
            id = this.id,
            titulo = this.titulo,
            descripcion = this.descripcion,
            imagen = this.imagen
        )
    }
}
