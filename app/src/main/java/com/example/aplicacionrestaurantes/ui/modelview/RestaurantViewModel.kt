package com.example.aplicacionrestaurantes.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacionrestaurantes.domain.models.Restaurant
import com.example.aplicacionrestaurantes.domain.usercase.AddRestaurantUseCase
import com.example.aplicacionrestaurantes.domain.usercase.DeleteRestaurantUseCase
import com.example.aplicacionrestaurantes.domain.usercase.EditRestaurantUseCase
import com.example.aplicacionrestaurantes.domain.usercase.GetRestaurantsUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class RestaurantViewModel @Inject constructor(
    private val getRestaurantsUseCase: GetRestaurantsUseCase,
    private val addRestaurantUseCase: AddRestaurantUseCase,
    private val deleteRestaurantUseCase: DeleteRestaurantUseCase,
    private val editRestaurantUseCase: EditRestaurantUseCase
) : ViewModel() {

    // LiveData para manejar los estados
    val restaurantLiveData = MutableLiveData<List<Restaurant>>()
    val progressBarLiveData = MutableLiveData<Boolean>()
    val errorLiveData = MutableLiveData<String>()

    // Función para obtener todos los restaurantes
    fun getRestaurants() {
        progressBarLiveData.value = true
        viewModelScope.launch {
            try {
                val data = getRestaurantsUseCase()
                restaurantLiveData.value = data
            } catch (e: Exception) {
                errorLiveData.value = e.message ?: "Error desconocido"
            } finally {
                progressBarLiveData.value = false
            }
        }
    }

    // Función para agregar un restaurante
    fun addRestaurant(restaurant: Restaurant) {
        viewModelScope.launch {
            try {
                addRestaurantUseCase(restaurant)
                getRestaurants() // Actualiza la lista después de agregar
            } catch (e: Exception) {
                errorLiveData.value = e.message ?: "Error desconocido"
            }
        }
    }

    // Función para eliminar un restaurante
    fun deleteRestaurant(restaurantId: Int) {
        viewModelScope.launch {
            try {
                deleteRestaurantUseCase(restaurantId)
                getRestaurants()
            } catch (e: Exception) {
                errorLiveData.value = e.message ?: "Error desconocido"
            }
        }
    }

    // Función para editar un restaurante
    fun editRestaurant(oldRestaurant: Restaurant, newRestaurant: Restaurant) {
        viewModelScope.launch {
            try {
                editRestaurantUseCase(oldRestaurant, newRestaurant)
                getRestaurants() // Actualiza la lista después de editar
            } catch (e: Exception) {
                errorLiveData.value = e.message ?: "Error desconocido"
            }
        }
    }
}

