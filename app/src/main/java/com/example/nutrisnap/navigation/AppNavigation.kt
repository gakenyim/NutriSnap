
package com.example.nutrisnap.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue


import com.example.nutrisnap.ui.auth.WelcomeScreen
import com.example.nutrisnap.ui.auth.LoginScreen
import com.example.nutrisnap.ui.auth.RegisterScreen
import com.example.nutrisnap.ui.dashboard.DashboardScreen

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
                    currentScreen = "dashboard"
                },
                onRegisterClick = {
                    currentScreen = "register"
                }
            )
        }
        "register" -> {
            RegisterScreen(
                onRegisterClick = {
                    currentScreen = "dashboard"
                },
                onLoginClick = {
                    currentScreen= "login"
                }
            )
        }
        "dashboard" ->
            DashboardScreen(
            hasMeals = true,
            onScanClick = { currentScreen = "scanner" },
            onHistoryClick = { currentScreen = "history" },
            onFavouritesClick = { currentScreen = "favourites" },
            onProfileClick = { currentScreen = "profile" }
        )
    }
}
