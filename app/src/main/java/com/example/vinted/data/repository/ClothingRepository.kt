package com.example.vinted.data.repository

import com.example.vinted.data.api.StoreApiService
import com.example.vinted.data.model.ClothingItem

class ClothingRepository(private val api: StoreApiService) {

    suspend fun getAllClothing(): List<ClothingItem> {
        val womens = api.getWomensClothing().body() ?: emptyList()
        val mens = api.getMensClothing().body() ?: emptyList()
        return (womens + mens).sortedBy { it.price }
    }

    suspend fun getItemById(id: Int): ClothingItem? {
        return api.getProductById(id).body()
    }
}