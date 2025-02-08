package com.example.aplicacionrestaurantes.ui.views.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.aplicacionrestaurantes.data.models.Restaurante
import com.example.aplicacionrestaurantes.databinding.DialogRestauranteBinding

class RestaurantDialogFragmentCU : DialogFragment() {

    // Binding para el diálogo
    private lateinit var binding: DialogRestauranteBinding

    // Variable para almacenar el restaurante actual (en caso de edición)
    private var currentRestaurant: Restaurante? = null

    // Callback para notificar al fragmento cuando se confirme la acción
    var onUpdate: ((Restaurante) -> Unit)? = null

    companion object {
        // Método para crear una nueva instancia del diálogo con un restaurante existente (edición)
        fun newInstance(restaurante: Restaurante): RestaurantDialogFragmentCU {
            val args = Bundle()
            args.putSerializable("restaurante", restaurante)
            val fragment = RestaurantDialogFragmentCU()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Inflar el layout del diálogo
        binding = DialogRestauranteBinding.inflate(LayoutInflater.from(requireContext()))

        // Obtener el restaurante actual (si se está editando)
        currentRestaurant = arguments?.getSerializable("restaurante") as? Restaurante

        // Si hay un restaurante actual, llenar los campos con sus datos
        currentRestaurant?.let { restaurante ->
            binding.edtTitulo.setText(restaurante.titulo)
            binding.edtDescripcion.setText(restaurante.descripcion)
        }

        // Configurar el diálogo
        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setTitle(if (currentRestaurant == null) "Agregar Restaurante" else "Editar Restaurante")
            .setPositiveButton("Guardar") { _, _ ->
                // Validar los campos y notificar al fragmento
                saveRestaurant()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }

    private fun saveRestaurant() {
        // Obtener los valores de los campos
        val titulo = binding.edtTitulo.text.toString()
        val descripcion = binding.edtDescripcion.text.toString()

        // Validar que los campos no estén vacíos
        if (titulo.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val imagen = "android.resource://${requireContext().packageName}/drawable/alchemist"

        // Crear un nuevo objeto Restaurante con los datos ingresados
        val restaurante = Restaurante(
            titulo = titulo,
            descripcion = descripcion,
            imagen = imagen
        )

        // Notificar al fragmento que se ha confirmado la acción
        onUpdate?.invoke(restaurante)
    }

}