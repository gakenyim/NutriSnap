package com.example.nutrisnap

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

@Composable
fun FoodScannerScreen(modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Selected gallery image
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Captured camera image
    var capturedImage by remember { mutableStateOf<Bitmap?>(null) }

    // API states
    var isLoading by remember {
        mutableStateOf(false)
    }

    var predictionResult by remember {
        mutableStateOf<PredictionResponse?>(null)
    }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    // Gallery picker
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            selectedImageUri = uri
            capturedImage = null
            predictionResult = null
            errorMessage = null
        }
    }

    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            capturedImage = bitmap
            selectedImageUri = null
            predictionResult = null
            errorMessage = null
        }
    }

    val hasImage = selectedImageUri != null || capturedImage != null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Screen title
        Text(
            text = "Food Scanner",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Take a photo or upload image to discover its nutritional information.",
            style = MaterialTheme.typography.bodyMedium
        )

        // Image Preview
        if (selectedImageUri != null) {

            AsyncImage(
                model = selectedImageUri,
                contentDescription = "Selected food image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp),
                contentScale = ContentScale.Crop
            )
        } else if (capturedImage != null) {

            Image(
                bitmap = capturedImage!!.asImageBitmap(),
                contentDescription = "Captured food image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp),
                contentScale = ContentScale.Crop
            )

        } else {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("Your food image will appear here")
            }

        }

        // Gallery button
        Button(
            onClick = {
                galleryLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Upload from Gallery")
        }

        // Camera button
        OutlinedButton(
            onClick = {
                cameraLauncher.launch(null)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Take a Photo")
        }

        // Analyze button
        Button(
            onClick = {

                scope.launch {
                    isLoading = true
                    predictionResult = null
                    errorMessage = null

                    try {
                        // Convert image to multipart
                        val imagePart = createImagePart(
                            context = context,
                            imageUri = selectedImageUri,
                            bitmap = capturedImage
                        )

                        // Send image to FastAPI
                        val response = RetrofitClient.api.predictFood(
                            imagePart
                        )

                        if (response.isSuccessful) {

                            predictionResult = response.body()

                            if (predictionResult == null) {
                                errorMessage = "The server returned an empty response"
                            }

                        } else {
                            errorMessage =
                                "Prediction failed: ${response.code()} - ${response.errorBody()?.string()}"
                        }

                    } catch (e: Exception) {

                        errorMessage = e.localizedMessage
                            ?: "Something went wrong"

                    } finally {
                        isLoading = false
                    }
                }

            },

            enabled = hasImage && !isLoading,

            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (isLoading) {
                    "Analyzing..."
                } else {
                    "Analyze Food"
                }
            )
        }

        // Loading Indicator
        if (isLoading) {
            CircularProgressIndicator()

            Text("Identifying your food...")
        }

        // Error message
        errorMessage?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error
            )
        }

        // Prediction results
        predictionResult?.let { result ->

            // Food identification card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Prediction Result",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = result.predicted_food
                            .replace("_", " ")
                            .replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Text(
                        text = "Confidence: %.2f%%".format(result.confidence),
                        style = MaterialTheme.typography.bodyLarge
                    )

                }
            }

            // Nutrition card
            val details = result.nutrition
            val nutrients = details?.nutrition

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "Nutrition Information",
                        style = MaterialTheme.typography.titleLarge
                    )

                    if (details != null) {

                        Text(
                            details.food_name ?: result.predicted_food,
                            style = MaterialTheme.typography.titleMedium
                        )

                        // Serving size
                        val servingText = buildString {
                            if (details.serving_size != null) {
                                append(details.serving_size)
                                if (details.serving_size_unit != null) {
                                    append(" ")
                                    append(details.serving_size_unit)
                                }
                            }

                            if (!details.household_serving.isNullOrBlank()) {
                                if (isNotEmpty()) append(" (")
                                append(details.household_serving)
                                if (isNotEmpty() && !startsWith("(")) append(")")
                            }
                        }

                        if (servingText.isNotBlank()) {
                            Text(
                                "Serving size: $servingText",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        HorizontalDivider()

                        if (nutrients != null) {

                            NutrientRow(
                                label = "Calories",
                                value = nutrients.calories,
                                unit = "kcal"
                            )

                            NutrientRow(
                                label = "Protein",
                                value = nutrients.protein,
                                unit = "g"
                            )

                            NutrientRow(
                                label = "Carbohydrates",
                                value = nutrients.carbohydrates,
                                unit = "g"
                            )

                            NutrientRow(
                                label = "Fat",
                                value = nutrients.fat,
                                unit = "g"
                            )
                        } else {
                            Text("Nutrient values are unavailable.")
                        }
                    } else {
                        Text(
                            "Nutrition information could not be retrieved."
                        )
                    }
                }
            }

            // Top predictions
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Top Predictions",
                        style = MaterialTheme.typography.titleMedium
                    )

                    result.top_predictions.forEach { prediction ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = prediction.food_name
                                    .replace("_", " ")
                                    .replaceFirstChar { it.uppercase() }
                            )

                            Text(
                                text = "%.2f%%".format(prediction.confidence)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun NutrientRow(
    label: String,
    value: Double?,
    unit: String
) {
    Text(label)

    Text(
        if (value != null) {
            "%.2f %s".format(value, unit)
        } else {
            "Unavailable"
        },
        style = MaterialTheme.typography.titleMedium
    )
}


@Preview
@Composable
fun FoodScannerPreview(modifier: Modifier = Modifier) {
    FoodScannerScreen()
}