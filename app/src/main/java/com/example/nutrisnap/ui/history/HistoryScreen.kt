package com.example.nutrisnap.ui.history

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutrisnap.data.SampleData
import com.example.nutrisnap.data.models.Meal
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

private val ScannerOrange = Color(0xFFFF7A00)
private val ScannerOrangeSoft = Color(0xFFFFF3E8)
private val CanvasBackground = Color(0xFFF8F9FA)
private val TextPrimary = Color(0xFF141414)
private val TextSecondary = Color(0xFF6B7280)
private val CardBorder = Color(0xFFECEEF2)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistoryScreen(
    meals: List<Meal>,
    dailyCalorieGoal: Int = 2500,
    onToggleFavourite: (Meal) -> Unit,
    onEditMeal: (Meal) -> Unit = {},
    onDeleteMeal: (Meal) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val today = remember { LocalDate.now() }
    val weekDates = remember { (6L downTo 0L).map { today.minusDays(it) } }
    var selectedDate by remember { mutableStateOf(today) }
    var mealBeingEdited by remember { mutableStateOf<Meal?>(null) }

    val mealsForSelectedDay = remember(meals, selectedDate) {
        meals.filter { meal ->
            val mealDate = Instant.ofEpochMilli(meal.timestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            mealDate == selectedDate
        }
    }

    val totalKcal = mealsForSelectedDay.sumOf { it.kcal }
    val totalProtein = mealsForSelectedDay.sumOf { it.protein }
    val totalCarbs = mealsForSelectedDay.sumOf { it.carbs }
    val totalFat = mealsForSelectedDay.sumOf { it.fat }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = CanvasBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HistoryHeader(selectedDate = selectedDate, today = today)

            DateStrip(
                dates = weekDates,
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it }
            )

            DailyCaloriesCard(
                isToday = selectedDate == today,
                consumedKcal = totalKcal,
                goalKcal = dailyCalorieGoal,
                protein = totalProtein,
                carbs = totalCarbs,
                fat = totalFat
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (selectedDate == today) {
                        "Today's Meals"
                    } else {
                        "Meals • ${selectedDate.format(DateTimeFormatter.ofPattern("MMM d"))}"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                if (mealsForSelectedDay.isNotEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = ScannerOrangeSoft
                    ) {
                        Text(
                            text = "${mealsForSelectedDay.size} logged",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = ScannerOrange
                        )
                    }
                }
            }

            if (mealsForSelectedDay.isEmpty()) {
                EmptyHistoryCard(isToday = selectedDate == today)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(mealsForSelectedDay, key = { it.id }) { meal ->
                        MealCard(
                            meal = meal,
                            onMealClick = { mealBeingEdited = it },
                            onToggleFavourite = onToggleFavourite,
                            onDeleteMeal = onDeleteMeal
                        )
                    }
                }
            }
        }
    }

    mealBeingEdited?.let { selectedMeal ->
        EditMealDialog(
            meal = selectedMeal,
            onDismiss = { mealBeingEdited = null },
            onSave = { updatedMeal ->
                onEditMeal(updatedMeal)
                mealBeingEdited = null
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun HistoryHeader(
    selectedDate: LocalDate,
    today: LocalDate
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = "Nutrition History",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = if (selectedDate == today) {
                    "Today, ${selectedDate.format(DateTimeFormatter.ofPattern("MMMM d"))}"
                } else {
                    selectedDate.format(DateTimeFormatter.ofPattern("EEEE, MMMM d"))
                },
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }

        Surface(
            shape = CircleShape,
            color = Color.White,
            border = BorderStroke(1.dp, CardBorder),
            shadowElevation = 2.dp
        ) {
            Box(
                modifier = Modifier.padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.DateRange,
                    contentDescription = "Calendar",
                    tint = ScannerOrange,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun DateStrip(
    dates: List<LocalDate>,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        dates.forEach { date ->
            val isSelected = date == selectedDate
            val dayLabel = date.dayOfWeek
                .getDisplayName(TextStyle.SHORT, Locale.getDefault())
                .take(3)

            val containerColor by animateColorAsState(
                targetValue = if (isSelected) ScannerOrange else Color.White,
                label = "dateChipBg"
            )
            val contentColor by animateColorAsState(
                targetValue = if (isSelected) Color.White else TextPrimary,
                label = "dateChipText"
            )

            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onDateSelected(date) },
                shape = RoundedCornerShape(16.dp),
                color = containerColor,
                border = if (isSelected) null else BorderStroke(1.dp, CardBorder),
                shadowElevation = if (isSelected) 4.dp else 1.dp
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = dayLabel,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium,
                        color = if (isSelected) Color.White.copy(alpha = 0.85f) else TextSecondary
                    )
                    Text(
                        text = date.dayOfMonth.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = contentColor
                    )
                }
            }
        }
    }
}

@Composable
private fun DailyCaloriesCard(
    isToday: Boolean,
    consumedKcal: Int,
    goalKcal: Int,
    protein: Int,
    carbs: Int,
    fat: Int
) {
    val progress = (consumedKcal.toFloat() / goalKcal.toFloat()).coerceIn(0f, 1f)
    val remainingKcal = (goalKcal - consumedKcal).coerceAtLeast(0)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        border = BorderStroke(1.dp, CardBorder),
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = if (isToday) "Calories Consumed" else "Daily Intake",
                        style = MaterialTheme.typography.labelLarge,
                        color = TextSecondary
                    )
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "$consumedKcal",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = " / $goalKcal kcal",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = ScannerOrangeSoft
                ) {
                    Text(
                        text = "$remainingKcal left",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = ScannerOrange
                    )
                }
            }

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp)),
                color = ScannerOrange,
                trackColor = ScannerOrangeSoft,
                strokeCap = StrokeCap.Round
            )

            HorizontalDivider(color = CardBorder, thickness = 1.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MacroBadge(
                    label = "Protein",
                    grams = protein,
                    accentColor = Color(0xFF3B82F6),
                    modifier = Modifier.weight(1f)
                )
                MacroBadge(
                    label = "Carbs",
                    grams = carbs,
                    accentColor = ScannerOrange,
                    modifier = Modifier.weight(1f)
                )
                MacroBadge(
                    label = "Fat",
                    grams = fat,
                    accentColor = Color(0xFF10B981),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun MacroBadge(
    label: String,
    grams: Int,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = CanvasBackground
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(accentColor)
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
            }
            Text(
                text = "${grams}g",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
private fun EmptyHistoryCard(isToday: Boolean) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        border = BorderStroke(1.dp, CardBorder),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = ScannerOrangeSoft
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = null,
                    tint = ScannerOrange,
                    modifier = Modifier
                        .padding(14.dp)
                        .size(28.dp)
                )
            }
            Text(
                text = if (isToday) "No meals scanned today" else "No meals recorded",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = if (isToday) {
                    "Use the food scanner to snap a meal and track your daily macros."
                } else {
                    "There were no food logs saved on this date."
                },
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HistoryScreenPreview() {
    var previewMeals by remember { mutableStateOf(SampleData.sampleMeals) }
    HistoryScreen(
        meals = previewMeals,
        onToggleFavourite = { clicked ->
            previewMeals = previewMeals.map {
                if (it.id == clicked.id) it.copy(isFavourite = !it.isFavourite) else it
            }
        },
        onEditMeal = { updatedMeal ->
            previewMeals = previewMeals.map {
                if (it.id == updatedMeal.id) updatedMeal else it
            }
        },
        onDeleteMeal = { mealToDelete ->
            previewMeals = previewMeals.filterNot { it.id == mealToDelete.id }
        }
    )
}