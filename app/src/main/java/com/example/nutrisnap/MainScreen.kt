package com.example.nutrisnap

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nutrisnap.navigation.BottomNavBar
import com.example.nutrisnap.navigation.Screen
import com.example.nutrisnap.ui.MealViewModel
import com.example.nutrisnap.ui.favourites.FavouritesScreen
import com.example.nutrisnap.ui.history.HistoryScreen
import com.example.nutrisnap.ui.screens.HomeScreen
import com.example.nutrisnap.ui.screens.ProfileScreen
import com.example.nutrisnap.ui.screens.scanner.FoodScannerScreen

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    mealViewModel: MealViewModel = viewModel()
) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    var isShowingScanResults by remember { mutableStateOf(false) }

    val allMeals by mealViewModel.allMeals.collectAsState()
    val favouriteMeals by mealViewModel.favouriteMeals.collectAsState()

    Scaffold(
        modifier = modifier,
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

            Screen.History -> Box(modifier = contentModifier) {
                HistoryScreen(
                    meals = allMeals,
                    onToggleFavourite = { mealViewModel.toggleFavourite(it) },
                    onEditMeal = { mealViewModel.editMeal(it) },
                    onDeleteMeal = { mealViewModel.deleteMeal(it) }
                )
            }

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

            Screen.Favourites -> Box(modifier = contentModifier) {
                FavouritesScreen(
                    meals = favouriteMeals,
                    onToggleFavourite = { mealViewModel.toggleFavourite(it) },
                    onEditMeal = { mealViewModel.editMeal(it) },
                    onDeleteMeal = { mealViewModel.deleteMeal(it) }
                )
            }

            Screen.Profile -> ProfileScreen(modifier = contentModifier)
        }
    }
}