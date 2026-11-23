package com.example.nutrisnap

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

// Color Palette
private val PeachSurface = Color(0xFFFBECE2)
private val OrangeAccent = Color(0xFFFF8C24)
private val CyanMacro = Color(0xFF42C2F5)
private val LimeMacro = Color(0xFF8CE67E)
private val HealthGreen = Color(0xFF5BD43B)

@Composable
fun FoodScannerScreen2(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Selected gallery image
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Captured camera image
    var capturedImage by remember { mutableStateOf<Bitmap?>(null) }

    // API states
    var isLoading by remember { mutableStateOf(false) }
    var predictionResult by remember { mutableStateOf<PredictionResponse?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Details dialog state (for Serving size, Confidence & Top Predictions)
    var showDetailsDialog by remember { mutableStateOf(false) }

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

    val analyzeFood: () -> Unit = {
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
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (predictionResult == null) {
            // ==========================================
            // 1. SCAN PAGE
            // ==========================================
            ScanPageUI(
                selectedImageUri = selectedImageUri,
                capturedImage = capturedImage,
                hasImage = hasImage,
                isLoading = isLoading,
                errorMessage = errorMessage,
                onAnalyzeClick = analyzeFood,
                onCameraClick = { cameraLauncher.launch(null) },
                onGalleryClick = {
                    galleryLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                onCloseClick = {
                    selectedImageUri = null
                    capturedImage = null
                    predictionResult = null
                    errorMessage = null
                }
            )
        } else {
            // ==========================================
            // 2. SCANNED RESULTS PAGE
            // ==========================================
            ScannedResultsPageUI(
                result = predictionResult!!,
                selectedImageUri = selectedImageUri,
                capturedImage = capturedImage,
                onCloseClick = {
                    predictionResult = null
                },
                onInfoClick = { showDetailsDialog = true },
                onUpdateDetailsClick = { showDetailsDialog = true },
                onConfirmClick = { showDetailsDialog = true },
                onCameraFabClick = {
                    predictionResult = null
                    cameraLauncher.launch(null)
                }
            )
        }

        // Loading Overlay
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.55f)),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    shadowElevation = 6.dp
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CircularProgressIndicator(color = OrangeAccent)
                        Text(
                            text = "Identifying your food...",
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                    }
                }
            }
        }

        // Details Dialog (Serving Size, Confidence, Top Predictions)
        if (showDetailsDialog && predictionResult != null) {
            PredictionDetailsDialog(
                result = predictionResult!!,
                onDismiss = { showDetailsDialog = false }
            )
        }
    }
}

// ==========================================
// 1. SCAN PAGE UI
// ==========================================
@Composable
private fun ScanPageUI(
    selectedImageUri: Uri?,
    capturedImage: Bitmap?,
    hasImage: Boolean,
    isLoading: Boolean,
    errorMessage: String?,
    onAnalyzeClick: () -> Unit,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit,
    onCloseClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1F1D1B))
    ) {
        // Image Preview Area
        FoodImagePreview(
            selectedImageUri = selectedImageUri,
            capturedImage = capturedImage,
            placeholderText = "Your food image will appear here",
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.68f)
        )

        // Viewfinder Corner Brackets
        ScannerBracketOverlay(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.56f)
                .padding(horizontal = 36.dp, vertical = 76.dp)
        )

        // Top-Left Close Button
        IconButton(
            onClick = onCloseClick,
            modifier = Modifier
                .padding(top = 40.dp, start = 20.dp)
                .size(36.dp)
                .border(1.5.dp, Color.White, CircleShape)
                .background(Color.Black.copy(alpha = 0.35f), CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Clear image",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }

        // Bottom Gradient Sheet
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.90f),
                            Color(0xFF5A524C),
                            PeachSurface
                        ),
                        startY = 0f,
                        endY = 480f
                    )
                )
                .padding(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header + Gallery Upload Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Food Scanner",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = if (hasImage) "Ready to Analyze" else "Take a photo or upload image",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(onClick = onGalleryClick) {
                    GalleryCanvasIcon(
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            // Error Message Banner
            errorMessage?.let { error ->
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.errorContainer
                ) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }
            }

            // Calorie Count Pill Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(text = "🔥", fontSize = 24.sp)
                    Column {
                        Text(
                            text = "Calorie Count",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = if (hasImage) "Tap Analyze Food below" else "-- kcal",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }

            // 3 Macro Mini Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ScanMacroMiniCard(
                    value = "-- g",
                    label = "Protein",
                    progress = if (hasImage) 0.5f else 0.15f,
                    modifier = Modifier.weight(1f)
                )
                ScanMacroMiniCard(
                    value = "-- g",
                    label = "Carbs",
                    progress = if (hasImage) 0.5f else 0.15f,
                    modifier = Modifier.weight(1f)
                )
                ScanMacroMiniCard(
                    value = "-- g",
                    label = "Fat",
                    progress = if (hasImage) 0.5f else 0.15f,
                    modifier = Modifier.weight(1f)
                )
            }

            // Bottom Action Buttons ("Analyze Food" + "Take a Photo / Re-Scan")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = onAnalyzeClick,
                    enabled = hasImage && !isLoading,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = CircleShape,
                    border = BorderStroke(
                        1.2.dp,
                        if (hasImage && !isLoading) Color.Black else Color.Black.copy(alpha = 0.35f)
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black,
                        disabledContentColor = Color.Black.copy(alpha = 0.35f)
                    )
                ) {
                    Text(
                        text = if (isLoading) "Analyzing..." else "Analyze Food",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Spacer(Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier
                            .size(18.dp)
                            .background(
                                if (hasImage && !isLoading) Color.Black else Color.Black.copy(alpha = 0.35f),
                                CircleShape
                            )
                            .padding(2.dp),
                        tint = Color.White
                    )
                }

                OutlinedButton(
                    onClick = onCameraClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = CircleShape,
                    border = BorderStroke(1.2.dp, Color.Black),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = if (hasImage) "Re-Scan" else "Take a Photo",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Spacer(Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = Color.Black
                    )
                }
            }
        }
    }
}

// ==========================================
// 2. SCANNED RESULTS PAGE UI
// ==========================================
@Composable
private fun ScannedResultsPageUI(
    result: PredictionResponse,
    selectedImageUri: Uri?,
    capturedImage: Bitmap?,
    onCloseClick: () -> Unit,
    onInfoClick: () -> Unit,
    onUpdateDetailsClick: () -> Unit,
    onConfirmClick: () -> Unit,
    onCameraFabClick: () -> Unit
) {
    val details = result.nutrition
    val nutrients = details?.nutrition

    val formattedFoodName = (details?.food_name ?: result.predicted_food)
        .replace("_", " ")
        .replaceFirstChar { it.uppercase() }

    val caloriesText = nutrients?.calories?.let { "%.0f kcal".format(it) } ?: "Unavailable"
    val carbsText = nutrients?.carbohydrates?.let { "%.1fg".format(it) } ?: "--"
    val proteinText = nutrients?.protein?.let { "%.1fg".format(it) } ?: "--"
    val fatText = nutrients?.fat?.let { "%.1fg".format(it) } ?: "--"

    // Derive score from confidence (scaled to 1..10)
    val scoreOutOfTen = (result.confidence / 10.0).toInt().coerceIn(1, 10)

    Scaffold(
        bottomBar = {
            BottomNavWithCameraFab(onCameraClick = onCameraFabClick)
        },
        containerColor = PeachSurface
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            // Top Image Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.38f)
            ) {
                FoodImagePreview(
                    selectedImageUri = selectedImageUri,
                    capturedImage = capturedImage,
                    placeholderText = formattedFoodName,
                    modifier = Modifier.fillMaxSize()
                )

                IconButton(
                    onClick = onCloseClick,
                    modifier = Modifier
                        .padding(top = 36.dp, start = 18.dp)
                        .size(36.dp)
                        .border(1.5.dp, Color.White, CircleShape)
                        .background(Color.Black.copy(alpha = 0.25f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Gradient Body
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.62f)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF7A7673),
                                Color(0xFFB5ABA4),
                                PeachSurface
                            )
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Title & Info Icon
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formattedFoodName,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = onInfoClick,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Prediction Info",
                            tint = Color(0xFF2B2B2B),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // Total Calories Pill Card
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(18.dp)),
                    shape = RoundedCornerShape(18.dp),
                    color = Color.White
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total $caloriesText",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(text = "🔥", fontSize = 26.sp)
                    }
                }

                // 3 Circular Macro Rings
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MacroRingCircle(grams = carbsText, label = "Carbs", ringColor = OrangeAccent)
                    MacroRingCircle(grams = proteinText, label = "Proteins", ringColor = CyanMacro)
                    MacroRingCircle(grams = fatText, label = "Fats", ringColor = LimeMacro)
                }

                // Confidence / Health Score Card
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(18.dp)),
                    shape = RoundedCornerShape(18.dp),
                    color = Color.White
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Confidence Score",
                            tint = Color(0xFFE51C23),
                            modifier = Modifier.size(26.dp)
                        )
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Confidence: %.2f%%".format(result.confidence),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            LinearProgressIndicator(
                                progress = { (result.confidence.toFloat() / 100f).coerceIn(0f, 1f) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = HealthGreen,
                                trackColor = Color(0xFFE8E8E8)
                            )
                        }
                        Text(
                            text = "$scoreOutOfTen/10",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }

                // Update Details & Confirm Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Button(
                        onClick = onUpdateDetailsClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .shadow(3.dp, CircleShape),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            text = "Update Details",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Button(
                        onClick = onConfirmClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Confirm",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// DETAILS DIALOG (Serving Size & Top Predictions)
// ==========================================
@Composable
private fun PredictionDetailsDialog(
    result: PredictionResponse,
    onDismiss: () -> Unit
) {
    val details = result.nutrition
    val nutrients = details?.nutrition

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Nutrition & Predictions",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = result.predicted_food
                        .replace("_", " ")
                        .replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Confidence: %.2f%%".format(result.confidence),
                    style = MaterialTheme.typography.bodyMedium
                )

                if (details != null) {
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
                            text = "Serving size: $servingText",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    HorizontalDivider()

                    if (nutrients != null) {
                        NutrientDetailRow("Calories", nutrients.calories, "kcal")
                        NutrientDetailRow("Protein", nutrients.protein, "g")
                        NutrientDetailRow("Carbohydrates", nutrients.carbohydrates, "g")
                        NutrientDetailRow("Fat", nutrients.fat, "g")
                    } else {
                        Text("Nutrient values are unavailable.")
                    }
                } else {
                    Text("Nutrition information could not be retrieved.")
                }

                if (result.top_predictions.isNotEmpty()) {
                    HorizontalDivider()
                    Text(
                        text = "Top Predictions",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
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
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = OrangeAccent, fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
private fun NutrientDetailRow(
    label: String,
    value: Double?,
    unit: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label)
        Text(
            text = if (value != null) "%.2f %s".format(value, unit) else "Unavailable",
            fontWeight = FontWeight.SemiBold
        )
    }
}

// ==========================================
// REUSABLE UI COMPONENTS & CUSTOM CANVAS ICONS
// ==========================================
@Composable
private fun FoodImagePreview(
    selectedImageUri: Uri?,
    capturedImage: Bitmap?,
    placeholderText: String,
    modifier: Modifier = Modifier
) {
    when {
        selectedImageUri != null -> {
            AsyncImage(
                model = selectedImageUri,
                contentDescription = "Selected food image",
                modifier = modifier,
                contentScale = ContentScale.Crop
            )
        }
        capturedImage != null -> {
            Image(
                bitmap = capturedImage.asImageBitmap(),
                contentDescription = "Captured food image",
                modifier = modifier,
                contentScale = ContentScale.Crop
            )
        }
        else -> {
            Box(
                modifier = modifier.background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF45403B),
                            Color(0xFF2B2623),
                            Color(0xFF1A1816)
                        ),
                        radius = 750f
                    )
                ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = placeholderText,
                    color = Color.White.copy(alpha = 0.75f),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(24.dp)
                )
            }
        }
    }
}

@Composable
private fun MacroRingCircle(
    grams: String,
    label: String,
    ringColor: Color
) {
    Box(
        modifier = Modifier
            .size(96.dp)
            .shadow(4.dp, CircleShape)
            .background(Color.White, CircleShape)
            .border(8.dp, ringColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = grams,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.DarkGray
            )
        }
    }
}

@Composable
private fun ScanMacroMiniCard(
    value: String,
    label: String,
    progress: Float,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.shadow(2.dp, RoundedCornerShape(10.dp)),
        shape = RoundedCornerShape(10.dp),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = label,
                fontSize = 11.sp,
                color = Color.Gray
            )
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = OrangeAccent,
                trackColor = Color(0xFFE5E5E5)
            )
        }
    }
}

@Composable
private fun BottomNavWithCameraFab(onCameraClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(78.dp)
            .background(Color.Transparent)
    ) {
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(62.dp),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavBarItem(icon = Icons.Default.Home, label = "Home", selected = false)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    HistoryCanvasIcon(tint = Color.Gray, modifier = Modifier.size(22.dp))
                    Text(text = "History", fontSize = 10.sp, color = Color.Gray)
                }

                Spacer(modifier = Modifier.width(48.dp))

                NavBarItem(icon = Icons.Default.FavoriteBorder, label = "Favourites", selected = false)
                NavBarItem(icon = Icons.Default.Person, label = "Profile", selected = true)
            }
        }

        // Center Floating Orange Camera Button
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(54.dp)
                .shadow(6.dp, CircleShape)
                .background(OrangeAccent, CircleShape)
                .clickable { onCameraClick() },
            contentAlignment = Alignment.Center
        ) {
            CameraCanvasIcon(
                tint = Color.White,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}

@Composable
private fun NavBarItem(
    icon: ImageVector,
    label: String,
    selected: Boolean
) {
    val tint = if (selected) OrangeAccent else Color.Gray
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = tint,
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = tint,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
private fun GalleryCanvasIcon(tint: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val stroke = 2.dp.toPx()
        drawRoundRect(
            color = tint,
            size = size,
            cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx()),
            style = Stroke(width = stroke)
        )
        drawCircle(
            color = tint,
            radius = size.minDimension * 0.12f,
            center = Offset(size.width * 0.32f, size.height * 0.32f)
        )
        val mountainPath = Path().apply {
            moveTo(size.width * 0.15f, size.height * 0.8f)
            lineTo(size.width * 0.42f, size.height * 0.52f)
            lineTo(size.width * 0.62f, size.height * 0.72f)
            lineTo(size.width * 0.75f, size.height * 0.58f)
            lineTo(size.width * 0.88f, size.height * 0.8f)
        }
        drawPath(mountainPath, color = tint, style = Stroke(width = stroke, cap = StrokeCap.Round))
    }
}

@Composable
private fun CameraCanvasIcon(tint: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val stroke = 2.dp.toPx()
        val bodyTop = size.height * 0.28f
        val bodyHeight = size.height * 0.58f
        drawRoundRect(
            color = tint,
            topLeft = Offset(size.width * 0.1f, bodyTop),
            size = Size(size.width * 0.8f, bodyHeight),
            cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx()),
            style = Stroke(width = stroke)
        )
        drawRoundRect(
            color = tint,
            topLeft = Offset(size.width * 0.34f, size.height * 0.16f),
            size = Size(size.width * 0.32f, size.height * 0.14f),
            cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx()),
            style = Stroke(width = stroke)
        )
        drawCircle(
            color = tint,
            radius = size.minDimension * 0.17f,
            center = Offset(size.width * 0.5f, bodyTop + bodyHeight * 0.52f),
            style = Stroke(width = stroke)
        )
    }
}

@Composable
private fun HistoryCanvasIcon(tint: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val stroke = 1.8.dp.toPx()
        val center = Offset(size.width / 2f, size.height / 2f)
        val radius = size.minDimension * 0.42f
        drawCircle(color = tint, radius = radius, center = center, style = Stroke(width = stroke))
        drawLine(
            color = tint,
            start = center,
            end = Offset(center.x, center.y - radius * 0.55f),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
        drawLine(
            color = tint,
            start = center,
            end = Offset(center.x + radius * 0.45f, center.y + radius * 0.25f),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun ScannerBracketOverlay(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val strokeWidth = 10.dp.toPx()
        val cornerLength = 54.dp.toPx()
        val radius = 24.dp.toPx()
        val w = size.width
        val h = size.height

        val path = Path().apply {
            moveTo(0f, cornerLength)
            lineTo(0f, radius)
            arcTo(Rect(Offset.Zero, Size(radius * 2, radius * 2)), 180f, 90f, false)
            lineTo(cornerLength, 0f)

            moveTo(w - cornerLength, 0f)
            lineTo(w - radius, 0f)
            arcTo(Rect(Offset(w - radius * 2, 0f), Size(radius * 2, radius * 2)), 270f, 90f, false)
            lineTo(w, cornerLength)

            moveTo(0f, h - cornerLength)
            lineTo(0f, h - radius)
            arcTo(Rect(Offset(0f, h - radius * 2), Size(radius * 2, radius * 2)), 180f, -90f, false)
            lineTo(cornerLength, h)

            moveTo(w - cornerLength, h)
            lineTo(w - radius, h)
            arcTo(Rect(Offset(w - radius * 2, h - radius * 2), Size(radius * 2, radius * 2)), 90f, -90f, false)
            lineTo(w, h - cornerLength)
        }

        drawPath(
            path = path,
            color = Color.Black,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FoodScanner2Preview(modifier: Modifier = Modifier) {
    FoodScannerScreen2()
}