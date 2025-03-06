package com.example.aplicacionrestaurantes.ui.views.activity

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicacionrestaurantes.databinding.ActivityRegistroBinding
import com.example.aplicacionrestaurantes.data.models.Usuario
import com.example.aplicacionrestaurantes.data.service.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegistro.setOnClickListener {
            val name = binding.editName.text.toString().trim()
            val email = binding.editEmail.text.toString().trim()
            val password = binding.editPassword.text.toString().trim()
            val confirmPassword = binding.editConfirmPassword.text.toString().trim()

            // Validación de los campos
            if (email.isEmpty() || name.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Correo electrónico inválido", Toast.LENGTH_SHORT).show()
            } else if (password != confirmPassword) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            } else if (password.length < 6) {
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
            } else {
                val usuario = Usuario(name = name, email = email, password = password) // Crear objeto User
                registerUser(usuario)
            }
        }

        binding.btnVolver.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun registerUser(user: Usuario) {
        // Hacer la petición de registro utilizando Retrofit
        RetrofitClient.apiService.register(user).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@RegistroActivity, "Registro exitoso, inicia sesión", Toast.LENGTH_LONG).show()
                    val intent = Intent(this@RegistroActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@RegistroActivity, "Error al registrarse con el usuario", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@RegistroActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
