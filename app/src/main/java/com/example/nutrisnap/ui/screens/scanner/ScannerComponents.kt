package com.example.nutrisnap.ui.screens.scanner

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import com.example.nutrisnap.PredictionResponse

// Shared Color Palette
internal val PeachSurface = Color(0xFFFBECE2)
internal val OrangeAccent = Color(0xFFFF8C24)
internal val CyanMacro = Color(0xFF42C2F5)
internal val LimeMacro = Color(0xFF8CE67E)
internal val HealthGreen = Color(0xFF5BD43B)

@Composable
internal fun FoodImagePreview(
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
internal fun ScannerBracketOverlay(modifier: Modifier = Modifier) {
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

@Composable
internal fun ScanMacroMiniCard(
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
internal fun MacroRingCircle(
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
internal fun GalleryCanvasIcon(tint: Color, modifier: Modifier = Modifier) {
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
internal fun PredictionDetailsDialog(
    result: PredictionResponse,
    onDismiss: () -> Unit
) {
    val details = result.nutrition
    val nutrients = details?.nutrition

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Nutrition & Predictions", fontWeight = FontWeight.Bold)
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
                            Text(text = "%.2f%%".format(prediction.confidence))
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
private fun NutrientDetailRow(label: String, value: Double?, unit: String) {
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
