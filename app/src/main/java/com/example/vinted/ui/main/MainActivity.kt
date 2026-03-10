package com.example.vinted.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vinted.R
import com.example.vinted.data.api.RetrofitInstance
import com.example.vinted.data.repository.ClothingRepository
import com.example.vinted.ui.detail.DetailActivity
import com.example.vinted.ui.viewmodel.ClothingViewModel
import com.example.vinted.ui.viewmodel.ClothingViewModelFactory
import com.example.vinted.ui.viewmodel.UiState
import com.google.android.material.chip.ChipGroup

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ClothingViewModel
    private lateinit var adapter: ClothingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository = ClothingRepository(RetrofitInstance.api)
        viewModel = ViewModelProvider(this, ClothingViewModelFactory(repository))
            .get(ClothingViewModel::class.java)

        setupRecyclerView()
        setupChips()
        setupSearch()
        observeViewModel()

        viewModel.loadClothing()
    }

    private fun setupRecyclerView() {
        adapter = ClothingAdapter(
            onItemClick = { item ->
                viewModel.setSelectedItem(item)
                startActivity(Intent(this, DetailActivity::class.java).apply {
                    putExtra("item_id", item.id)
                    putExtra("item_title", item.title)
                    putExtra("item_price", item.price)
                    putExtra("item_image", item.image)
                    putExtra("item_desc", item.description)
                    putExtra("item_favorite", item.isFavorite)
                })
            },
            onFavoriteClick = { item ->
                viewModel.toggleFavorite(item)
            }
        )
        findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            adapter = this@MainActivity.adapter
        }
    }

    private fun setupChips() {
        findViewById<ChipGroup>(R.id.chipGroup).setOnCheckedStateChangeListener { _, checkedIds ->
            val category = when (checkedIds.firstOrNull()) {
                R.id.chipWomens -> "women's clothing"
                R.id.chipMens -> "men's clothing"
                else -> "All"
            }
            viewModel.filterByCategory(category)
        }
    }

    private fun setupSearch() {
        findViewById<EditText>(R.id.searchBar).addTextChangedListener { text ->
            viewModel.search(text.toString())
        }
    }

    private fun observeViewModel() {
        val progress = findViewById<ProgressBar>(R.id.progressBar)
        val error = findViewById<TextView>(R.id.errorText)

        viewModel.items.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
                    progress.visibility = View.VISIBLE
                    error.visibility = View.GONE
                }
                is UiState.Success -> {
                    progress.visibility = View.GONE
                    adapter.submitList(state.data.toList())
                }
                is UiState.Error -> {
                    progress.visibility = View.GONE
                    error.visibility = View.VISIBLE
                    error.text = state.message
                }
            }
        }
    }
}