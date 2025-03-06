package com.example.aplicacionrestaurantes.ui.views.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aplicacionrestaurantes.data.models.AuthResponse
import com.example.aplicacionrestaurantes.data.service.LoginRequest
import com.example.aplicacionrestaurantes.data.service.RetrofitClient
import com.example.aplicacionrestaurantes.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("login-info", MODE_PRIVATE)

        binding.btnRegistro.setOnClickListener{
            startActivity(Intent(this, RegistroActivity::class.java))
        }

        binding.btnValidate.setOnClickListener{
            val email = binding.etUser.text.toString().trim()
            val password = binding.etPass.text.toString()

            if (email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val loginRequest = LoginRequest(email, password)
            RetrofitClient.apiService.login(loginRequest).enqueue(object: Callback<AuthResponse> {
                override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                    if (response.isSuccessful) {
                        val token = response.body()?.token
                        token?.let {
                            saveToken(it)
                            Toast.makeText(this@LoginActivity, "Inicio de sesión correcto", Toast.LENGTH_LONG).show()
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Correo o contraseña incorrectos", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    // Función para guardar el token en SharedPreferences
    private fun saveToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString("jwt_token", token)
        editor.apply()
    }
}
