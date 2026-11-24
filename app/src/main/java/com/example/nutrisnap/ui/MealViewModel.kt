package com.example.nutrisnap.ui

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrisnap.data.local.NutriSnapDatabase
import com.example.nutrisnap.model.DailyNutritionSummary
import com.example.nutrisnap.model.Meal
import com.example.nutrisnap.model.toDailySummary
import com.example.nutrisnap.data.repositories.MealRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MealViewModel(application: Application) : AndroidViewModel(application) {

    // 1. Grab the Room database and repository
    private val dao = NutriSnapDatabase.getDatabase(application).mealDao()
    private val repository = MealRepository(dao)

    // 2. Live list of ALL meals from Room
    val allMeals: StateFlow<List<Meal>> = repository.allMeals.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    // 3. Live list of FAVOURITE meals from Room
    val favouriteMeals: StateFlow<List<Meal>> = repository.favouriteMeals.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )
    val todaySummary: StateFlow<DailyNutritionSummary> = allMeals
        .map { meals -> meals.toDailySummary() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DailyNutritionSummary()
        )

//    init {
//        // Optional helper: seed sample meals once if the database is completely empty
//        viewModelScope.launch {
//            if (repository.allMeals.first().isEmpty()) {
//                SampleData.sampleMeals.forEach { repository.saveMeal(it) }
//            }
//        }
//    }

    fun toggleFavourite(meal: Meal) {
        viewModelScope.launch {
            repository.toggleFavourite(meal)
        }
    }

    fun editMeal(updatedMeal: Meal) {
        viewModelScope.launch {
            repository.editMeal(updatedMeal)
        }
    }

    fun deleteMeal(meal: Meal) {
        viewModelScope.launch {
            if (meal.imageUri.startsWith("file://")) {
                Uri.parse(meal.imageUri).path?.let { path ->
                    runCatching { java.io.File(path).delete() }
                }
            }

            repository.removeMeal(meal)
        }
    }

    fun addMeal(meal: Meal) {
        viewModelScope.launch {
            repository.saveMeal(meal)
        }
    }
}