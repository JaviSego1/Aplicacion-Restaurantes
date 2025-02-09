package com.example.aplicacionrestaurantes.ui.views.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicacionrestaurantes.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnValidate.setOnClickListener {
            val email = binding.etUser.text.toString()
            val password = binding.etPass.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Guardar el estado de sesión en SharedPreferences
                            val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
                            sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()

                            // Redirigir a MainActivity
                            val intent = Intent(    this, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Error: Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Por favor, ingresa tu correo y contraseña", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnRegistro.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }

        binding.btnRecuperar.setOnClickListener {
            val email = binding.etUser.text.toString().trim()
            if (email.isNotEmpty()) {
                recuperarContrasena(email)
            } else {
                Toast.makeText(this, "Ingrese el correo electrónico para recuperar la contraseña", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Función para recuperar contraseña
    private fun recuperarContrasena(email: String) {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Correo de recuperación de contraseña enviado",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "Error: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }


}
