package com.example.aplicacionrestaurantes.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aplicacionrestaurantes.R
import com.example.aplicacionrestaurantes.databinding.ItemRestauranteBinding
import com.example.aplicacionrestaurantes.domain.models.Restaurant

class RestauranteViewHolder(private val binding: ItemRestauranteBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(restaurant: Restaurant, onEditClick: (Restaurant) -> Unit, onEliminarClick: (Restaurant) -> Unit) {
        binding.titulo.text = restaurant.titulo
        binding.descripcion.text = restaurant.descripcion

        Glide.with(itemView.context)
            .load("http://10.0.2.2:8081/images/${restaurant.imagen}")
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(binding.imagenRestaurante)

        binding.imagenEliminar.setOnClickListener {
            onEliminarClick(restaurant)
        }

        binding.imagenEditar.setOnClickListener {
            onEditClick(restaurant)
        }
    }
}