package com.example.aplicacionrestaurantes.ui.views.fragment

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.aplicacionrestaurantes.databinding.DialogRestauranteBinding
import com.example.aplicacionrestaurantes.domain.models.Restaurant
import java.io.ByteArrayOutputStream

class RestaurantDialogFragmentCU : DialogFragment() {

    private lateinit var binding: DialogRestauranteBinding
    private var currentRestaurant: Restaurant? = null
    private var selectedImageUri: Uri? = null

    // Callback para notificar al fragmento cuando se confirme la acción
    var onUpdate: ((Restaurant) -> Unit)? = null

    companion object {
        fun newInstance(restaurant: Restaurant): RestaurantDialogFragmentCU {
            val args = Bundle()
            args.putSerializable("restaurant", restaurant)
            val fragment = RestaurantDialogFragmentCU()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogRestauranteBinding.inflate(LayoutInflater.from(requireContext()))
        currentRestaurant = arguments?.getSerializable("restaurant") as? Restaurant

        currentRestaurant?.let { restaurant ->
            binding.edtTitulo.setText(restaurant.titulo)
            binding.edtDescripcion.setText(restaurant.descripcion)
            if (restaurant.imagen.startsWith("data:image")) {
                val imageBytes = Base64.decode(restaurant.imagen.split(",")[1], Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                binding.imgRestaurante.setImageBitmap(bitmap)
            } else {
                loadImageFromResource(restaurant.imagen)
            }
        }

        binding.btnTomarFoto.setOnClickListener { checkCameraPermission() }
        binding.btnSeleccionarGaleria.setOnClickListener { checkMediaPermission() }

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setTitle(if (currentRestaurant == null) "Agregar Restaurant" else "Editar Restaurant")
            .setPositiveButton("Guardar") { _, _ -> saveRestaurant() }
            .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
            .create()
    }

    private fun saveRestaurant() {
        val titulo = binding.edtTitulo.text.toString()
        val descripcion = binding.edtDescripcion.text.toString()

        if (titulo.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val imagen = selectedImageUri?.let { uri ->
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
                "data:image/jpeg;base64,${bitmapToBase64(bitmap)}"
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } ?: currentRestaurant?.imagen ?: "android.resource://${requireContext().packageName}/drawable/alchemist"

        val restaurant = Restaurant(
            id = currentRestaurant?.id ?: 0,
            titulo = titulo,
            descripcion = descripcion,
            imagen = imagen
        )

        onUpdate?.invoke(restaurant)
    }

    private fun loadImageFromResource(imagen: String) {
        val resId = context?.resources?.getIdentifier(imagen, "drawable", context?.packageName)
        resId?.let { binding.imgRestaurante.setImageResource(it) }
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedImageUri = result.data?.data
            selectedImageUri?.let { uri -> binding.imgRestaurante.setImageURI(uri) }
        }
    }

    private fun openImageSelector() {
        val intent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
        imagePickerLauncher.launch(intent)
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) openCamera() else Toast.makeText(requireContext(), "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
    }

    private val requestMediaPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) openImageSelector() else Toast.makeText(requireContext(), "Permiso de acceso a medios denegado", Toast.LENGTH_SHORT).show()
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        } else {
            openCamera()
        }
    }

    private fun checkMediaPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                openImageSelector()
            } else {
                requestMediaPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            }
        } else {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openImageSelector()
            } else {
                requestMediaPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as Bitmap
            binding.imgRestaurante.setImageBitmap(imageBitmap)
            selectedImageUri = saveImageToGallery(imageBitmap)
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(intent)
    }

    private fun saveImageToGallery(bitmap: Bitmap): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "image_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        val uri = requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        uri?.let {
            val outputStream = requireContext().contentResolver.openOutputStream(it)
            outputStream?.use { stream -> bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream) }
        }
        return uri
    }
}
