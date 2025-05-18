package com.moblieappproject.todo.data.network


import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//kotlin singleton object
object RetrofitClient {
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    val apiService: TodoApiService by lazy {
        //logs- trace response and request
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        // Build an OkHttpClient that logs all HTTP bodies
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()


        // Build Retrofit with base URL, gson converter, and our client

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            //GsonConverterFactory: converts JSON to Kotlin objects and vise versa
            .build()
            .create(TodoApiService::class.java)
    }
}