package com.example.vinted.ui.main

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vinted.R
import com.example.vinted.data.model.ClothingItem
import com.example.vinted.data.model.getCondition
import com.example.vinted.data.model.getConditionColor

class ClothingAdapter(
    private val onItemClick: (ClothingItem) -> Unit,
    private val onFavoriteClick: (ClothingItem) -> Unit
) : ListAdapter<ClothingItem, ClothingAdapter.ClothingViewHolder>(DiffCallback) {

    class ClothingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.itemImage)
        val title: TextView = view.findViewById(R.id.itemTitle)
        val price: TextView = view.findViewById(R.id.itemPrice)
        val conditionBadge: TextView = view.findViewById(R.id.conditionBadge)
        val btnFavorite: ImageButton = view.findViewById(R.id.btnFavorite)

        fun bind(item: ClothingItem, onItemClick: (ClothingItem) -> Unit, onFavoriteClick: (ClothingItem) -> Unit) {
            title.text = item.title
            price.text = "€${String.format("%.2f", item.price)}"

            // Condition badge
            val condition = item.getCondition()
            conditionBadge.text = condition
            conditionBadge.backgroundTintList = android.content.res.ColorStateList.valueOf(
                Color.parseColor(item.getConditionColor())
            )

            // Favorite button
            btnFavorite.setImageResource(
                if (item.isFavorite) android.R.drawable.btn_star_big_on
                else android.R.drawable.btn_star_big_off
            )

            Glide.with(itemView.context).load(item.image).centerCrop().into(image)
            itemView.setOnClickListener { onItemClick(item) }
            btnFavorite.setOnClickListener { onFavoriteClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClothingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_clothing, parent, false)
        return ClothingViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClothingViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClick, onFavoriteClick)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ClothingItem>() {
        override fun areItemsTheSame(oldItem: ClothingItem, newItem: ClothingItem) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: ClothingItem, newItem: ClothingItem) = oldItem == newItem
    }
}