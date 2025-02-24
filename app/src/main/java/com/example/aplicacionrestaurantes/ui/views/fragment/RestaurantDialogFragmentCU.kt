package com.example.aplicacionrestaurantes.ui.views.fragment

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.aplicacionrestaurantes.data.models.Restaurante
import com.example.aplicacionrestaurantes.databinding.DialogRestauranteBinding
import java.io.ByteArrayOutputStream

class RestaurantDialogFragmentCU : DialogFragment() {

    private lateinit var binding: DialogRestauranteBinding
    private var currentRestaurant: Restaurante? = null
    private var imageBitmap: Bitmap? = null

    // Callback para notificar al fragmento cuando se confirme la acción
    var onUpdate: ((Restaurante) -> Unit)? = null

    companion object {
        const val CAMERA_REQUEST_CODE = 100
        const val GALLERY_REQUEST_CODE = 101
        const val CAMERA_PERMISSION_REQUEST = 102

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
            // Cargar la imagen si existe
            loadImageFromResource(restaurante.imagen)
        }

        // Configurar los botones
        binding.btnTomarFoto.setOnClickListener {
            requestCameraPermission() // Llama a la función que maneja la toma de foto
        }

        binding.btnSeleccionarGaleria.setOnClickListener {
            openGallery() // Llama a la función que abre la galería
        }

        // Configurar el diálogo
        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setTitle(if (currentRestaurant == null) "Agregar Restaurante" else "Editar Restaurante")
            .setPositiveButton("Guardar") { _, _ ->
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

        // Convertir la imagen seleccionada a Base64 si se ha seleccionado una imagen
        val imagen = if (imageBitmap != null) {
            convertBitmapToBase64(imageBitmap)
        } else {
            "android.resource://${requireContext().packageName}/drawable/alchemist" // Imagen por defecto
        }

        // Crear un nuevo objeto Restaurante con los datos ingresados
        val restaurante = Restaurante(
            titulo = titulo,
            descripcion = descripcion,
            imagen = imagen
        )

        // Notificar al fragmento que se ha confirmado la acción
        onUpdate?.invoke(restaurante)
    }

    private fun loadImageFromResource(imagen: String) {
        val resId = context?.resources?.getIdentifier(imagen, "drawable", context?.packageName)
        resId?.let {
            binding.imgRestaurante.setImageResource(it)
        }
    }

    private fun convertBitmapToBase64(bitmap: Bitmap?): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun openCamera() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST)
        } else {
            val values = ContentValues()
            val imageUri = requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    val imageUri = data?.data
                    imageUri?.let {
                        val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, it)
                        binding.imgRestaurante.setImageBitmap(bitmap)
                        imageBitmap = bitmap
                    }
                }
                GALLERY_REQUEST_CODE -> {
                    data?.data?.let {
                        val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, it)
                        binding.imgRestaurante.setImageBitmap(bitmap)
                        imageBitmap = bitmap
                    }
                }
            }
        }
    }

    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST)
        } else {
            openCamera()
        }
    }
}
