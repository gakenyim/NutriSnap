package com.example.nutrisnap.ui.favourites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nutrisnap.data.SampleData
import com.example.nutrisnap.data.models.Meal
import com.example.nutrisnap.ui.history.MealListScreen

@Composable
fun FavouritesScreen(
    meals: List<Meal>,
    onToggleFavourite: (Meal) -> Unit,
    onEditMeal: (Meal) -> Unit,
    onDeleteMeal: (Meal) -> Unit,
    modifier: Modifier = Modifier
) {
    val syncedFavourites = meals.filter { it.isFavourite }

    if (syncedFavourites.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(start = 24.dp, end = 24.dp, top = 28.dp, bottom = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Favourites",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "No favourite meals yet. Tap the heart on a meal to sync it here.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    } else {
        MealListScreen(
            title = "Favourites",
            meals = syncedFavourites,
            onToggleFavourite = onToggleFavourite,
            onEditMeal = onEditMeal,
            onDeleteMeal = onDeleteMeal,
            showDeleteButton = false,
            modifier = modifier
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun FavouritesScreenPreview() {
    var previewMeals by remember { mutableStateOf(SampleData.sampleMeals) }

    FavouritesScreen(
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