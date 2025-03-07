package com.example.aplicacionrestaurantes.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacionrestaurantes.data.models.Restaurante
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

    // LiveData para manejar la lista de restaurantes
    private val _restaurantLiveData = MutableLiveData<List<Restaurant>>()
    val restaurantLiveData: LiveData<List<Restaurant>> get() = _restaurantLiveData

    // LiveData para manejar el estado de carga (ProgressBar)
    private val _progressBarLiveData = MutableLiveData<Boolean>()
    val progressBarLiveData: LiveData<Boolean> get() = _progressBarLiveData

    // LiveData para manejar errores
    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData

    init {
        getRestaurants()
    }

    // Función para obtener los restaurantes
    fun getRestaurants() {
        _progressBarLiveData.value = true  // Muestra el ProgressBar
        viewModelScope.launch {
            try {
                _restaurantLiveData.value = getRestaurantsUseCase.execute() // Actualiza el LiveData
            } catch (e: Exception) {
                _errorLiveData.value = e.message ?: "Error desconocido" // Maneja el error
            } finally {
                _progressBarLiveData.value = false  // Oculta el ProgressBar
            }
        }
    }

    // Función para agregar un restaurante
    fun addRestaurant(restaurant: Restaurant) {
        viewModelScope.launch {
            try {
                addRestaurantUseCase.execute(restaurant) // Llama al caso de uso para agregar el restaurante
                getRestaurants() // Actualiza la lista de restaurantes después de agregar
            } catch (e: Exception) {
                _errorLiveData.value = e.message ?: "Error desconocido" // Maneja el error
            }
        }
    }

    // Función para editar un restaurante
    fun editRestaurant(id: Int, restaurant: Restaurant) {
        viewModelScope.launch {
            try {
                editRestaurantUseCase.execute(id, restaurant) // Llama al caso de uso para editar el restaurante
                getRestaurants() // Actualiza la lista de restaurantes después de editar
            } catch (e: Exception) {
                _errorLiveData.value = e.message ?: "Error desconocido" // Maneja el error
            }
        }
    }

    // Función para eliminar un restaurante
    fun deleteRestaurant(id: Int) {
        viewModelScope.launch {
            try {
                deleteRestaurantUseCase.execute(id) // Llama al caso de uso para eliminar el restaurante
                getRestaurants() // Actualiza la lista de restaurantes después de eliminar
            } catch (e: Exception) {
                _errorLiveData.value = e.message ?: "Error desconocido" // Maneja el error
            }
        }
    }
}
