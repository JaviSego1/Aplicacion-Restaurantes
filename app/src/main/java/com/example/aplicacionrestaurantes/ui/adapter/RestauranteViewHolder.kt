package com.example.aplicacionrestaurantes.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aplicacionrestaurantes.databinding.ItemRestauranteBinding
import com.example.aplicacionrestaurantes.data.models.Restaurante

class RestauranteViewHolder(private val binding: ItemRestauranteBinding) : RecyclerView.ViewHolder(binding.root) {

    // Método que renderiza los datos del restaurante
    fun bind(restaurante: Restaurante, onEditClick: (Restaurante) -> Unit, onEliminarClick: (Restaurante) -> Unit) {
        binding.titulo.text = restaurante.titulo
        binding.descripcion.text = restaurante.descripcion

        // Cargar imagen usando Glide desde los recursos locales (drawable) o la imagen seleccionada/capturada
        if (restaurante.imagen.startsWith("http")) {
            Glide.with(itemView.context)
                .load(restaurante.imagen) // Usamos URL de la imagen
                .centerCrop()
                .into(binding.imagenRestaurante)
        } else {
            val resId = binding.imagenRestaurante.context.resources.getIdentifier(
                restaurante.imagen, "drawable", binding.imagenRestaurante.context.packageName)
            Glide.with(itemView.context)
                .load(resId) // Usamos el identificador del recurso drawable
                .centerCrop()
                .into(binding.imagenRestaurante)
        }

        // Configurar los clics para editar y eliminar
        binding.imagenEliminar.setOnClickListener {
            onEliminarClick(restaurante)
        }

        binding.imagenEditar.setOnClickListener {
            onEditClick(restaurante)
        }
    }
}

