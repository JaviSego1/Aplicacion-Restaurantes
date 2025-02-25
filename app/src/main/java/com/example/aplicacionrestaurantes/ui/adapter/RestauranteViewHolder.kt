package com.example.aplicacionrestaurantes.ui.adapter

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.recyclerview.widget.RecyclerView
import com.example.aplicacionrestaurantes.R
import com.example.aplicacionrestaurantes.databinding.ItemRestauranteBinding
import com.example.aplicacionrestaurantes.data.models.Restaurante

class RestauranteViewHolder(private val binding: ItemRestauranteBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(restaurante: Restaurante, onEditClick: (Restaurante) -> Unit, onEliminarClick: (Restaurante) -> Unit) {
        binding.titulo.text = restaurante.titulo
        binding.descripcion.text = restaurante.descripcion

        // Verificar si la imagen es un string en Base64
        if (restaurante.imagen.startsWith("data:image")) {
            val imageBytes = Base64.decode(restaurante.imagen.split(",")[1], Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            binding.imagenRestaurante.setImageBitmap(bitmap)
        } else {
            // Si es una ruta de archivo o recurso
            val resId = binding.imagenRestaurante.context.resources.getIdentifier(
                restaurante.imagen, "drawable", binding.imagenRestaurante.context.packageName
            )
            if (resId != 0) {
                binding.imagenRestaurante.setImageResource(resId)
            } else {
                binding.imagenRestaurante.setImageResource(R.drawable.ic_launcher_background)
            }
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