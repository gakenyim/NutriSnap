package com.example.nutrisnap.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.nutrisnap.R
import com.example.nutrisnap.ui.theme.NutriSnapTheme

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    // mutableStateOf("") creates a state variable whose initial value is an empty string
    // remember keeps that state when Compose recomposes the screen
    // var allows the value to change
    var username by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // 1. Food image at the top
        Image(
            painter = painterResource(id = R.drawable.food_background),
            contentDescription = "NutriSnap food banner",
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Crop
        )

        // 2. Login form
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Welcome to NutriSnap. Karibu!",
                fontSize = 20.sp,
                color = Color(0xFF252525)
            )

            Spacer(modifier = Modifier.height(35.dp))

            // Username label
            Text(
                text = "Username*",
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(5.dp))

            // Username input
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                placeholder = { Text("John Doe") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                singleLine = true,
                shape = RoundedCornerShape(30.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFE94E0B),
                    unfocusedBorderColor = Color(0xFFDADDE2)
                )
            )

            Spacer(modifier = Modifier.height(25.dp))

            // Password label
            Text(
                text = "Password*",
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(5.dp))

            // Password input
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Enter password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                singleLine = true,
                shape = RoundedCornerShape(30.dp),
                visualTransformation = PasswordVisualTransformation(),// hides the actual password characters as the user types
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFE94E0B),
                    unfocusedBorderColor = Color(0xFFDADDE2)
                )
            )

            Spacer(modifier = Modifier.height(25.dp))

            // Login button
            Button(
                onClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE94E0B),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Login",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Registration link
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Not registered? ",
                    fontSize = 16.sp
                )

                TextButton(
                    onClick = onRegisterClick,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "Click here",
                        color = Color(0xFFE94E0B),
                        fontSize = 16.sp
                    )
                }
            }

            // Push copyright to the bottom
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "© 2026 NutriSnap. All rights reserved.",
                fontSize = 16.sp,
                color = Color(0xFF555555),
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }
    }
}

// Preview LoginScreen
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    NutriSnapTheme {
        LoginScreen(
            onLoginClick = {},
            onRegisterClick = {}
        )
    }
}
