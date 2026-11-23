package com.example.nutrisnap.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nutrisnap.data.SampleData
import com.example.nutrisnap.data.models.Meal

@Composable
fun MealCard(
    meal: Meal,
    onToggleFavourite: (Meal) -> Unit,
    modifier: Modifier = Modifier
) {
    // Orange accent matching your Figma theme
    val brandOrange = Color(0xFFFF7A00)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: Square food image placeholder (later you can swap in Coil/AsyncImage)
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFFE6D1)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = meal.name.take(1),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = brandOrange
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Middle: Meal name + calories & macros
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = meal.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${meal.kcal} kcal • ${meal.protein}g P • ${meal.carbs}g C • ${meal.fat}g F",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Right: Favourite toggle button
            IconButton(onClick = { onToggleFavourite(meal) }) {
                Text(
                    text = if (meal.isFavourite) "♥" else "♡",
                    style = MaterialTheme.typography.titleLarge,
                    color = if (meal.isFavourite) brandOrange else Color.Gray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MealCardPreview() {
    MealCard(
        meal = SampleData.sampleMeals.first(),
        onToggleFavourite = {}
    )
}