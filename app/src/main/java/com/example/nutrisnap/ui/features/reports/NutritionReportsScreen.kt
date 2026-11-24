package com.example.nutrisnap.ui.features.reports

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutrisnap.data.SampleData
import com.example.nutrisnap.model.Meal
import java.text.NumberFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import kotlin.math.roundToInt

private val BrandOrange = Color(0xFFFF7A00)
private val BrandOrangeSoft = Color(0xFFFFF3E8)
private val CanvasBackground = Color(0xFFF8F9FA)
private val TextPrimary = Color(0xFF141414)
private val TextSecondary = Color(0xFF6B7280)
private val CardBorder = Color(0xFFECEEF2)

private val ProteinBlue = Color(0xFF3B82F6)
private val FatGreen = Color(0xFF10B981)

private enum class ReportRange(val label: String) {
    TODAY("Today"),
    WEEK("Last 7 Days")
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NutritionReportsScreen(
    meals: List<Meal>,
    favouriteMealsCount: Int,
    dailyCalorieGoal: Int = 2500,
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedRange by remember { mutableStateOf(ReportRange.TODAY) }
    val today = remember { LocalDate.now() }
    val sevenDaysAgo = remember(today) { today.minusDays(6) }

    // Filter meals dynamically based on selected time range
    val filteredMeals = remember(meals, selectedRange, today) {
        meals.filter { meal ->
            val mealDate = Instant.ofEpochMilli(meal.timestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            when (selectedRange) {
                ReportRange.TODAY -> mealDate == today
                ReportRange.WEEK -> !mealDate.isBefore(sevenDaysAgo) && !mealDate.isAfter(today)
            }
        }
    }

    val totalKcal = filteredMeals.sumOf { it.kcal }
    val totalProtein = filteredMeals.sumOf { it.protein }
    val totalCarbs = filteredMeals.sumOf { it.carbs }
    val totalFat = filteredMeals.sumOf { it.fat }
    val avgHealthScore = if (filteredMeals.isNotEmpty()) {
        filteredMeals.map { it.healthScore }.average().roundToInt()
    } else {
        0
    }

    val targetKcal = if (selectedRange == ReportRange.TODAY) dailyCalorieGoal else dailyCalorieGoal * 7
    val progress = (totalKcal.toFloat() / targetKcal.coerceAtLeast(1).toFloat()).coerceIn(0f, 1f)
    val formattedKcal = remember(totalKcal) { NumberFormat.getIntegerInstance().format(totalKcal) }
    val formattedTarget = remember(targetKcal) { NumberFormat.getIntegerInstance().format(targetKcal) }

    // Macro calorie share for progress bars
    val totalMacroKcal = ((totalProtein * 4) + (totalCarbs * 4) + (totalFat * 9)).coerceAtLeast(1)

    Surface(
        modifier = modifier.fillMaxSize(),
        color = CanvasBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
                .padding(top = 14.dp)
        ) {
            // Top Header matching HistoryScreen
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        onClick = onBackClick,
                        shape = CircleShape,
                        color = Color.White,
                        border = BorderStroke(1.dp, CardBorder),
                        shadowElevation = 2.dp
                    ) {
                        Box(
                            modifier = Modifier.padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                                contentDescription = "Back",
                                tint = TextPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "Nutrition Reports",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = if (selectedRange == ReportRange.TODAY) {
                                "Today's intake & macro breakdown"
                            } else {
                                "7-day cumulative nutrition overview"
                            },
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Range Segmented Toggle (Today vs Last 7 Days)
            RangeSelectorRow(
                selectedRange = selectedRange,
                onRangeSelected = { selectedRange = it }
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Scrollable Report Cards (padded at bottom so MainScreen's BottomNavBar doesn't cover content)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Hero Calorie Card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = BrandOrange,
                    shadowElevation = 3.dp
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (selectedRange == ReportRange.TODAY) {
                                    "Today's Nutrition"
                                } else {
                                    "7-Day Nutrition"
                                },
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White.copy(alpha = 0.9f)
                            )

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color.White.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "${(progress * 100).roundToInt()}% of goal",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = formattedKcal,
                                fontSize = 34.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = " / $formattedTarget kcal",
                                fontSize = 13.sp,
                                color = Color.White.copy(alpha = 0.85f),
                                modifier = Modifier.padding(bottom = 5.dp)
                            )
                        }

                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = Color.White,
                            trackColor = Color.White.copy(alpha = 0.25f),
                            strokeCap = StrokeCap.Round
                        )

                        Text(
                            text = if (filteredMeals.isEmpty()) {
                                "No meals logged in this period yet"
                            } else {
                                "Calculated from ${filteredMeals.size} recorded meal${if (filteredMeals.size == 1) "" else "s"}"
                            },
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }

                // Section Title: Macronutrient Breakdown
                Text(
                    text = "Macronutrient Breakdown",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                NutrientCard(
                    name = "Protein",
                    amountGrams = totalProtein,
                    share = if (filteredMeals.isEmpty()) 0f else (totalProtein * 4f) / totalMacroKcal,
                    description = "Muscle growth and tissue repair",
                    accentColor = ProteinBlue
                )

                NutrientCard(
                    name = "Carbohydrates",
                    amountGrams = totalCarbs,
                    share = if (filteredMeals.isEmpty()) 0f else (totalCarbs * 4f) / totalMacroKcal,
                    description = "Your body's primary energy source",
                    accentColor = BrandOrange
                )

                NutrientCard(
                    name = "Fats",
                    amountGrams = totalFat,
                    share = if (filteredMeals.isEmpty()) 0f else (totalFat * 9f) / totalMacroKcal,
                    description = "Supports hormones and nutrient absorption",
                    accentColor = FatGreen
                )

                // Section Title: Activity Summary
                Text(
                    text = "Activity Summary",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SummaryCard(
                        title = if (selectedRange == ReportRange.TODAY) "Scanned Today" else "Scanned (7d)",
                        value = filteredMeals.size.toString(),
                        modifier = Modifier.weight(1f)
                    )
                    SummaryCard(
                        title = "Saved Meals",
                        value = favouriteMealsCount.toString(),
                        modifier = Modifier.weight(1f)
                    )
                    SummaryCard(
                        title = "Avg Score",
                        value = if (filteredMeals.isEmpty()) "--" else "$avgHealthScore",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(96.dp))
            }
        }
    }
}

@Composable
private fun RangeSelectorRow(
    selectedRange: ReportRange,
    onRangeSelected: (ReportRange) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        border = BorderStroke(1.dp, CardBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            ReportRange.entries.forEach { range ->
                val isSelected = range == selectedRange
                val bgColor by animateColorAsState(
                    targetValue = if (isSelected) BrandOrange else Color.Transparent,
                    label = "rangeBg"
                )
                val textColor by animateColorAsState(
                    targetValue = if (isSelected) Color.White else TextSecondary,
                    label = "rangeText"
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(bgColor)
                        .clickable { onRangeSelected(range) }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = range.label,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = textColor
                    )
                }
            }
        }
    }
}

@Composable
private fun NutrientCard(
    name: String,
    amountGrams: Int,
    share: Float,
    description: String,
    accentColor: Color
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        border = BorderStroke(1.dp, CardBorder),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(accentColor)
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = description,
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = accentColor.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = "${amountGrams}g",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = accentColor
                    )
                }
            }

            LinearProgressIndicator(
                progress = { share.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = accentColor,
                trackColor = accentColor.copy(alpha = 0.15f),
                strokeCap = StrokeCap.Round
            )
        }
    }
}

@Composable
private fun SummaryCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        border = BorderStroke(1.dp, CardBorder),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = BrandOrange
            )
            Text(
                text = title,
                fontSize = 11.sp,
                color = TextSecondary
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NutritionReportsScreenPreview() {
    NutritionReportsScreen(
        meals = SampleData.sampleMeals,
        favouriteMealsCount = SampleData.sampleMeals.count { it.isFavourite }
    )
}