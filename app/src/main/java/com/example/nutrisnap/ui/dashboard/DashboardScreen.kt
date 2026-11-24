package com.example.nutrisnap.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.nutrisnap.R
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable

private val Orange = Color(0xFFE94E0B)
private val DarkText = Color(0xFF252525)
private val GreyText = Color(0xFF888888)
private val Cream = Color(0xFFFFFBF8)

@Composable
fun DashboardScreen(
    hasMeals: Boolean = false,
    onHomeClick: () -> Unit = {},
    onScanClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onFavouritesClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // Main dashboard content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            if (!hasMeals) {

                // FIRST-TIME USER HOME

                Text(
                    text = "Welcome to NutriSnap!",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Snap · Scan · Know",
                    fontSize = 14.sp,
                    color = GreyText
                )

                Spacer(modifier = Modifier.height(30.dp))

                // Empty state card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Cream
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.food_background),
                            contentDescription = "NutriSnap food banner",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.height(15.dp))

                        Text(
                            text = "Your nutrition journey starts here!",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "You haven't scanned any meals yet. " +
                                    "Scan your first meal to discover its nutritional value.",
                            fontSize = 14.sp,
                            color = GreyText
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = onScanClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(15.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Orange
                            )
                        ) {
                            Text(
                                text = "📷  Scan Your First Meal",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(25.dp))

                Text(
                    text = "How NutriSnap Works",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )

                Spacer(modifier = Modifier.height(15.dp))

                DashboardInfoItem(
                    icon = "📸",
                    title = "Scan Your Food",
                    description = "Take a picture of your meal."
                )

                DashboardInfoItem(
                    icon = "🤖",
                    title = "AI Food Recognition",
                    description = "Let NutriSnap identify your food."
                )

                DashboardInfoItem(
                    icon = "📊",
                    title = "View Nutrition Information",
                    description = "Discover calories and nutrients."
                )

            } else {

                // RETURNING USER HOME

                Text(
                    text = "Recent Meals",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )

                Spacer(modifier = Modifier.height(15.dp))

                // Search field
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text("🔍  Search")
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(15.dp)
                )

                Spacer(modifier = Modifier.height(15.dp))

                // AI recommendation
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(15.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF5DCC6)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "AI Recommendation",
                                color = Orange,
                                fontSize = 11.sp
                            )

                            Spacer(modifier = Modifier.height(5.dp))

                            Text(
                                text = "High Protein Lunch",
                                fontWeight = FontWeight.Bold,
                                color = DarkText
                            )

                            Text(
                                text = "Based on your goals and today's intake",
                                fontSize = 11.sp,
                                color = DarkText
                            )
                        }

                        Text(
                            text = "🥘",
                            fontSize = 45.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))

                // Meal filters
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    listOf("All", "High Protein", "Vegan", "Low calories")
                        .forEach { filter ->

                            FilterChip(
                                selected = selectedFilter == filter,
                                onClick = { selectedFilter = filter },
                                label = {
                                    Text(
                                        text = filter,
                                        fontSize = 10.sp
                                    )
                                }
                            )
                        }
                }

                Spacer(modifier = Modifier.height(15.dp))

                // Temporary sample meals
                val sampleMeals = listOf(
                    Triple(R.drawable.chicken_bowl, "Chicken Power Bowl", "520 kcal  •  38g Protein"),
                    Triple(R.drawable.minced_pasta, "Minced Meat Pasta", "320 kcal  •  40g Protein"),
                    Triple(R.drawable.tofu_salad, "Tofu Salad", "220 kcal  •  48g Protein"),
                    Triple(R.drawable.chapo_beans, "Chapati and Beans", "450 kcal  •  16g Protein"),
                    Triple(R.drawable.ugali_fish, "Ugali with Fish", "480 kcal  •  32g Protein")
                )

                sampleMeals
                    .filter {
                        it.second.contains(searchText, ignoreCase = true)
                    }
                    .forEach { meal ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            shape = RoundedCornerShape(15.dp),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 3.dp
                            ),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Image(
                                    painter = painterResource(id = meal.first),
                                    contentDescription = meal.second,
                                    modifier = Modifier
                                        .size(75.dp)
                                        .clip(RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Crop
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = meal.second,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = DarkText
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = meal.third,
                                        fontSize = 11.sp,
                                        color = GreyText
                                    )
                                }

                                Text(
                                    text = "♡",
                                    fontSize = 22.sp,
                                    color = GreyText
                                )
                            }
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

            DashboardNavItem("⌂", "Home", true, onHomeClick)
            DashboardNavItem("◎", "History", false, onHistoryClick)

            // Central scan button
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Orange, RoundedCornerShape(50))
                    .clickable { onScanClick() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "📷",
                    fontSize = 22.sp
                )
            }

            DashboardNavItem("♡", "Favourites", false, onFavouritesClick)
            DashboardNavItem("♟", "Profile", false, onProfileClick)
        }
    }
}

@Composable
fun DashboardInfoItem(
    icon: String,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = icon,
            fontSize = 28.sp
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                color = DarkText
            )

            Text(
                text = description,
                fontSize = 12.sp,
                color = GreyText
            )
        }
    }
}

@Composable
fun DashboardNavItem(
    icon: String,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 5.dp),
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

// Preview Dashboard
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardPreview() {
    DashboardScreen(
        hasMeals = false,
        onScanClick = {},
        onHistoryClick = {},
        onFavouritesClick = {},
        onProfileClick = {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardWithMealsPreview() {
    DashboardScreen(
        hasMeals = true,
        onScanClick = {},
        onHistoryClick = {},
        onFavouritesClick = {},
        onProfileClick = {}
    )
}
