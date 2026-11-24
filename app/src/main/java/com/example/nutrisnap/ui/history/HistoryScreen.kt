package com.example.nutrisnap.ui.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.nutrisnap.ui.theme.*

private data class MealActivity(
    val image: Int?,
    val emoji: String,
    val name: String,
    val time: String,
    val calories: String,
    val protein: String,
    val carbs: String,
    val fats: String
)

private data class HistoryDate(
    val day: String,
    val date: String
)

@Composable
fun HistoryScreen(
    onHomeClick: () -> Unit,
    onScanClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onFavouritesClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    var selectedDate by remember { mutableStateOf(2) }

    val dates = listOf(
        HistoryDate("Su", "06"),
        HistoryDate("Mo", "07"),
        HistoryDate("Tu", "08"),
        HistoryDate("We", "09"),
        HistoryDate("Thur", "10"),
        HistoryDate("Fri", "09"),
        HistoryDate("Sat", "10")
    )

    val meals = listOf(
        MealActivity(
            image = R.drawable.ugali_fish,
            emoji = "",
            name = "Ugali",
            time = "1:32 pm",
            calories = "1732 kcal",
            protein = "160 g",
            carbs = "100 g",
            fats = "160 g"
        ),
        MealActivity(
            image = R.drawable.chai,
            emoji = "",
            name = "Tea",
            time = "9:00 am",
            calories = "120 kcal",
            protein = "4 g",
            carbs = "18 g",
            fats = "3 g"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NutriWhite)
    ) {

        // Main scrollable content
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp)
        ) {

            Spacer(modifier = Modifier.height(28.dp))

            // Profile greeting
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(NutriOrange),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "♟",
                        fontSize = 34.sp,
                        color = NutriWhite
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = "Good Morning",
                        fontSize = 13.sp,
                        color = NutriGrey
                    )

                    Text(
                        text = "John Doe 👋",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = NutriDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // Date selector
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                dates.forEachIndexed { index, date ->
                    DateSelectorItem(
                        date = date,
                        selected = selectedDate == index,
                        onClick = { selectedDate = index }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Today's calories
            Text(
                text = "Today Calories",
                fontSize = 14.sp,
                color = NutriDark
            )

            Spacer(modifier = Modifier.height(5.dp))

            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "1982",
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold,
                    color = NutriDark
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "kcal / 3,000 kcal",
                    fontSize = 14.sp,
                    color = NutriGrey,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Calories progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(7.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFEEEEEE)
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.66f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(20.dp))
                        .background(NutriOrange)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Today's activity heading
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Today's Activity",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = NutriDark
                )

                Text(
                    text = "See more",
                    fontSize = 11.sp,
                    color = NutriOrange,
                    modifier = Modifier.clickable {
                        onHistoryClick()
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Meal activity cards
            meals.forEach { meal ->
                MealActivityCard(meal = meal)

                Spacer(modifier = Modifier.height(10.dp))
            }

            Spacer(modifier = Modifier.height(15.dp))
        }

        // Bottom navigation with central Scan button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(66.dp)
                .background(NutriWhite)
                .border(
                    width = 0.5.dp,
                    color = Color(0xFFEEEEEE)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                HistoryBottomItem(
                    icon = "⌂",
                    label = "Home",
                    selected = false,
                    onClick = onHomeClick
                )

                HistoryBottomItem(
                    icon = "◷",
                    label = "History",
                    selected = true,
                    onClick = onHistoryClick
                )

                Spacer(modifier = Modifier.width(55.dp))

                HistoryBottomItem(
                    icon = "♡",
                    label = "Favourites",
                    selected = false,
                    onClick = onFavouritesClick
                )

                HistoryBottomItem(
                    icon = "♟",
                    label = "Profile",
                    selected = false,
                    onClick = onProfileClick
                )
            }

            // Central camera button
            FloatingActionButton(
                onClick = onScanClick,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-17).dp)
                    .size(48.dp),
                shape = CircleShape,
                containerColor = NutriOrange,
                contentColor = NutriWhite
            ) {
                Text(
                    text = "📷",
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Composable
private fun DateSelectorItem(
    date: HistoryDate,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .size(width = 40.dp, height = 50.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(
                if (selected) NutriOrange else NutriWhite
            )
            .clickable { onClick() }
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = date.day,
            fontSize = 10.sp,
            color = if (selected) NutriWhite else NutriDark
        )

        Text(
            text = date.date,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (selected) NutriWhite else NutriDark
        )
    }
}

@Composable
private fun MealActivityCard(
    meal: MealActivity
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = NutriWhite
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Meal image or placeholder
            if (meal.image != null) {
                Image(
                    painter = painterResource(meal.image),
                    contentDescription = meal.name,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(NutriLightBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = meal.emoji,
                        fontSize = 30.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = meal.name,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = NutriDark
                    )

                    Text(
                        text = meal.time,
                        fontSize = 9.sp,
                        color = NutriGrey
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = meal.calories,
                    fontSize = 10.sp,
                    color = NutriGrey
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = meal.protein,
                        fontSize = 9.sp,
                        color = NutriOrange
                    )

                    Text(
                        text = meal.carbs,
                        fontSize = 9.sp,
                        color = NutriBlue
                    )

                    Text(
                        text = meal.fats,
                        fontSize = 9.sp,
                        color = NutriGreen
                    )
                }
            }
        }
    }
}

@Composable
private fun HistoryBottomItem(
    icon: String,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = icon,
            fontSize = 20.sp,
            color = if (selected) NutriOrange else NutriGrey
        )

        Text(
            text = label,
            fontSize = 9.sp,
            color = if (selected) NutriOrange else NutriGrey
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HistoryScreenPreview() {
    HistoryScreen(
        onHomeClick = {},
        onScanClick = {},
        onHistoryClick = {},
        onFavouritesClick = {},
        onProfileClick = {}
    )
}
