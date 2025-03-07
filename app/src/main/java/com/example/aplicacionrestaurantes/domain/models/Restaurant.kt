package com.example.aplicacionrestaurantes.domain.models

import java.io.Serializable

data class Restaurant(
    var id: Int,
    var titulo: String,
    var descripcion: String,
    val imagen: String
) : Serializable