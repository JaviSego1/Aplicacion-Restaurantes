package com.example.aplicacionrestaurantes.ui.views.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.aplicacionrestaurantes.R
import com.example.aplicacionrestaurantes.domain.models.Restaurant

class RestaurantDialogFragmentCU : DialogFragment() {

    private lateinit var nameEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var phoneEditText: EditText
    private var restaurantToEdit: Restaurant? = null
    var onUpdate: ((Restaurant) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_restaurant_dialog_cu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nameEditText = view.findViewById(R.id.nameEditText)
        addressEditText = view.findViewById(R.id.addressEditText)
        phoneEditText = view.findViewById(R.id.phoneEditText)

        // Si el fragmento recibe un restaurante para editar
        restaurantToEdit = arguments?.getParcelable("restaurant")
        restaurantToEdit?.let {
            nameEditText.setText(it.name)
            addressEditText.setText(it.address)
            phoneEditText.setText(it.phone)
        }

        view.findViewById<View>(R.id.saveButton).setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val address = addressEditText.text.toString().trim()
            val phone = phoneEditText.text.toString().trim()

            if (name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                Toast.makeText(context, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
            } else {
                val restaurant = Restaurant(name = name, address = address, phone = phone)

                if (restaurantToEdit != null) {
                    // Editar el restaurante
                    onUpdate?.invoke(restaurant)
                } else {
                    // Agregar un nuevo restaurante
                    onUpdate?.invoke(restaurant)
                }
                dismiss()
            }
        }

        view.findViewById<View>(R.id.cancelButton).setOnClickListener {
            dismiss()
        }
    }

    fun newInstance(restaurant: Restaurant? = null): RestaurantDialogFragmentCU {
        val fragment = RestaurantDialogFragmentCU()
        val args = Bundle()
        args.putParcelable("restaurant", restaurant)
        fragment.arguments = args
        return fragment
    }
}
