package com.example.nutrisnap.navigation

sealed class Screen(val route: String, val label: String) {
    data object Home: Screen("home", "Home")
    data object History: Screen("history", "History")
    data object Scanner: Screen("scanner", "Scanner")
    data object Favourites: Screen("favourites", "Favourites")
    data object Profile: Screen("profile", "Profile")
    data object Reports: Screen("reports", "Reports")
    data object Settings: Screen("settings", "Settings")
}