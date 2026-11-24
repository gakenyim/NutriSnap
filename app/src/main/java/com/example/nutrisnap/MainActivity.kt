package com.example.nutrisnap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.nutrisnap.ui.MealViewModel
import com.example.nutrisnap.ui.screens.auth.LandingScreen
import com.example.nutrisnap.ui.screens.auth.LoginScreen
import com.example.nutrisnap.ui.screens.auth.RegisterScreen
import com.example.nutrisnap.ui.theme.NutriSnapTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    private val mealViewModel: MealViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NutriSnapTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NutriSnapApp(
                        mealViewModel = mealViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

private enum class AuthRoute {
    LANDING, LOGIN, REGISTER, MAIN_APP
}

@Composable
fun NutriSnapApp(
    mealViewModel: MealViewModel,
    modifier: Modifier = Modifier
) {
    val auth = remember { FirebaseAuth.getInstance() }
    var currentRoute by remember {
        mutableStateOf(
            if (auth.currentUser != null) AuthRoute.MAIN_APP else AuthRoute.LANDING
        )
    }

    when (currentRoute) {
        AuthRoute.LANDING -> LandingScreen(
            onGetStartedClick = { currentRoute = AuthRoute.LOGIN }
        )

        AuthRoute.LOGIN -> LoginScreen(
            onLoginSuccess = { currentRoute = AuthRoute.MAIN_APP },
            onNavigateToRegister = { currentRoute = AuthRoute.REGISTER }
        )

        AuthRoute.REGISTER -> RegisterScreen(
            onRegisterSuccess = { currentRoute = AuthRoute.MAIN_APP },
            onNavigateToLogin = { currentRoute = AuthRoute.LOGIN }
        )

        AuthRoute.MAIN_APP -> MainScreen(
            mealViewModel = mealViewModel,
            modifier = modifier
        )
    }
}