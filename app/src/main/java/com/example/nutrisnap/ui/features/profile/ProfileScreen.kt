package com.example.nutrisnap.ui.features.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutrisnap.R

private val Orange = Color(0xFFE94E0B)
private val DarkText = Color(0xFF252525)
private val GreyText = Color(0xFF888888)

private class ConcaveHeaderShape : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width, size.height)
            quadraticTo(
                size.width / 2f,
                size.height * 0.78f,
                0f,
                size.height
            )
            close()
        }
        return Outline.Generic(path)
    }
}

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    userName: String = "User",
    userEmail: String = "",
    healthTag: String = "Health conscious",
    onScanHistoryClick: () -> Unit = {},
    onSavedMealsClick: () -> Unit = {},
    onNutritionReportsClick: () -> Unit = {},
    onAccountSettingsClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
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
                    .height(260.dp)
                    .clip(ConcaveHeaderShape())
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
                        .statusBarsPadding()
                        .padding(horizontal = 18.dp, vertical = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "NutriSnap",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Text(
                            text = "Snap · Scan · Know",
                            fontSize = 13.sp,
                            color = Color.White
                        )
                    }

                    IconButton(onClick = onAccountSettingsClick) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Account Settings",
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }
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
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Profile Avatar",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.padding(top = 22.dp)
                ) {
                    Text(
                        text = userName.ifBlank { "User" },
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )

                    if (userEmail.isNotBlank()) {
                        Text(
                            text = userEmail,
                            fontSize = 12.sp,
                            color = GreyText
                        )
                    }

                    Spacer(modifier = Modifier.height(5.dp))

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFFFFEEE7))
                            .padding(horizontal = 9.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = null,
                            tint = Orange,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = healthTag,
                            fontSize = 11.sp,
                            color = Orange
                        )
                    }
                }
            }

            // Welcome message
            Text(
                text = "Welcome, ${userName.ifBlank { "User" }}!",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Orange,
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-15).dp)
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center
            )

            // Profile menu
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                ProfileMenuItem(
                    icon = Icons.Outlined.DateRange,
                    title = "Scan History",
                    subtitle = "View your analyzed meals",
                    onClick = onScanHistoryClick
                )

                ProfileMenuItem(
                    icon = Icons.Outlined.FavoriteBorder,
                    title = "Saved Meals",
                    subtitle = "Your favorite and saved foods",
                    onClick = onSavedMealsClick
                )

                ProfileMenuItem(
                    icon = Icons.Outlined.Info,
                    title = "Nutrition Reports",
                    subtitle = "See your nutrition insights",
                    onClick = onNutritionReportsClick
                )

                ProfileMenuItem(
                    icon = Icons.Outlined.Person,
                    title = "Account Settings",
                    subtitle = "Update your profile and preferences",
                    onClick = onAccountSettingsClick
                )

                ProfileMenuItem(
                    icon = Icons.AutoMirrored.Filled.ExitToApp,
                    title = "Log Out",
                    subtitle = "Sign out of your account",
                    onClick = onLogoutClick
                )
            }

            Spacer(modifier = Modifier.height(96.dp))
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(Color.White)
            .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Orange,
            modifier = Modifier.size(22.dp)
        )

        Spacer(modifier = Modifier.width(14.dp))

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

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = GreyText,
            modifier = Modifier.size(22.dp)
        )
    }

    HorizontalDivider(color = Color(0xFFE8E8E8))
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(
        userName = "John Doe",
        userEmail = "john.doe@gmail.com",
        onScanHistoryClick = {},
        onSavedMealsClick = {},
        onNutritionReportsClick = {},
        onAccountSettingsClick = {},
        onLogoutClick = {}
    )
}