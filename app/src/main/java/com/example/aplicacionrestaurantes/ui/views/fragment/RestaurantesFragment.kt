package com.example.aplicacionrestaurantes.ui.views.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplicacionrestaurantes.R
import com.example.aplicacionrestaurantes.databinding.FragmentRestaurantesBinding
import com.example.aplicacionrestaurantes.domain.models.Restaurant
import com.example.aplicacionrestaurantes.ui.adapter.RestauranteAdapter
import com.example.aplicacionrestaurantes.ui.viewmodel.RestaurantViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantesFragment : Fragment(R.layout.fragment_restaurantes) {

    private lateinit var binding: FragmentRestaurantesBinding
    private lateinit var adapter: RestauranteAdapter
    private val restaurantViewModel: RestaurantViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRestaurantesBinding.inflate(inflater, container, false)

        setupRecyclerView()
        observeViewModel()
        setupAddButton()

        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = RestauranteAdapter(::onDeleteRestaurant, ::onEditRestaurant)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupAddButton() {
        binding.btnAgregar.setOnClickListener {
            showAddRestaurantDialog()
        }
    }

    private fun onDeleteRestaurant(restaurant: Restaurant) {
        restaurantViewModel.deleteRestaurant(restaurant.id)
        Toast.makeText(
            requireContext(),
            "Restaurante eliminado: ${restaurant.titulo}",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun onEditRestaurant(restaurant: Restaurant) {
        val dialog = RestaurantDialogFragmentCU.newInstance(restaurant)
        dialog.onUpdate = { updatedRestaurant ->
            if (updatedRestaurant != null) {
                restaurantViewModel.editRestaurant(restaurant.id, updatedRestaurant)
            }
            refreshList()
        }
        dialog.show(parentFragmentManager, "EditRestaurantDialog")
    }

    private fun showAddRestaurantDialog() {
        val dialog = RestaurantDialogFragmentCU()
        dialog.onUpdate = { newRestaurant ->
            if (newRestaurant != null) {
                restaurantViewModel.addRestaurant(newRestaurant)
            }
            refreshList()
        }
        dialog.show(parentFragmentManager, "AddRestaurantDialog")
    }

    private fun observeViewModel() {
        restaurantViewModel.restaurantLiveData.observe(viewLifecycleOwner) { restaurants ->
            updateRestaurantList(restaurants)
        }
    }

    private fun updateRestaurantList(restaurants: List<Restaurant>) {
        adapter.submitList(restaurants)
    }

    private fun refreshList() {
        restaurantViewModel.getRestaurants()  // Se asume que el ViewModel tiene este método
    }
}