package com.example.vinted.data.model

data class ClothingItem(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: Rating,
    var isFavorite: Boolean = false
)

data class Rating(
    val rate: Double,
    val count: Int
)

// Assign condition based on item id (simulates real data)
fun ClothingItem.getCondition(): String {
    return when (id % 3) {
        0 -> "NEW"
        1 -> "USED"
        else -> "GOOD"
    }
}

fun ClothingItem.getConditionColor(): String {
    return when (getCondition()) {
        "NEW" -> "#4CAF50"
        "GOOD" -> "#2196F3"
        else -> "#FF6B35"
    }
}