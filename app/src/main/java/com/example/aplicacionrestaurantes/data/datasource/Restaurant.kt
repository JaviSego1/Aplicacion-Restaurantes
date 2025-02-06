package com.example.aplicacionrestaurantes.data.datasource

object Restaurant {

    val resturants : List<Triple<String, String, String>> = listOf(
        Triple("Disfrutar", "Desde Barcelona, galardonado con estrellas Michelin, ofrece una experiencia gastronómica " +
                "de vanguardia con platos creativos.", "imagen"),
        Triple("Etxebarri","Desde Vizcaya, es conocido por su cocina de autor basada en la parrilla.", "imagen"),
        Triple("Table by Bruno Verjus","Desde París, restaurante que fusiona ingredientes locales con técnicas modernas, ofreciendo menús degustación innovadores", "imagen"),
        Triple("DiverXO", "Desde Madrid, el chef David Muñoz crea platos únicos que combinan la cocina asiática y mediterránea. ", "imagen"),
        Triple("Maido","Desde Lima, Perú, restaurante que fusiona lo mejor de Japón y Perú, dirigido por el chef Mitsuharu Tsumura.", "imagen"),
        Triple("Atomix","Desde Nueva York, ofrece una experiencia gastronómica coreana moderna, con menús degustación innovadores.", "imagen"),
        Triple("Quintonil","Desde Ciudad de México, dirigido por el chef Jorge Vallejo, destaca por su reinterpretación de la cocina mexicana.", "imagen"),
        Triple("Alchemist","Desde Copenhague, combina arte, ciencia y gastronomía en una experiencia multisensorial única.", "imagen")
    )
}
