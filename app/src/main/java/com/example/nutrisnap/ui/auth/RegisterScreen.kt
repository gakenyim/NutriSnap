package com.example.nutrisnap.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
fun RegisterScreen(
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit
) {

    // Variables to store the user's input
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember {mutableStateOf("")}
    var successMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Food image banner
        Image(
            painter = painterResource(id = R.drawable.food_background),
            contentDescription = "NutriSnap food banner",
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Crop
        )

        // Scrollable registration form
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Create Your Account",
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF252525)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Username
            Text(
                text = "Username*",
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.SemiBold
            )

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                placeholder = { Text("Choose a username") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFE94E0B),
                    unfocusedBorderColor = Color(0xFFDADDE2)
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Email address
            Text(
                text = "Email Address*",
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.SemiBold
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Enter your email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(20.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFE94E0B),
                    unfocusedBorderColor = Color(0xFFDADDE2)
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Password
            Text(
                text = "Password*",
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.SemiBold
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Create a password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(20.dp),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFE94E0B),
                    unfocusedBorderColor = Color(0xFFDADDE2)
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Confirm password
            Text(
                text = "Confirm Password*",
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.SemiBold
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = { Text("Re-enter your password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(20.dp),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFE94E0B),
                    unfocusedBorderColor = Color(0xFFDADDE2)
                )
            )

            Spacer(modifier = Modifier.height(22.dp))

            // Register button
            Button(
                onClick = {
                    // Clear previous messages
                    errorMessage = ""
                    successMessage = ""

                    when {
                        username.isBlank() -> {
                            errorMessage = "Please enter a username."
                        }
                        email.isBlank() -> {
                            errorMessage = "Please enter your email address."
                        }

                        !android.util.Patterns.EMAIL_ADDRESS
                            .matcher(email.trim())
                            .matches() -> {
                            errorMessage = "Please enter a valid email address."
                        }
                        password.isBlank() -> {
                            errorMessage = "Please enter a password."
                        }
                        confirmPassword.isBlank() -> {
                            errorMessage = "Please confirm your password."
                        }
                        password != confirmPassword -> {
                            errorMessage = "Passwords do not match."
                        }
                        else -> {
                            successMessage = "Registration details are valid!"
                            onRegisterClick()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE94E0B),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Register",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            if (successMessage.isNotEmpty()) {
                Text(
                    text = successMessage,
                    color = Color(0xFF4F793B),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Link to Log in Screen
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account?",
                    fontSize = 14.sp
                )

                TextButton(
                    onClick = onLoginClick
                ) {
                    Text(
                        text = "Login here",
                        color = Color(0xFFE94E0B),
                        fontSize = 14.sp
                    )
                }
            }
        }

        // Copyright footer fixed at the bottom
        Text(
            text = "© 2026 NutriSnap. All rights reserved.",
            fontSize = 11.sp,
            color = Color(0xFF555555),
            modifier = Modifier.padding(
                top = 8.dp,
                bottom = 12.dp
            )
        )
    }
}

// Preview Register Screen
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreenPreview() {
    NutriSnapTheme {
        RegisterScreen(
            onRegisterClick = {},
            onLoginClick = {}
        )
    }
}
