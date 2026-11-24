package com.example.nutrisnap.ui.features.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val auth = remember { FirebaseAuth.getInstance() }
    val scrollState = rememberScrollState()
    val usernameBringIntoViewRequester = remember { BringIntoViewRequester() }
    val passwordBringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

    val handleRegister: () -> Unit = {
        val cleanEmail = email.trim()
        val cleanUsername = username.trim()

        when {
            cleanEmail.isEmpty() || cleanUsername.isEmpty() || password.isEmpty() -> {
                errorMessage = "Please fill in all fields."
            }
            password.length < 6 -> {
                errorMessage = "Password must be at least 6 characters."
            }
            else -> {
                isLoading = true
                errorMessage = null
                auth.createUserWithEmailAndPassword(cleanEmail, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Save the username to Firebase user profile
                            val profileUpdates = UserProfileChangeRequest.Builder()
                                .setDisplayName(cleanUsername)
                                .build()

                            task.result.user?.updateProfile(profileUpdates)
                                ?.addOnCompleteListener {
                                    isLoading = false
                                    onRegisterSuccess()
                                } ?: run {
                                isLoading = false
                                onRegisterSuccess()
                            }
                        } else {
                            isLoading = false
                            errorMessage = task.exception?.localizedMessage
                                ?: "Registration failed. Please try again."
                        }
                    }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .navigationBarsPadding()
            .imePadding()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Food Image Banner
        AuthTopFoodBanner()

        // Form Section (no .weight(1f) so verticalScroll expands freely above the keyboard)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Welcome to NutriSnap. Karibu!",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1F2937),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            AuthPillInputField(
                label = "Email*",
                value = email,
                onValueChange = { email = it },
                placeholder = "johndoe@gmail.com",
                keyboardType = KeyboardType.Email
            )

            // Scroll Username into view when focused
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .bringIntoViewRequester(usernameBringIntoViewRequester)
                    .onFocusEvent { focusState ->
                        if (focusState.isFocused || focusState.hasFocus) {
                            coroutineScope.launch {
                                delay(250)
                                usernameBringIntoViewRequester.bringIntoView()
                            }
                        }
                    }
            ) {
                AuthPillInputField(
                    label = "Username*",
                    value = username,
                    onValueChange = { username = it },
                    placeholder = "John Doe",
                    keyboardType = KeyboardType.Text
                )
            }

            // Scroll Password + Register button into view when focused
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .bringIntoViewRequester(passwordBringIntoViewRequester)
                    .onFocusEvent { focusState ->
                        if (focusState.isFocused || focusState.hasFocus) {
                            coroutineScope.launch {
                                delay(250)
                                passwordBringIntoViewRequester.bringIntoView()
                            }
                        }
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                AuthPillInputField(
                    label = "Password*",
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "••••••••",
                    isPassword = true,
                    keyboardType = KeyboardType.Password
                )

                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = Color(0xFFDC2626),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                BurntOrangePillButton(
                    text = "Register",
                    onClick = handleRegister,
                    isLoading = isLoading
                )
            }

            // "Already registered? Login"
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already registered? ",
                    fontSize = 12.sp,
                    color = Color(0xFF1F2937)
                )

                Text(
                    text = "Login",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LinkOrange,
                    modifier = Modifier.clickable {
                        onNavigateToLogin()
                    }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        AuthCopyrightFooter()
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(onRegisterSuccess = {}, onNavigateToLogin = {})
}