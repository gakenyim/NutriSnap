package com.example.nutrisnap.ml

import com.example.nutrisnap.model.Meal
import com.example.nutrisnap.data.remote.PredictionResponse
import java.util.Locale
import java.util.UUID
import kotlin.math.roundToInt

fun PredictionResponse.toMeal(imageUri: String = ""): Meal {
    // Prefer the USDA-matched food name if present, otherwise fall back to the ML prediction
    val rawName = this.nutrition?.food_name
        ?.takeIf { it.isNotBlank() }
        ?: this.predicted_food

    // Clean up underscores/lowercase if predicted_food is a raw label like "grilled_chicken_salad"
    val formattedName = rawName
        .replace("_", " ")
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

    val nutrients = this.nutrition?.nutrition

    return Meal(
        id = UUID.randomUUID().toString(),
        name = formattedName,
        kcal = nutrients?.calories?.roundToInt() ?: 0,
        carbs = nutrients?.carbohydrates?.roundToInt() ?: 0,
        protein = nutrients?.protein?.roundToInt() ?: 0,
        fat = nutrients?.fat?.roundToInt() ?: 0,
        healthScore = calculateHealthScore(
            kcal = nutrients?.calories ?: 0.0,
            protein = nutrients?.protein ?: 0.0,
            fat = nutrients?.fat ?: 0.0
        ),
        imageUri = imageUri,
        timestamp = System.currentTimeMillis(),
        isFavourite = false,
    )
}

private fun calculateHealthScore(kcal: Double, protein: Double, fat: Double): Int {
    if (kcal <= 0.0) return 75 // Neutral default if USDA lookup returned null
    val proteinRatio = (protein * 4.0) / kcal
    val fatRatio = (fat * 9.0) / kcal
    val score = 70 + (proteinRatio * 50).roundToInt() - (fatRatio * 30).roundToInt()
    return score.coerceIn(10, 100)
}