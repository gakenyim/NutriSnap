package com.example.nutrisnap.ui.features.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nutrisnap.data.SampleData
import com.example.nutrisnap.model.Meal
import com.example.nutrisnap.ui.components.MealCard

@Composable
fun MealListScreen(
    title: String,
    meals: List<Meal>,
    onToggleFavourite: (Meal) -> Unit,
    onEditMeal: (Meal) -> Unit,
    onDeleteMeal: (Meal) -> Unit,
    showDeleteButton: Boolean = true,
    modifier: Modifier = Modifier
) {
    val brandOrange = Color(0xFFFF7A00)

    var searchQuery by remember { mutableStateOf("") }
    val filters = listOf("All", "High Protein", "Low Calorie")
    var selectedFilter by remember { mutableStateOf("All") }
    var mealBeingEdited by remember { mutableStateOf<Meal?>(null) }

    val filteredMeals = meals.filter { meal ->
        val matchesSearch = meal.name.contains(searchQuery.trim(), ignoreCase = true)
        val matchesFilter = when (selectedFilter) {
            "High Protein" -> meal.protein >= 25
            "Low Calorie" -> meal.kcal <= 450
            else -> true
        }
        matchesSearch && matchesFilter
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 44.dp, bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search meals...") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filters) { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter },
                    label = { Text(filter) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = brandOrange,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredMeals, key = { it.id }) { meal ->
                MealCard(
                    meal = meal,
                    onMealClick = { mealBeingEdited = meal },
                    onToggleFavourite = onToggleFavourite,
                    onDeleteMeal = onDeleteMeal,
                    showDeleteButton = showDeleteButton
                )
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MealListScreenPreview() {
    var previewMeals by remember { mutableStateOf(SampleData.sampleMeals) }

    MealListScreen(
        title = "Recent Meals",
        meals = previewMeals,
        onToggleFavourite = { clickedMeal ->
            previewMeals = previewMeals.map {
                if (it.id == clickedMeal.id) it.copy(isFavourite = !it.isFavourite) else it
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