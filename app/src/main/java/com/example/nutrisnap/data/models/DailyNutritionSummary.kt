package com.example.nutrisnap.data.models

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

// 1. Clean data bundle Member 1 can read directly for Today's Overall
data class DailyNutritionSummary(
    val totalKcal: Int = 0,
    val totalProtein: Int = 0,
    val totalCarbs: Int = 0,
    val totalFat: Int = 0,
    val mealCount: Int = 0
)

// 2. Helper function that calculates totals from any List<Meal>
fun List<Meal>.toDailySummary(date: LocalDate = LocalDate.now()): DailyNutritionSummary {
    val todaysMeals = this.filter { meal ->
        val mealDate = Instant.ofEpochMilli(meal.timestamp)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        mealDate == date
    }

    return DailyNutritionSummary(
        totalKcal = todaysMeals.sumOf { it.kcal },
        totalProtein = todaysMeals.sumOf { it.protein },
        totalCarbs = todaysMeals.sumOf { it.carbs },
        totalFat = todaysMeals.sumOf { it.fat },
        mealCount = todaysMeals.size
    )
}