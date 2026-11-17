
package com.example.nutrisnap.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import com.example.nutrisnap.ui.auth.WelcomeScreen
import com.example.nutrisnap.ui.auth.LoginScreen

// controls the navigation between screens
@Composable
fun AppNavigation() {

    // Keeps track of the screen currently being displayed
    var currentScreen by remember {
        mutableStateOf("welcome")
    }

    // Decides which screen to display
    when (currentScreen) {

        "welcome" -> {
            WelcomeScreen(
                onGetStartedClick = {
                    currentScreen = "login"
                }
            )
        }

        "login" -> {
            LoginScreen(
                onLoginClick = {
                    // We will implement login functionality later
                },
                onRegisterClick = {
                    // Registration navigation will be added later
                }
            )
        }
    }
}
