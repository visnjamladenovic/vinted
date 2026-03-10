package com.example.vinted.ui.detail

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.vinted.R

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val title = intent.getStringExtra("item_title") ?: ""
        val price = intent.getDoubleExtra("item_price", 0.0)
        val image = intent.getStringExtra("item_image") ?: ""
        val desc = intent.getStringExtra("item_desc") ?: ""

        supportActionBar?.title = title
        findViewById<TextView>(R.id.detailTitle).text = title
        findViewById<TextView>(R.id.detailPrice).text = "€${String.format("%.2f", price)}"
        findViewById<TextView>(R.id.detailDescription).text = desc
        Glide.with(this).load(image).into(findViewById<ImageView>(R.id.detailImage))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}