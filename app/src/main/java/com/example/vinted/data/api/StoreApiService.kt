package com.example.vinted.data.api

import com.example.vinted.data.model.ClothingItem
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface StoreApiService {
    @GET("products/category/women's clothing")
    suspend fun getWomensClothing(): Response<List<ClothingItem>>

    @GET("products/category/men's clothing")
    suspend fun getMensClothing(): Response<List<ClothingItem>>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Response<ClothingItem>
}

object RetrofitInstance {
    private const val BASE_URL = "https://fakestoreapi.com/"

    val api: StoreApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(StoreApiService::class.java)
    }
}