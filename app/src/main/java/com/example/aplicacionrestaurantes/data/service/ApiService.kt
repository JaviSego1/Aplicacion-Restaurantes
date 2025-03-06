package com.example.aplicacionrestaurantes.data.service

import com.example.aplicacionrestaurantes.data.models.AuthResponse
import com.example.aplicacionrestaurantes.data.models.Restaurante
import com.example.aplicacionrestaurantes.data.models.Usuario
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("restaurantes")
    fun getRestaurantes(): Response<List<Restaurante>>

    @POST("restaurantes/add")
    fun addRestaurante(@Body review: Restaurante): Response<Unit>

    @PATCH("restaurantes/edit/{id}")
    fun editRestaurante(@Path("id") id: Int, @Body review: Restaurante): Response<Unit>

    @DELETE("restaurantes/del/{id}")
    fun deleteRestaurante(@Path("id") id: Int): Response<Unit>

    @POST("usuarios/login")
    fun login(@Body loginRequest: LoginRequest): Call<AuthResponse>

    @POST("usuarios/registro")
    fun register(@Body user: Usuario): Call<Void>
}

data class LoginRequest(
    val name: String,
    val email: String,
    val password: String
)


