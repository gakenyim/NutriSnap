package com.example.nutrisnap.data.models

data class Meal(
    val id: Long = 0,
    val name: String,
    val kcal: Int,
    val carbs: Int,
    val protein: Int,
    val fat: Int,
    val healthScore: Int,
    val imageUri: String?,
    val timestamp: Long,
    val isFavourite: Boolean = false
)