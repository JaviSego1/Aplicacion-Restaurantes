package com.example.aplicacionrestaurantes.data.service

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitClient {

    // Con la IP 10.0.2.2 es para uso local, con el emulador
    private const val BASE_URL = "http://10.0.2.2:8081/"

    // Interceptor para log de las peticiones HTTP
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Interceptor para agregar el token JWT en las solicitudes
    private fun authInterceptor(context: Context): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            val sharedPreferences = context.getSharedPreferences("login-info", Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("jwt_token", null)

            val newRequest = if (token != null) {
                // Agrega el token en el encabezado Authorization
                request.newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
            } else {
                // Si no hay token, procede con la solicitud sin el encabezado
                request
            }

            chain.proceed(newRequest)
        }
    }

    // OkHttpClient que usaremos con Retrofit
    private fun provideOkHttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logging)  // Interceptor para logs
            .addInterceptor(authInterceptor(context))  // Interceptor para autenticación
            .build()
    }

    // Método que proporciona la instancia de ApiService
    @Provides
    @Singleton
    fun provideApiService(@ApplicationContext context: Context): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideOkHttpClient(context))  // Usa el OkHttpClient con los interceptores
            .build()
            .create(ApiService::class.java)
    }
}