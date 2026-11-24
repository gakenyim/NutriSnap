
package com.example.nutrisnap.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
private val LightBackground = Color(0xFFFFFBF8)

@Composable
fun ProfileScreen(
    onHomeClick: () -> Unit,
    onScanClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onProfileClick: () -> Unit,
    onScanHistoryClick: () -> Unit,
    onSavedMealsClick: () -> Unit,
    onNutritionReportsClick: () -> Unit,
    onAccountSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // Main scrollable content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {

            // Header image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.food_background),
                    contentDescription = "Food background",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Dark overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.35f))
                )

                // Branding and settings
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 25.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "NutriSnap",
                            fontSize = 27.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Text(
                            text = "Snap · Scan · Know",
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }

                    Text(
                        text = "⚙",
                        fontSize = 27.sp,
                        color = Color.White,
                        modifier = Modifier.clickable {
                            onAccountSettingsClick()
                        }
                    )
                }
            }

            // User profile information
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-35).dp)
                    .padding(horizontal = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Profile avatar
                Box(
                    modifier = Modifier
                        .size(78.dp)
                        .clip(CircleShape)
                        .background(Orange),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "👤",
                        fontSize = 35.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "John Doe",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )

                    Text(
                        text = "john.doe@gmail.com",
                        fontSize = 12.sp,
                        color = GreyText
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = "♥ Health conscious",
                        fontSize = 11.sp,
                        color = Orange,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFFFFEEE7))
                            .padding(horizontal = 9.dp, vertical = 4.dp)
                    )
                }
            }

            // Welcome message
            Text(
                text = "Welcome, John Doe!",
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = Orange,
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-15).dp)
                    .padding(vertical = 12.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            // Profile menu
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {

                ProfileMenuItem(
                    icon = "◎",
                    title = "Scan History",
                    subtitle = "View your analyzed meals",
                    onClick = onScanHistoryClick
                )

                ProfileMenuItem(
                    icon = "♡",
                    title = "Saved Meals",
                    subtitle = "Your favorite and saved foods",
                    onClick = onSavedMealsClick
                )

                ProfileMenuItem(
                    icon = "▤",
                    title = "Nutrition Reports",
                    subtitle = "See your nutrition insights",
                    onClick = onNutritionReportsClick
                )

                ProfileMenuItem(
                    icon = "♙",
                    title = "Account Settings",
                    subtitle = "Update your profile and preferences",
                    onClick = onAccountSettingsClick
                )

                ProfileMenuItem(
                    icon = "⇥",
                    title = "Log Out",
                    subtitle = "Sign out of your account",
                    onClick = onLogoutClick
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        // Bottom navigation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 9.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem("⌂", "Home", false, onHomeClick)
            BottomNavItem("◎", "Scan", false, onScanClick)
            BottomNavItem("◷", "History", false, onHistoryClick)
            BottomNavItem("♟", "Profile", true, onProfileClick)
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: String,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(Color.White)
            .padding(horizontal = 12.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = icon,
            fontSize = 23.sp,
            color = Orange
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkText
            )

            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = GreyText
            )
        }

        Text(
            text = "›",
            fontSize = 27.sp,
            color = GreyText
        )
    }

    HorizontalDivider(color = Color(0xFFE8E8E8))
}

@Composable
fun BottomNavItem(
    icon: String,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 12.dp),
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
fun ProfileScreenPreview() {
    ProfileScreen(
        onHomeClick = {},
        onScanClick = {},
        onHistoryClick = {},
        onProfileClick = {},
        onScanHistoryClick = {},
        onSavedMealsClick = {},
        onNutritionReportsClick = {},
        onAccountSettingsClick = {},
        onLogoutClick = {}
    )
}


