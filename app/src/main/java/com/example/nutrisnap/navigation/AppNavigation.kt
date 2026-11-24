
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
import com.example.nutrisnap.ui.profile.ProfileScreen
import com.example.nutrisnap.ui.history.HistoryScreen
import com.example.nutrisnap.ui.favourites.FavouritesScreen
import com.example.nutrisnap.ui.reports.NutritionReportsScreen
import com.example.nutrisnap.ui.profile.AccountSettingsScreen

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
        "profile" -> ProfileScreen(
            onHomeClick = {
                currentScreen = "dashboard"
            },
            onScanClick = {
                currentScreen = "scanner"
            },
            onHistoryClick = {
                currentScreen = "history"
            },
            onProfileClick = {
                currentScreen = "profile"
            },
            onScanHistoryClick = {
                currentScreen = "history"
            },
            onSavedMealsClick = {
                currentScreen = "favourites"
            },
            onNutritionReportsClick = {
                currentScreen = "reports"
            },
            onAccountSettingsClick = {
                currentScreen = "accountSettings"
            },
            onLogoutClick = {
                currentScreen = "welcome"
            }
        )
        "history" -> HistoryScreen(
            onHomeClick = { currentScreen = "dashboard" },
            onScanClick = { currentScreen = "scanner" },
            onHistoryClick = { currentScreen = "history" },
            onFavouritesClick = { currentScreen = "favourites" },
            onProfileClick = { currentScreen = "profile" }
        )
        "favourites" -> FavouritesScreen(
            onHomeClick = { currentScreen = "dashboard" },
            onScanClick = { currentScreen = "scanner" },
            onHistoryClick = { currentScreen = "history" },
            onProfileClick = { currentScreen = "profile" }
        )
        "reports" -> NutritionReportsScreen(
            onHomeClick = {
                currentScreen = "dashboard"
            },
            onScanClick = {
                currentScreen = "scanner"
            },
            onHistoryClick = {
                currentScreen = "history"
            },
            onProfileClick = {
                currentScreen = "profile"
            }
        )
        "accountSettings" -> AccountSettingsScreen(
            onBackClick = {
                currentScreen = "profile"
            },
            onLogoutClick = {
                currentScreen = "welcome"
            }
        )
    }
}
