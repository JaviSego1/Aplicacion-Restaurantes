package com.example.aplicacionrestaurantes.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aplicacionrestaurantes.domain.models.Restaurant
import com.example.aplicacionrestaurantes.databinding.ItemRestauranteBinding

class RestauranteAdapter(
    private val onEditClick: (Restaurant) -> Unit,
    private val onEliminarClick: (Restaurant) -> Unit
) : RecyclerView.Adapter<RestauranteViewHolder>() {

    private var restaurants: List<Restaurant> = emptyList()

    // Actualiza la lista de restaurantes
    fun submitList(newList: List<Restaurant>) {
        restaurants = newList
        notifyDataSetChanged()
    }

    fun getRestaurantes(): List<Restaurant> = restaurants

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestauranteViewHolder {
        val binding = ItemRestauranteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RestauranteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RestauranteViewHolder, position: Int) {
        val restaurant = restaurants[position]
        holder.bind(restaurant, onEliminarClick, onEditClick)
    }

    override fun getItemCount(): Int = restaurants.size
}
