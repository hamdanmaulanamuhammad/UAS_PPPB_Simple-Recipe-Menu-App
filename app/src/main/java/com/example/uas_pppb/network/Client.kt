package com.example.uas_pppb.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Client {
    private const val BASE_URL = "https://ppbo-api.vercel.app/"

    // Membuat instance HttpLoggingInterceptor
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Atur level logging
    }

    // Membuat OkHttpClient dengan interceptor
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging) // Menambahkan interceptor ke OkHttpClient
        .build()

    // Membuat instance Retrofit dengan OkHttpClient yang telah dikonfigurasi
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client) // Menggunakan OkHttpClient yang telah dikonfigurasi
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Mengembalikan instance RecipeApi
    fun getInstance(): RecipeApi {
        return retrofit.create(RecipeApi::class.java)
    }
}
