package com.example.nutrisnap.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutrisnap.R

private val OrangeAccent = Color(0xFFFF8C24)

@Composable
fun BottomNavBar(
    currentRoute: String,
    onNavigate: (Screen) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .height(78.dp)
            .background(Color.Transparent)
    ) {
        // White Bottom Bar
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(62.dp),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Home
                NavBarItem(
                    icon = Icons.Default.Home,
                    label = Screen.Home.label,
                    selected = currentRoute == Screen.Home.route,
                    onClick = { onNavigate(Screen.Home) }
                )

                // History
                NavBarItem(
                    icon = ImageVector.vectorResource(R.drawable.history),
                    label = Screen.History.label,
                    selected = currentRoute == Screen.History.route,
                    onClick = { onNavigate(Screen.History) }
                )

                // Space for the center floating camera button
                Spacer(modifier = Modifier.width(48.dp))

                // Favourites
                NavBarItem(
                    icon = Icons.Default.FavoriteBorder,
                    label = Screen.Favourites.label,
                    selected = currentRoute == Screen.Favourites.route,
                    onClick = { onNavigate(Screen.Favourites) }
                )

                // Profile
                NavBarItem(
                    icon = Icons.Default.Person,
                    label = Screen.Profile.label,
                    selected = currentRoute == Screen.Profile.route,
                    onClick = { onNavigate(Screen.Profile) }
                )
            }
        }

        // Center Floating Orange Camera Button
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(54.dp)
                .shadow(6.dp, CircleShape)
                .background(OrangeAccent, CircleShape)
                .clickable { onNavigate(Screen.Scanner) },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.photo_camera),
                contentDescription = "Camera",
                tint = Color.White,
                modifier = Modifier.size(26.dp)
            )

        }
    }
}

@Composable
private fun NavBarItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
){
    val tint = if (selected) OrangeAccent else Color.Gray

    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = tint,
            modifier = Modifier.size(22.dp)
        )

        Text(
            text = label,
            fontSize = 10.sp,
            color = tint,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}