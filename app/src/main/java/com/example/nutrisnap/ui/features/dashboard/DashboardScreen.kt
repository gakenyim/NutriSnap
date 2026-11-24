package com.example.nutrisnap.ui.features.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutrisnap.R

private val BrandOrange = Color(0xFFFF7A00)
private val BrandOrangeSoft = Color(0xFFFFF3E8)
private val CanvasBackground = Color(0xFFF8F9FA)
private val DarkText = Color(0xFF141414)
private val GreyText = Color(0xFF6B7280)
private val CardBorder = Color(0xFFECEEF2)

private data class DashboardMealItem(
    val imageRes: Int,
    val name: String,
    val summary: String,
    val category: String
)

@Composable
fun DashboardScreen(
    hasMeals: Boolean = false,
    onScanClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var searchText by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    var favouriteNames by remember { mutableStateOf(setOf<String>()) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = CanvasBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            if (!hasMeals) {
                // FIRST-TIME USER HOME
                Text(
                    text = "Welcome to NutriSnap!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Snap · Scan · Know",
                    style = MaterialTheme.typography.bodySmall,
                    color = GreyText
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Empty state hero card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, CardBorder),
                    shadowElevation = 3.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.food_background),
                            contentDescription = "NutriSnap food banner",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(190.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Your nutrition journey starts here!",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "You haven't scanned any meals yet. Scan your first meal to discover its nutritional value.",
                            style = MaterialTheme.typography.bodySmall,
                            color = GreyText
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Button(
                            onClick = onScanClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BrandOrange,
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Add,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Scan Your First Meal",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "How NutriSnap Works",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )

                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, CardBorder),
                    shadowElevation = 2.dp
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        DashboardInfoItem(
                            icon = Icons.Outlined.Add,
                            title = "Scan Your Food",
                            description = "Take a picture of your meal."
                        )
                        HorizontalDivider(color = CardBorder, thickness = 1.dp)
                        DashboardInfoItem(
                            icon = Icons.Outlined.CheckCircle,
                            title = "AI Food Recognition",
                            description = "Let NutriSnap identify your food."
                        )
                        HorizontalDivider(color = CardBorder, thickness = 1.dp)
                        DashboardInfoItem(
                            icon = Icons.Outlined.Info,
                            title = "View Nutrition Information",
                            description = "Discover calories and nutrients."
                        )
                    }
                }

                Spacer(modifier = Modifier.height(96.dp))

            } else {
                // RETURNING USER HOME
                Text(
                    text = "Recent Meals",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Search field
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = "Search meals...",
                            color = GreyText,
                            fontSize = 14.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Search",
                            tint = GreyText
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = BrandOrange,
                        unfocusedBorderColor = CardBorder
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // AI recommendation card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = BrandOrangeSoft,
                    border = BorderStroke(1.dp, BrandOrange.copy(alpha = 0.25f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "AI Recommendation",
                                color = BrandOrange,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Text(
                                text = "High Protein Lunch",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = DarkText
                            )

                            Text(
                                text = "Based on your goals and today's intake",
                                fontSize = 12.sp,
                                color = GreyText
                            )
                        }

                        Surface(
                            shape = CircleShape,
                            color = Color.White,
                            shadowElevation = 2.dp
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = null,
                                tint = BrandOrange,
                                modifier = Modifier
                                    .padding(12.dp)
                                    .size(24.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Meal filters
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("All", "High Protein", "Vegan", "Low calories").forEach { filter ->
                        val isSelected = selectedFilter == filter
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedFilter = filter },
                            label = {
                                Text(
                                    text = filter,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                )
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Color.White,
                                labelColor = DarkText,
                                selectedContainerColor = BrandOrange,
                                selectedLabelColor = Color.White
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = CardBorder,
                                selectedBorderColor = BrandOrange
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                val sampleMeals = remember {
                    listOf(
                        DashboardMealItem(R.drawable.chicken_bowl, "Chicken Power Bowl", "520 kcal  •  38g Protein", "High Protein"),
                        DashboardMealItem(R.drawable.minced_pasta, "Minced Meat Pasta", "320 kcal  •  40g Protein", "High Protein"),
                        DashboardMealItem(R.drawable.tofu_salad, "Tofu Salad", "220 kcal  •  48g Protein", "Vegan"),
                        DashboardMealItem(R.drawable.chapo_beans, "Chapati and Beans", "450 kcal  •  16g Protein", "Vegan"),
                        DashboardMealItem(R.drawable.ugali_fish, "Ugali with Fish", "480 kcal  •  32g Protein", "Low calories")
                    )
                }

                sampleMeals
                    .filter { meal ->
                        val matchesSearch = meal.name.contains(searchText, ignoreCase = true)
                        val matchesFilter = selectedFilter == "All" || meal.category == selectedFilter
                        matchesSearch && matchesFilter
                    }
                    .forEach { meal ->
                        val isFav = meal.name in favouriteNames

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            shape = RoundedCornerShape(20.dp),
                            color = Color.White,
                            border = BorderStroke(1.dp, CardBorder),
                            shadowElevation = 2.dp
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = meal.imageRes),
                                    contentDescription = meal.name,
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(RoundedCornerShape(14.dp)),
                                    contentScale = ContentScale.Crop
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = meal.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = DarkText
                                    )

                                    Text(
                                        text = meal.summary,
                                        fontSize = 12.sp,
                                        color = GreyText
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        favouriteNames = if (isFav) {
                                            favouriteNames - meal.name
                                        } else {
                                            favouriteNames + meal.name
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (isFav) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                        contentDescription = "Favourite",
                                        tint = if (isFav) BrandOrange else GreyText,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        }
                    }

                Spacer(modifier = Modifier.height(84.dp))
            }
        }
    }
}

@Composable
private fun DashboardInfoItem(
    icon: ImageVector,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = BrandOrangeSoft
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = BrandOrange,
                modifier = Modifier
                    .padding(10.dp)
                    .size(20.dp)
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = title,
                fontSize = 14.sp,
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardPreview() {
    DashboardScreen(hasMeals = false)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardWithMealsPreview() {
    DashboardScreen(hasMeals = true)
}