package com.example.nutrisnap.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nutrisnap.ml.toMeal
import com.example.nutrisnap.navigation.BottomNavBar
import com.example.nutrisnap.navigation.Screen
import com.example.nutrisnap.ui.features.dashboard.DashboardScreen
import com.example.nutrisnap.ui.features.favourites.FavouritesScreen
import com.example.nutrisnap.ui.features.history.HistoryScreen
import com.example.nutrisnap.ui.features.profile.AccountSettingsScreen
import com.example.nutrisnap.ui.features.profile.ProfileScreen
import com.example.nutrisnap.ui.features.reports.NutritionReportsScreen
import com.example.nutrisnap.ui.features.scanner.FoodScannerScreen
import com.google.firebase.auth.FirebaseAuth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    mealViewModel: MealViewModel = viewModel(),
    onLogout: () -> Unit = {},
) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    var isShowingScanResults by remember { mutableStateOf(false) }

    val allMeals by mealViewModel.allMeals.collectAsState()
    val favouriteMeals by mealViewModel.favouriteMeals.collectAsState()

    val screenBackgroundColor = when (currentScreen) {
        Screen.History -> Color(0xFFF8F9FA)
        else -> Color(0xFFFBECE2)
    }

    val currentUser = remember { FirebaseAuth.getInstance().currentUser }
    val userName = currentUser?.displayName
        ?.takeIf { it.isNotBlank() }
        ?: currentUser?.email?.substringBefore("@")
        ?: "User"
    val userEmail = currentUser?.email.orEmpty()

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        containerColor = screenBackgroundColor
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val contentModifier = Modifier.fillMaxSize()

            when (currentScreen) {
                Screen.Home -> DashboardScreen(
                    onScanClick = {
                        currentScreen = Screen.Scanner
                    }
                )

                Screen.History -> HistoryScreen(
                    modifier = contentModifier,
                    meals = allMeals,
                    onToggleFavourite = { mealViewModel.toggleFavourite(it) },
                    onEditMeal = { mealViewModel.editMeal(it) },
                    onDeleteMeal = { mealViewModel.deleteMeal(it) }
                )

                Screen.Scanner -> FoodScannerScreen(
                    modifier = contentModifier,
                    onResultStateChanged = { hasResults ->
                        isShowingScanResults = hasResults
                    },
                    onConfirm = { prediction, imageUri ->
                        val newMeal = prediction.toMeal(imageUri = imageUri)
                        mealViewModel.addMeal(newMeal)

                        isShowingScanResults = false
                        currentScreen = Screen.History
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

                Screen.Profile -> ProfileScreen(
                    userName = userName,
                    userEmail = userEmail,
                    onNutritionReportsClick =  {
                        currentScreen = Screen.Reports
                    },
                    onSavedMealsClick = {
                        currentScreen = Screen.Favourites
                    },
                    onScanHistoryClick = {
                        currentScreen = Screen.History
                    },
                    onAccountSettingsClick = {
                        currentScreen = Screen.Settings
                    },
                    onLogoutClick = onLogout
                )

                Screen.Reports -> NutritionReportsScreen(
                    modifier = contentModifier,
                    meals = allMeals,
                    favouriteMealsCount = favouriteMeals.size,
                    onBackClick = {
                        currentScreen = Screen.Profile
                    }
                )

                Screen.Settings -> AccountSettingsScreen(
                    onBackClick = {
                        currentScreen = Screen.Profile
                    },
                    onLogoutClick = onLogout
                )
            }

            if (currentScreen != Screen.Scanner) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                ) {
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
            }
        }
    }
}