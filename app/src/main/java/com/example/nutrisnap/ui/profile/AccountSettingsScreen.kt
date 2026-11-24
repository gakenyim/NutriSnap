package com.example.nutrisnap.ui.profile

import androidx.compose.foundation.background
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
import com.example.nutrisnap.ui.theme.*

@Composable
fun AccountSettingsScreen(
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    var name by remember { mutableStateOf("John Doe") }
    var email by remember { mutableStateOf("john.doe@gmail.com") }
    var notificationsEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NutriBackground)
    ) {

        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(NutriWhite)
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBackClick) {
                Text(
                    text = "‹ Back",
                    color = NutriOrange,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "Account Settings",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = NutriDark
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(18.dp)
        ) {

            Text(
                text = "Personal Information",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = NutriDark
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Name
            Text(
                text = "Full Name",
                color = NutriGrey,
                fontSize = 13.sp
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Email
            Text(
                text = "Email Address",
                color = NutriGrey,
                fontSize = 13.sp
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            HorizontalDivider(color = Color(0xFFE8E8E8))

            Spacer(modifier = Modifier.height(22.dp))

            // Preferences
            Text(
                text = "Preferences",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = NutriDark
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = NutriWhite
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Notifications",
                            fontWeight = FontWeight.SemiBold,
                            color = NutriDark
                        )

                        Text(
                            text = "Receive updates and reminders",
                            fontSize = 12.sp,
                            color = NutriGrey
                        )
                    }

                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = {
                            notificationsEnabled = it
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = NutriWhite,
                            checkedTrackColor = NutriOrange
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Save button
            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NutriOrange
                )
            ) {
                Text(
                    text = "Save Changes",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Logout button
            OutlinedButton(
                onClick = onLogoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Log Out",
                    color = NutriOrange,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


