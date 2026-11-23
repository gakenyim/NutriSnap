package com.example.nutrisnap.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nutrisnap.data.SampleData
import com.example.nutrisnap.data.models.DailyNutritionSummary
import com.example.nutrisnap.data.models.Meal
import com.example.nutrisnap.data.models.toDailySummary
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale

private val BrandOrange = Color(0xFFFF7A00)

@Composable
fun HistoryScreen(
    meals: List<Meal>,
    dailyCalorieGoal: Int = 2500,
    onToggleFavourite: (Meal) -> Unit,
    onEditMeal: (Meal) -> Unit,
    onDeleteMeal: (Meal) -> Unit,
    modifier: Modifier = Modifier
) {
    val today = remember { LocalDate.now() }
    val weekDates = remember { (6L downTo 0L).map { today.minusDays(it) } }
    var selectedDate by remember { mutableStateOf(today) }
    var mealBeingEdited by remember { mutableStateOf<Meal?>(null) }

    val mealsForSelectedDay = meals.filter { meal ->
        val mealDate = Instant.ofEpochMilli(meal.timestamp)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        mealDate == selectedDate
    }

    // Reuses your DailyNutritionSummary helper for whichever day is tapped
    val daySummary = meals.toDailySummary(selectedDate)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "History",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        DateStrip(
            dates = weekDates,
            selectedDate = selectedDate,
            onDateSelected = { selectedDate = it }
        )

        DailyCaloriesCard(
            summary = daySummary,
            goalKcal = dailyCalorieGoal
        )

        Text(
            text = if (selectedDate == today) "Today's Activity"
            else "Activity for ${selectedDate.dayOfMonth} ${selectedDate.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        if (mealsForSelectedDay.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No meals logged for this day yet.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
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

@Composable
private fun DateStrip(
    dates: List<LocalDate>,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        dates.forEach { date ->
            val isSelected = date == selectedDate
            val dayLabel = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()).take(2)

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isSelected) BrandOrange else Color(0xFFF4F4F4))
                    .clickable { onDateSelected(date) }
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = dayLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isSelected) Color.White else Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = date.dayOfMonth.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color.White else Color.Black
                )
            }
        }
    }
}

@Composable
private fun DailyCaloriesCard(
    summary: DailyNutritionSummary,
    goalKcal: Int
) {
    val progress = (summary.totalKcal.toFloat() / goalKcal.toFloat()).coerceIn(0f, 1f)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF6EE))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Today Calories",
                style = MaterialTheme.typography.labelLarge,
                color = Color.DarkGray
            )
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = BrandOrange,
                trackColor = Color(0xFFFFDFC2)
            )
            Text(
                text = "${summary.totalKcal} / $goalKcal kcal",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Protein: ${summary.totalProtein}g  •  Carbs: ${summary.totalCarbs}g  •  Fat: ${summary.totalFat}g",
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray
            )
        }
    }
}

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
        onEditMeal = { updated ->
            previewMeals = previewMeals.map {
                if (it.id == updated.id) updated else it
            }
        },
        onDeleteMeal = { deleted ->
            previewMeals = previewMeals.filterNot { it.id == deleted.id }
        }
    )
}