package com.example.nutrisnap.ui.reports

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.nutrisnap.ui.theme.*

@Composable
fun NutritionReportsScreen(
    onHomeClick: () -> Unit,
    onScanClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NutriWhite)
    ) {
        // Page heading
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Text(
                text = "Nutrition Reports",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = NutriDark
            )

            Text(
                text = "Your nutritional overview",
                fontSize = 14.sp,
                color = NutriGrey
            )
        }

        // Scrollable report content
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            // Daily summary
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = NutriOrange
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Today's Nutrition",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NutriWhite
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    Text(
                        text = "1,520",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = NutriWhite
                    )

                    Text(
                        text = "kcal recorded",
                        fontSize = 13.sp,
                        color = NutriWhite
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    Text(
                        text = "Based on your recorded meals",
                        fontSize = 12.sp,
                        color = NutriWhite
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Macronutrient Breakdown",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = NutriDark
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Protein
            NutrientCard(
                name = "Protein",
                amount = "86g",
                description = "Muscle growth and repair",
                background = NutriLightGreen
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Carbohydrates
            NutrientCard(
                name = "Carbohydrates",
                amount = "180g",
                description = "Your body's main energy source",
                background = NutriLightBlue
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Fats
            NutrientCard(
                name = "Fats",
                amount = "45g",
                description = "Supports energy and nutrient absorption",
                background = NutriBackground
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Meal Summary",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = NutriDark
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SummaryCard(
                    title = "Meals Scanned",
                    value = "3",
                    modifier = Modifier.weight(1f)
                )

                SummaryCard(
                    title = "Saved Meals",
                    value = "5",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Bottom navigation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(NutriWhite)
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ReportNavItem("⌂", "Home", false, onHomeClick)
            ReportNavItem("◎", "Scan", false, onScanClick)
            ReportNavItem("◷", "History", false, onHistoryClick)
            ReportNavItem("♟", "Profile", false, onProfileClick)
        }
    }
}

@Composable
private fun NutrientCard(
    name: String,
    amount: String,
    description: String,
    background: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = background
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = NutriDark
                )

                Text(
                    text = description,
                    fontSize = 11.sp,
                    color = NutriGrey
                )
            }

            Text(
                text = amount,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = NutriOrange
            )
        }
    }
}

@Composable
private fun SummaryCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = NutriBackground
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
                color = NutriOrange
            )

            Text(
                text = title,
                fontSize = 12.sp,
                color = NutriGrey
            )
        }
    }
}

@Composable
private fun ReportNavItem(
    icon: String,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = icon,
            fontSize = 22.sp,
            color = if (selected) NutriOrange else NutriGrey
        )

        Text(
            text = label,
            fontSize = 10.sp,
            color = if (selected) NutriOrange else NutriGrey
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NutritionReportsScreenPreview() {
    NutritionReportsScreen(
        onHomeClick = {},
        onScanClick = {},
        onHistoryClick = {},
        onProfileClick = {}
    )
}
