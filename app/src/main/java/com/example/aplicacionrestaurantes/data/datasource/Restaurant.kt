package com.example.aplicacionrestaurantes.data.datasource

import com.example.aplicacionrestaurantes.data.models.Restaurante

class RestaurantDataSource {

    private val restaurantList = mutableListOf<Restaurante>()
    private var nextId = 1  // Empezamos el ID en 1 (o cualquier valor que desees como punto de inicio)

    init {
        // Pre-cargar algunos restaurantes de ejemplo con un ID único
        addRestaurant(Restaurante(nextId++, "Disfrutar", "Desde Barcelona, galardonado con estrellas Michelin, ofrece una experiencia gastronómica de vanguardia con platos creativos.", "disfrutar"))
        addRestaurant(Restaurante(nextId++, "Etxebarri", "Desde Vizcaya, es conocido por su cocina de autor basada en la parrilla.", "vizcaya"))
        addRestaurant(Restaurante(nextId++, "Table by Bruno Verjus", "Desde París, restaurante que fusiona ingredientes locales con técnicas modernas, ofreciendo menús degustación innovadores", "brunoverjus"))
        addRestaurant(Restaurante(nextId++, "DiverXO", "Desde Madrid, el chef David Muñoz crea platos únicos que combinan la cocina asiática y mediterránea.", "driverxo"))
        addRestaurant(Restaurante(nextId++, "Maido", "Desde Lima, Perú, restaurante que fusiona lo mejor de Japón y Perú, dirigido por el chef Mitsuharu Tsumura.", "maido"))
        addRestaurant(Restaurante(nextId++, "Atomix", "Desde Nueva York, ofrece una experiencia gastronómica coreana moderna, con menús degustación innovadores.", "atomix"))
        addRestaurant(Restaurante(nextId++, "Quintonil", "Desde Ciudad de México, dirigido por el chef Jorge Vallejo, destaca por su reinterpretación de la cocina mexicana.", "quintonil"))
        addRestaurant(Restaurante(nextId++, "Alchemist", "Desde Copenhague, combina arte, ciencia y gastronomía en una experiencia multisensorial única.", "alchemist"))
    }

    // Obtener todos los restaurantes
    fun getRestaurants(): List<Restaurante> {
        return restaurantList
    }

    // Agregar un restaurante
    fun addRestaurant(restaurant: Restaurante) {
        // Asignar un id al restaurante
        val newRestaurant = restaurant.copy(id = nextId++)
        restaurantList.add(newRestaurant)
    }

    // Eliminar un restaurante por ID
    fun deleteRestaurant(restaurantId: Int) {
        val index = restaurantList.indexOfFirst { it.id == restaurantId }
        if (index != -1) {
            restaurantList.removeAt(index)
        }
    }

    // Editar un restaurante
    fun editRestaurant(oldRestaurant: Restaurante, newRestaurant: Restaurante) {
        val index = restaurantList.indexOfFirst { it.id == oldRestaurant.id }
        if (index != -1) {
            restaurantList[index] = newRestaurant.copy(id = oldRestaurant.id) // Mantener el mismo id
        }
    }
}

