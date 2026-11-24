package com.example.nutrisnap.data

import com.example.nutrisnap.model.Meal

object SampleData {
    val sampleMeals = listOf(
        Meal(
            id = "1",
            name = "Chicken Power Bowl",
            kcal = 480,
            carbs = 42,
            protein = 38,
            fat = 16,
            healthScore = 88,
            imageUri = "",
            timestamp = System.currentTimeMillis(),
            isFavourite = true
        ),
        Meal(
            id = "2",
            name = "Minced Meat Pasta",
            kcal = 610,
            carbs = 68,
            protein = 29,
            fat = 22,
            healthScore = 72,
            imageUri = "",
            timestamp = System.currentTimeMillis() - 3_600_000,
            isFavourite = false
        ),
        Meal(
            id = "3",
            name = "Tofu Salad",
            kcal = 310,
            carbs = 19,
            protein = 22,
            fat = 14,
            healthScore = 92,
            imageUri = "",
            timestamp = System.currentTimeMillis() - 7_200_000,
            isFavourite = true
        )
    )
}