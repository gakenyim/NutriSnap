package com.example.nutrisnap.data.repositories

import com.example.nutrisnap.data.database.MealDao
import com.example.nutrisnap.data.models.Meal
import kotlinx.coroutines.flow.Flow

class MealRepository(private val mealDao: MealDao) {

    val allMeals: Flow<List<Meal>> = mealDao.getAllMeals()
    val favouriteMeals: Flow<List<Meal>> = mealDao.getFavouriteMeals()

    fun searchMeals(query: String): Flow<List<Meal>> =
        mealDao.searchMealsByName(query)

    fun mealsForDay(startOfDay: Long, endOfDay: Long): Flow<List<Meal>> =
        mealDao.getMealsForDay(startOfDay, endOfDay)

    suspend fun saveMeal(meal: Meal) {
        mealDao.insertMeal(meal)
    }

    suspend fun editMeal(meal: Meal) {
        mealDao.updateMeal(meal)
    }

    suspend fun toggleFavourite(meal: Meal) {
        // Flips isFavourite (true -> false or false -> true) and saves it
        mealDao.updateMeal(meal.copy(isFavourite = !meal.isFavourite))
    }

    suspend fun removeMeal(meal: Meal) {
        mealDao.deleteMeal(meal)
    }
}