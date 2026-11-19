package com.example.nutrisnap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.nutrisnap.navigation.AppNavigation
import com.example.nutrisnap.ui.auth.WelcomeScreen
import com.example.nutrisnap.ui.theme.NutriSnapTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //setContent tells Android that we want to display a Jetpack Compose interface
        setContent {
            NutriSnapTheme {
                // Tells compose to display welcome screen
                AppNavigation()
            }
        }
    }
}

