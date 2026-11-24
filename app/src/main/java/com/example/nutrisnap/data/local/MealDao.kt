package com.example.nutrisnap.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.nutrisnap.model.Meal
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {

    // Inserts a meal; if a meal with the same id already exists, replace it
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: Meal)

    // Updates an existing meal (e.g., editing name/macros or toggling favourite)
    @Update
    suspend fun updateMeal(meal: Meal)

    // Deletes a specific meal from the table
    @Delete
    suspend fun deleteMeal(meal: Meal)

    // Returns all meals ordered newest first; Flow updates automatically on changes
    @Query("SELECT * FROM meals ORDER BY timestamp DESC")
    fun getAllMeals(): Flow<List<Meal>>

    // Returns only meals where isFavourite is true (1 in SQLite)
    @Query("SELECT * FROM meals WHERE isFavourite = 1 ORDER BY timestamp DESC")
    fun getFavouriteMeals(): Flow<List<Meal>>

    // Searches meals whose name contains the typed query (case-insensitive)
    @Query("SELECT * FROM meals WHERE name LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    fun searchMealsByName(query: String): Flow<List<Meal>>

    // Returns meals saved between two timestamps (e.g., start of today to end of today)
    @Query("SELECT * FROM meals WHERE timestamp BETWEEN :startOfDay AND :endOfDay ORDER BY timestamp DESC")
    fun getMealsForDay(startOfDay: Long, endOfDay: Long): Flow<List<Meal>>
}