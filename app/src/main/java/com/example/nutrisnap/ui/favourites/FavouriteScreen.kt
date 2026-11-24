package com.example.nutrisnap.ui.favourites

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.nutrisnap.R

private val Orange = Color(0xFFE94E0B)
private val DarkText = Color(0xFF252525)
private val GreyText = Color(0xFF888888)

private data class SavedMeal(
    val image: Int,
    val name: String,
    val calories: String,
    val protein: String
)

@Composable
fun FavouritesScreen(
    onHomeClick: () -> Unit,
    onScanClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val savedMeals = listOf(
        SavedMeal(
            R.drawable.chicken_bowl,
            "Chicken Power Bowl",
            "520 kcal",
            "38g protein"
        ),
        SavedMeal(
            R.drawable.passion_juice,
            "Passion Juice",
            "126 kcal",
            "0.96g protein"
        ),
        SavedMeal(
            R.drawable.pilau,
            "Pilau with Kachumbari",
            "690 kcal",
            "38g protein"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Page heading
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Text(
                text = "Saved Meals",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText
            )

            Text(
                text = "Your favourite meals in one place",
                fontSize = 14.sp,
                color = GreyText
            )
        }

        Text(
            text = "${savedMeals.size} saved meals",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Orange,
            modifier = Modifier.padding(
                horizontal = 20.dp,
                vertical = 8.dp
            )
        )

        // Saved meals list
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 8.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(savedMeals) { meal ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFFBF8)
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(meal.image),
                            contentDescription = meal.name,
                            modifier = Modifier
                                .size(85.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = meal.name,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkText
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = meal.calories,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Orange
                            )

                            Text(
                                text = meal.protein,
                                fontSize = 12.sp,
                                color = GreyText
                            )
                        }

                        Text(
                            text = "♥",
                            fontSize = 24.sp,
                            color = Orange
                        )
                    }
                }
            }
        }

        // Bottom navigation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FavouriteNavItem("⌂", "Home", false, onHomeClick)
            FavouriteNavItem("◎", "Scan", false, onScanClick)
            FavouriteNavItem("◷", "History", false, onHistoryClick)
            FavouriteNavItem("♟", "Profile", false, onProfileClick)
        }
    }
}

@Composable
private fun FavouriteNavItem(
    icon: String,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = icon,
            fontSize = 22.sp,
            color = if (selected) Orange else GreyText
        )

        Text(
            text = label,
            fontSize = 10.sp,
            color = if (selected) Orange else GreyText
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FavouritesScreenPreview() {
    FavouritesScreen(
        onHomeClick = {},
        onScanClick = {},
        onHistoryClick = {},
        onProfileClick = {}
    )
}


