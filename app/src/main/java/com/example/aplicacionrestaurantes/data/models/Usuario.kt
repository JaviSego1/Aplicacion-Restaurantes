package com.example.aplicacionrestaurantes.data.models

data class Usuario(
    val name: String,
    val email: String,
    val password: String,
    val token: String? = null
)

data class AuthResponse(
    val token: String
)