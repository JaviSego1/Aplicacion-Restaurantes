package com.example.aplicacionrestaurantes.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aplicacionrestaurantes.data.models.Restaurante
import com.example.aplicacionrestaurantes.databinding.ItemRestauranteBinding

class RestauranteAdapter(
    private var restaurantes: List<Restaurante> = listOf(),
    private val onEditClick: (Restaurante) -> Unit,
    private val onEliminarClick: (Restaurante) -> Unit
) : RecyclerView.Adapter<RestauranteViewHolder>() {

    // Actualiza la lista de restaurantes
    fun submitList(newList: List<Restaurante>) {
        restaurantes = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestauranteViewHolder {
        val binding = ItemRestauranteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RestauranteViewHolder(binding) // Pasamos el binding
    }

    override fun onBindViewHolder(holder: RestauranteViewHolder, position: Int) {
        val restaurante = restaurantes[position]
        holder.bind(restaurante, onEditClick, onEliminarClick)
    }

    override fun getItemCount(): Int = restaurantes.size
}
