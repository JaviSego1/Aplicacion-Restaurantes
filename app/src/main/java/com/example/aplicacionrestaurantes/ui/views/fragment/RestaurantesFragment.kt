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
import com.example.aplicacionrestaurantes.data.models.Restaurante
import com.example.aplicacionrestaurantes.databinding.FragmentRestaurantesBinding
import com.example.aplicacionrestaurantes.domain.models.Restaurant
import com.example.aplicacionrestaurantes.ui.adapter.RestauranteAdapter
import com.example.aplicacionrestaurantes.ui.viewmodel.RestaurantViewModel

class RestaurantFragment : Fragment(R.layout.fragment_restaurantes) {

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
        setupLogoutButton()

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = RestauranteAdapter(emptyList(), ::onDeleteRestaurant, ::onEditRestaurant)
        binding.recyclerView.adapter = adapter
    }

    private fun setupAddButton() {
        binding.btnAgregar.setOnClickListener {
            showAddRestaurantDialog()
        }
    }

    private fun setupLogoutButton() {
        binding.btnVolver.setOnClickListener {
            // Implementar lógica para cerrar sesión
            Toast.makeText(requireContext(), "Cerrando sesión...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onDeleteRestaurant(restaurante: Restaurante) {
        val restaurantName = restaurante.titulo  // Usamos 'titulo' para obtener el nombre
        restaurantViewModel.deleteRestaurant(restaurante.id)  // Usamos el 'id' para eliminar
        Toast.makeText(
            requireContext(),
            "Restaurante eliminado: $restaurantName",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun onEditRestaurant(restaurant: Restaurant) {
        val dialog = RestaurantDialogFragmentCU().newInstance(restaurant)
        dialog.onUpdate = { updatedRestaurant ->
            restaurantViewModel.editRestaurant(restaurant, updatedRestaurant)
        }
        dialog.show(parentFragmentManager, "EditRestaurantDialogFragment")
    }

    private fun showAddRestaurantDialog() {
        val dialog = RestaurantDialogFragmentCU()
        dialog.onUpdate = { restaurant ->
            restaurantViewModel.addRestaurant(restaurant)
        }
        dialog.show(parentFragmentManager, "AddRestaurantDialogFg")
    }

    private fun observeViewModel() {
        restaurantViewModel.restaurantLiveData.observe(viewLifecycleOwner) { restaurants ->
            if (restaurants.isNotEmpty()) {
                updateRestaurantList(restaurants)
            }
        }
        loadData()
    }

    private fun loadData() {
        restaurantViewModel.getRestaurants()
    }

    private fun updateRestaurantList(restaurants: List<Restaurant>) {
        // Convertimos la lista de Restaurant a Restaurante
        val restaurantesList = restaurants.map { restaurant ->
            Restaurante(
                titulo = restaurant.titulo,
                descripcion = restaurant.descripcion,
                imagen = restaurant.imagen
            )
        }

        if (adapter.itemCount != restaurantesList.size) {
            adapter.submitList(restaurantesList)  // Actualiza la lista utilizando submitList

            if (!isFirstLoad && restaurantesList.size > adapter.itemCount) {
                binding.recyclerView.post {
                    binding.recyclerView.smoothScrollToPosition(restaurantesList.size - 1)
                }
            }

            isFirstLoad = false
        }
    }
}
