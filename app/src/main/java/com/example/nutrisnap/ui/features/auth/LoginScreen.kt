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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    modifier: Modifier = Modifier
) {
    var emailOrUsername by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val auth = remember { FirebaseAuth.getInstance() }
    val scrollState = rememberScrollState()
    val passwordBringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

    val handleLogin: () -> Unit = {
        val cleanEmail = emailOrUsername.trim()
        if (cleanEmail.isEmpty() || password.isEmpty()) {
            errorMessage = "Please enter your email and password."
        } else {
            isLoading = true
            errorMessage = null
            auth.signInWithEmailAndPassword(cleanEmail, password)
                .addOnCompleteListener { task ->
                    isLoading = false
                    if (task.isSuccessful) {
                        onLoginSuccess()
                    } else {
                        errorMessage = task.exception?.localizedMessage
                            ?: "Login failed. Please check your credentials."
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
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Food Image Banner
        AuthTopFoodBanner()

        // Form Section
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
                }
                .padding(horizontal = 24.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text(
                text = "Welcome to NutriSnap. Karibu!",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1F2937),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            AuthPillInputField(
                label = "Username*",
                value = emailOrUsername,
                onValueChange = { emailOrUsername = it },
                placeholder = "johndoe@gmail.com",
                keyboardType = KeyboardType.Email
            )

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
                text = "Login",
                onClick = handleLogin,
                isLoading = isLoading
            )

            // "Not registered? Click here"
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Not registered? ",
                    fontSize = 12.sp,
                    color = Color(0xFF1F2937)
                )

                Text(
                    text = "Click here",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LinkOrange,
                    modifier = Modifier.clickable { onNavigateToRegister() }
                )
            }
        }

        AuthCopyrightFooter()
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun LoginScreenPreview() {
    LoginScreen(onLoginSuccess = {}, onNavigateToRegister = {})
}