package com.example.nutrisnap.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

// Light theme
private val LightColorScheme = lightColorScheme(
    primary = NutriOrange,
    onPrimary = NutriWhite,

    secondary = NutriLightBlue,
    onSecondary = NutriDark,

    tertiary = NutriGreen,
    onTertiary = NutriWhite,

    background = NutriBackground,
    onBackground = NutriDark,

    surface = NutriWhite,
    onSurface = NutriDark
)

// Dark theme
private val DarkColorScheme = darkColorScheme(
    primary = NutriOrange,
    onPrimary = NutriWhite,

    secondary = NutriBlue,
    onSecondary = NutriDark,

    tertiary = NutriGreen,
    onTertiary = NutriWhite,

    background = NutriDark,
    onBackground = NutriWhite,

    surface = Color(0xFF303030),
    onSurface = NutriWhite
)

@Composable
fun NutriSnapTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}