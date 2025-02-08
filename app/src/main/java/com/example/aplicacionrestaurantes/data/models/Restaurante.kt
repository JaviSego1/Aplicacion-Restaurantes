package com.example.aplicacionrestaurantes.data.models

import java.io.Serializable

data class Restaurante(
    var titulo: String,
    var descripcion: String,
    val imagen: String
) : Serializable