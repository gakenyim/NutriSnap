package com.example.nutrisnap

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri

class FoodScannerRepository(
    context: Context,
    private val usdaApiKey: String
) {
    private val classifier = FoodClassifier(context.applicationContext)

    suspend fun analyzeFood(
        context: Context,
        imageUri: Uri?,
        bitmap: Bitmap?
    ): PredictionResponse {
        val softwareBitmap = loadSoftwareBitmap(context, imageUri, bitmap)
        val topPredictions = classifier.classify(softwareBitmap, topK = 5)
        val best = topPredictions.first()

        val cleanQuery = best.food_name.replace("_", " ")
        val nutritionDetails = try {
            val usdaResponse = RetrofitClient.usdaApi.searchFood(
                apiKey = usdaApiKey,
                query = cleanQuery
            )
            usdaResponse.foods.firstOrNull()?.toNutritionDetails()
        } catch (e: Exception) {
            null
        }

        return PredictionResponse(
            predicted_food = best.food_name,
            confidence = best.confidence,
            top_predictions = topPredictions,
            nutrition = nutritionDetails
        )
    }

    private fun UsdaFoodDto.toNutritionDetails(): NutritionDetails {
        fun findNutrient(id: Int): Double? =
            foodNutrients.firstOrNull { it.nutrientId == id }?.value

        return NutritionDetails(
            food_name = description,
            fdc_id = fdcId,
            data_type = dataType,
            serving_size = servingSize ?: 100.0,
            serving_size_unit = servingSizeUnit ?: "g",
            household_serving = householdServingFullText,
            nutrition = NutrientValues(
                calories = findNutrient(1008),
                protein = findNutrient(1003),
                carbohydrates = findNutrient(1005),
                fat = findNutrient(1004)
            )
        )
    }
}