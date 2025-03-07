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
    private var isFirstLoad = true

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
        // Se asume que el adaptador está usando una lista de tipo Restaurante
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
        val position = adapter.getRestaurantes().indexOf(restaurant)
        if (position != -1) {
            restaurantViewModel.deleteRestaurant(restaurant.id)  // Usamos el id del restaurante para eliminarlo
            Toast.makeText(
                requireContext(),
                "Restaurante eliminado: ${restaurant.titulo}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun onEditRestaurant(restaurant: Restaurant) {
        val dialog = RestaurantDialogFragmentCU.newInstance(restaurant)
        dialog.onUpdate = { updatedRestaurante ->
            restaurantViewModel.editRestaurant(restaurant.id, updatedRestaurante) // Editamos el restaurante con su id
        }
        dialog.show(parentFragmentManager, "EditRestaurantDialog")
    }

    private fun showAddRestaurantDialog() {
        val dialog = RestaurantDialogFragmentCU()
        dialog.onUpdate = { restaurante ->
            restaurantViewModel.addRestaurant(restaurante)  // Pasamos el objeto Restaurante al ViewModel
        }
        dialog.show(parentFragmentManager, "AddRestaurantDialog")
    }

    private fun observeViewModel() {
        // Aquí se cambia "restaurants" por "restaurantLiveData" para que coincida con el LiveData en el ViewModel
        restaurantViewModel.restaurantLiveData.observe(viewLifecycleOwner) { restaurants ->
            if (restaurants.isNotEmpty()) {
                updateRestaurantList(restaurants)
            } else {
                Toast.makeText(requireContext(), "No se encontraron restaurantes", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateRestaurantList(restaurants: List<Restaurant>) {
        if (adapter.itemCount != restaurants.size) {
            adapter.submitList(restaurants)  // Actualiza la lista utilizando submitList

            if (!isFirstLoad && restaurants.size > adapter.itemCount) {
                binding.recyclerView.post {
                    binding.recyclerView.smoothScrollToPosition(restaurants.size - 1)
                }
            }

            isFirstLoad = false
        }
    }
}
