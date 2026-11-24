package com.example.nutrisnap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.material3.PrimaryTabRow
import com.example.nutrisnap.ui.MealViewModel
import com.example.nutrisnap.ui.favourites.FavouritesScreen
import com.example.nutrisnap.ui.history.HistoryScreen
import com.example.nutrisnap.ui.history.MealListScreen
import com.example.nutrisnap.ui.theme.NutriSnapTheme

class MainActivity : ComponentActivity() {

    private val mealViewModel: MealViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NutriSnapTheme {
                val allMeals by mealViewModel.allMeals.collectAsState()
                val favouriteMeals by mealViewModel.favouriteMeals.collectAsState()
                var selectedTab by remember { mutableIntStateOf(0) }
                val tabs = listOf("History", "Recent", "Favourites")

                Scaffold { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        PrimaryTabRow(selectedTabIndex = selectedTab) {
                            tabs.forEachIndexed { index, title ->
                                Tab(
                                    selected = selectedTab == index,
                                    onClick = { selectedTab = index },
                                    text = { Text(title) }
                                )
                            }
                        }

                        when (selectedTab) {
                            0 -> HistoryScreen(
                                meals = allMeals,
                                onToggleFavourite = { mealViewModel.toggleFavourite(it) },
                                onEditMeal = { mealViewModel.editMeal(it) },
                                onDeleteMeal = { mealViewModel.deleteMeal(it) }
                            )
                            1 -> MealListScreen(
                                title = "Recent Meals",
                                meals = allMeals,
                                onToggleFavourite = { mealViewModel.toggleFavourite(it) },
                                onEditMeal = { mealViewModel.editMeal(it) },
                                onDeleteMeal = { mealViewModel.deleteMeal(it) }
                            )
                            2 -> FavouritesScreen(
                                meals = favouriteMeals,
                                onToggleFavourite = { mealViewModel.toggleFavourite(it) },
                                onEditMeal = { mealViewModel.editMeal(it) },
                                onDeleteMeal = { mealViewModel.deleteMeal(it) }
                            )
                        }
                    }
                }
            }
        }
    }
}