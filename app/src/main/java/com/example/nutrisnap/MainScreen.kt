package com.example.nutrisnap

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.nutrisnap.navigation.BottomNavBar
import com.example.nutrisnap.navigation.Screen
import com.example.nutrisnap.ui.screens.FavouritesScreen
import com.example.nutrisnap.ui.screens.scanner.FoodScannerScreen
import com.example.nutrisnap.ui.screens.HistoryScreen
import com.example.nutrisnap.ui.screens.HomeScreen
import com.example.nutrisnap.ui.screens.ProfileScreen

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    var isShowingScanResults by remember { mutableStateOf(false) }

    val showBottomBar = currentScreen != Screen.Scanner || isShowingScanResults

    Scaffold(
        bottomBar = {
            if (currentScreen != Screen.Scanner) {
                BottomNavBar(
                    currentRoute = currentScreen.route,
                    onNavigate = { selectedScreen ->
                        if (selectedScreen == Screen.Scanner) {
                            isShowingScanResults = false
                        }
                        currentScreen = selectedScreen
                    }
                )
            }
        },
        containerColor = Color(0xFFFBECE2)
    ) { innerPadding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)

        when (currentScreen) {
            Screen.Home -> HomeScreen(modifier = contentModifier)
            Screen.History -> HistoryScreen(modifier = contentModifier)
            Screen.Scanner -> FoodScannerScreen(
                modifier = contentModifier,
                onResultStateChanged = { hasResults ->
                    isShowingScanResults = hasResults
                },
                onConfirm = { _ ->
                    isShowingScanResults = false
                    currentScreen = Screen.Home
                },
                onCloseScanner = {
                    currentScreen = Screen.Home
                }
            )
            Screen.Favourites -> FavouritesScreen(modifier = contentModifier)
            Screen.Profile -> ProfileScreen(modifier = contentModifier)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}