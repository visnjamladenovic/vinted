package com.example.vinted.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vinted.data.model.ClothingItem
import com.example.vinted.data.repository.ClothingRepository
import kotlinx.coroutines.launch

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

class ClothingViewModel(private val repository: ClothingRepository) : ViewModel() {

    private val _allItems = mutableListOf<ClothingItem>()
    private var currentCategory = "All"
    private var currentQuery = ""

    private val _items = MutableLiveData<UiState<List<ClothingItem>>>()
    val items: LiveData<UiState<List<ClothingItem>>> = _items

    private val _selectedItem = MutableLiveData<ClothingItem?>()
    val selectedItem: LiveData<ClothingItem?> = _selectedItem

    fun loadClothing() {
        _items.value = UiState.Loading
        viewModelScope.launch {
            try {
                val result = repository.getAllClothing()
                _allItems.clear()
                _allItems.addAll(result)
                applyFilters()
            } catch (e: Exception) {
                _items.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun filterByCategory(category: String) {
        currentCategory = category
        applyFilters()
    }

    fun search(query: String) {
        currentQuery = query
        applyFilters()
    }

    fun toggleFavorite(item: ClothingItem) {
        val index = _allItems.indexOfFirst { it.id == item.id }
        if (index != -1) {
            _allItems[index] = _allItems[index].copy(isFavorite = !_allItems[index].isFavorite)
            applyFilters()
        }
    }

    private fun applyFilters() {
        var filtered = _allItems.toList()
        if (currentCategory != "All") {
            filtered = filtered.filter { it.category == currentCategory }
        }
        if (currentQuery.isNotEmpty()) {
            filtered = filtered.filter {
                it.title.contains(currentQuery, ignoreCase = true)
            }
        }
        _items.value = UiState.Success(filtered)
    }

    fun setSelectedItem(item: ClothingItem) {
        _selectedItem.value = item
    }
}