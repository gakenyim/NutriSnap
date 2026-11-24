package com.example.nutrisnap.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meals") // Creates a "meals" table in the Room database
data class Meal(
    @PrimaryKey val id: String,       // Unique ID for each meal (e.g., UUID string)
    val name: String,                 // Food name, e.g., "Grilled Chicken Salad"
    val kcal: Int,                    // Total calories
    val carbs: Int,                   // Carbohydrates in grams
    val protein: Int,                 // Protein in grams
    val fat: Int,                     // Fat in grams
    val healthScore: Int,             // Score from 0–100 (or 1–10)
    val imageUri: String,             // Path/URI string to the meal photo (can be "" if none)
    val timestamp: Long,              // Time saved in milliseconds (System.currentTimeMillis())
    val isFavourite: Boolean = false  // True if starred/favourited by the user
)