package com.example.nutrisnap.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

data class FoodPrediction(
    val food_name: String,
    val confidence: Double
)

data class NutrientValues(
    val calories: Double?,
    val protein: Double?,
    val carbohydrates: Double?,
    val fat: Double?
)

data class NutritionDetails(
    val food_name: String?,
    val fdc_id: Int?,
    val data_type: String?,
    val serving_size: Double?,
    val serving_size_unit: String?,
    val household_serving: String?,
    val nutrition: NutrientValues?
)

data class PredictionResponse(
    val predicted_food: String,
    val confidence: Double,
    val top_predictions: List<FoodPrediction>,
    val nutrition: NutritionDetails?
)

// USDA FoodData Central API models (must match USDA's camelCase JSON keys)
data class UsdaSearchResponse(
    val foods: List<UsdaFoodDto> = emptyList()
)

data class UsdaFoodDto(
    val fdcId: Int,
    val description: String?,
    val dataType: String?,
    val servingSize: Double?,
    val servingSizeUnit: String?,
    val householdServingFullText: String?,
    val foodNutrients: List<UsdaNutrientDto> = emptyList()
)

data class UsdaNutrientDto(
    val nutrientId: Int,
    val nutrientName: String?,
    val value: Double?,
    val unitName: String?
)

interface UsdaApi {
    @GET("foods/search")
    suspend fun searchFood(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("pageSize") pageSize: Int = 1
    ): UsdaSearchResponse
}