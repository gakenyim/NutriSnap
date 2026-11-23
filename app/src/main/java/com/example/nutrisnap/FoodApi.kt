package com.example.nutrisnap

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

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

interface FoodApi {

    @Multipart
    @POST("predict/")
    suspend fun predictFood(
        @Part file: MultipartBody.Part
    ): Response<PredictionResponse>

}