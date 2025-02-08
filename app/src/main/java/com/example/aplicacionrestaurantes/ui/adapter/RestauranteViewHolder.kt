package com.example.aplicacionrestaurantes.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aplicacionrestaurantes.databinding.ItemRestauranteBinding
import com.example.aplicacionrestaurantes.data.models.Restaurante

class RestauranteViewHolder(private val binding: ItemRestauranteBinding) : RecyclerView.ViewHolder(binding.root) {

    // Método que renderiza los datos del restaurante
    fun bind(restaurante: Restaurante, onEditClick: (Restaurante) -> Unit, onEliminarClick: (Restaurante) -> Unit) {
        binding.titulo.text = restaurante.titulo
        binding.descripcion.text = restaurante.descripcion

        // Cargar imagen usando Glide (en caso de ser una URL o recurso local)
        Glide.with(itemView.context)
            .load(restaurante.imagen) // Aquí puede ser una URL o recurso drawable
            .centerCrop()
            .into(binding.imagenRestaurante)

        // Configurar los clics para editar y eliminar
        binding.imagenEliminar.setOnClickListener {
            onEliminarClick(restaurante)
        }

        binding.imagenEditar.setOnClickListener {
            onEditClick(restaurante)
        }
    }
}
