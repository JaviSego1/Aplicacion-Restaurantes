package com.example.aplicacionrestaurantes.data.service

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RetrofitClient {

    // Con la IP 10.0.2.2 es para uso local, con el emulador
    private const val BASE_URL = "http://10.0.2.2:8081/"

    // Interceptor para log de las peticiones HTTP
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // OkHttpClient que usaremos con Retrofit
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    // Método que proporciona la instancia de ApiService
    @Provides
    fun provideApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)
    }
}
