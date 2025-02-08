package com.example.aplicacionrestaurantes.data.datasource

import com.example.aplicacionrestaurantes.data.models.Restaurante

class RestaurantDataSource {

    private val restaurantList = mutableListOf<Restaurante>(
        Restaurante(
            "Disfrutar",
            "Desde Barcelona, galardonado con estrellas Michelin, ofrece una experiencia gastronómica " +
                    "de vanguardia con platos creativos.",
            "disfrutar"
        ),
        Restaurante(
            "Etxebarri",
            "Desde Vizcaya, es conocido por su cocina de autor basada en la parrilla.",
            "vizcaya"
        ),
        Restaurante(
            "Table by Bruno Verjus",
            "Desde París, restaurante que fusiona ingredientes locales con técnicas modernas, ofreciendo menús degustación innovadores",
            "brunoverjus"
        ),
        Restaurante(
            "DiverXO",
            "Desde Madrid, el chef David Muñoz crea platos únicos que combinan la cocina asiática y mediterránea. ",
            "driverxo"
        ),
        Restaurante(
            "Maido",
            "Desde Lima, Perú, restaurante que fusiona lo mejor de Japón y Perú, dirigido por el chef Mitsuharu Tsumura.",
            "maido"
        ),
        Restaurante(
            "Atomix",
            "Desde Nueva York, ofrece una experiencia gastronómica coreana moderna, con menús degustación innovadores.",
            "atomix"
        ),
        Restaurante(
            "Quintonil",
            "Desde Ciudad de México, dirigido por el chef Jorge Vallejo, destaca por su reinterpretación de la cocina mexicana.",
            "quintonil"
        ),
        Restaurante(
            "Alchemist",
            "Desde Copenhague, combina arte, ciencia y gastronomía en una experiencia multisensorial única.",
            "alchemist"
        )
    )

    fun getRestaurants(): List<Restaurante> {
        return restaurantList
    }

    fun addRestaurant(restaurant: Restaurante) {
        restaurantList.add(restaurant)
    }

    fun deleteRestaurant(restaurantId: Int) {
        restaurantList.removeAt(restaurantId)
    }

    fun editRestaurant(oldRestaurant: Restaurante, newRestaurant: Restaurante){
        val index = restaurantList.indexOfFirst { it.titulo == newRestaurant.titulo }
        if (index != -1)
            restaurantList[index] = newRestaurant
    }
}
