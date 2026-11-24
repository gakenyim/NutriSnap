package com.example.nutrisnap.ui.favourites

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutrisnap.data.SampleData
import com.example.nutrisnap.data.models.Meal
import com.example.nutrisnap.ui.history.EditMealDialog

private val BrandOrange = Color(0xFFFF7A00)
private val BrandOrangeSoft = Color(0xFFFFF3E8)
private val CanvasBackground = Color(0xFFF8F9FA)
private val DarkText = Color(0xFF141414)
private val GreyText = Color(0xFF6B7280)
private val CardBorder = Color(0xFFECEEF2)

@Composable
fun FavouritesScreen(
    meals: List<Meal>,
    onToggleFavourite: (Meal) -> Unit,
    onEditMeal: (Meal) -> Unit,
    onDeleteMeal: (Meal) -> Unit,
    modifier: Modifier = Modifier
) {
    val syncedFavourites = remember(meals) { meals.filter { it.isFavourite } }
    var mealBeingEdited by remember { mutableStateOf<Meal?>(null) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = CanvasBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = "Saved Meals",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                    Text(
                        text = "Your favourite meals in one place",
                        style = MaterialTheme.typography.bodySmall,
                        color = GreyText
                    )
                }

                if (syncedFavourites.isNotEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = BrandOrangeSoft
                    ) {
                        Text(
                            text = "${syncedFavourites.size} saved",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = BrandOrange
                        )
                    }
                }
            }

            if (syncedFavourites.isEmpty()) {
                EmptyFavouritesCard()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(syncedFavourites, key = { it.id }) { meal ->
                        FavouriteMealCard(
                            meal = meal,
                            onMealClick = { mealBeingEdited = it },
                            onToggleFavourite = { onToggleFavourite(meal) }
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

@Composable
private fun FavouriteMealCard(
    meal: Meal,
    onMealClick: (Meal) -> Unit,
    onToggleFavourite: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onMealClick(meal) },
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        border = BorderStroke(1.dp, CardBorder),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = meal.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "${meal.kcal} kcal",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BrandOrange
                    )
                    Text(
                        text = "•  ${meal.protein}g P  •  ${meal.carbs}g C  •  ${meal.fat}g F",
                        fontSize = 12.sp,
                        color = GreyText
                    )
                }
            }

            IconButton(onClick = onToggleFavourite) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Remove from favourites",
                    tint = BrandOrange,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun EmptyFavouritesCard() {
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
                color = BrandOrangeSoft
            ) {
                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                    tint = BrandOrange,
                    modifier = Modifier
                        .padding(14.dp)
                        .size(28.dp)
                )
            }
            Text(
                text = "No favourite meals yet",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = DarkText
            )
            Text(
                text = "Tap the heart icon on any logged meal in History to sync it here.",
                style = MaterialTheme.typography.bodySmall,
                color = GreyText,
                textAlign = TextAlign.Center
            )
        }
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