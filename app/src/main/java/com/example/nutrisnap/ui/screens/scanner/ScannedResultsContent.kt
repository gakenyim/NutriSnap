package com.example.nutrisnap.ui.screens.scanner

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutrisnap.PredictionResponse

@Composable
internal fun ScannedResultsContent(
    result: PredictionResponse,
    selectedImageUri: Uri?,
    capturedImage: Bitmap?,
    onCloseClick: () -> Unit,
    onInfoClick: () -> Unit,
    onUpdateDetailsClick: () -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier
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
    val scoreOutOfTen = (result.confidence / 10.0).toInt().coerceIn(1, 10)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PeachSurface)
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
                    .statusBarsPadding()
                    .padding(top = 12.dp, start = 18.dp)
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

            // Confidence Score Card
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