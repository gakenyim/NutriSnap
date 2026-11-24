package com.example.nutrisnap.ui.features.scanner

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun ScanPageContent(
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
                .statusBarsPadding()
                .padding(top = 16.dp, start = 20.dp)
                .size(36.dp)
                .border(1.5.dp, Color.White, CircleShape)
                .background(Color.Black.copy(alpha = 0.35f), CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
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
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 20.dp,
                    bottom = WindowInsets.navigationBars
                        .asPaddingValues()
                        .calculateBottomPadding()
                        .coerceAtLeast(12.dp)
                ),
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