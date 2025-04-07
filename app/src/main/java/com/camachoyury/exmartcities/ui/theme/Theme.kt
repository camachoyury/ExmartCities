package com.camachoyury.exmartcities.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = ExmartRed,
    onPrimary = ExmartWhite,
    primaryContainer = ExmartDarkRed,
    onPrimaryContainer = ExmartLightRed,
    secondary = ExmartGray,
    onSecondary = ExmartWhite,
    secondaryContainer = ExmartBlack,
    onSecondaryContainer = ExmartLightGray,
    background = ExmartBlack,
    onBackground = ExmartWhite,
    surface = ExmartBlack,
    onSurface = ExmartWhite,
    error = Color.Red,
    onError = Color.Black
)

private val LightColorScheme = lightColorScheme(
    primary = ExmartRed,
    onPrimary = ExmartWhite,
    primaryContainer = ExmartLightRed,
    onPrimaryContainer = ExmartDarkRed,
    secondary = ExmartGray,
    onSecondary = ExmartWhite,
    secondaryContainer = ExmartLightGray,
    onSecondaryContainer = ExmartBlack,
    background = ExmartWhite,
    onBackground = ExmartBlack,
    surface = ExmartWhite,
    onSurface = ExmartBlack,
    error = Color.Red,
    onError = Color.White
    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)



private val DarkColors = darkColorScheme(
    primary = ExmartRed,
    onPrimary = ExmartWhite,
    primaryContainer = ExmartDarkRed,
    onPrimaryContainer = ExmartLightRed,
    secondary = ExmartGray,
    onSecondary = ExmartWhite,
    secondaryContainer = ExmartBlack,
    onSecondaryContainer = ExmartLightGray,
    background = ExmartBlack,
    onBackground = ExmartWhite,
    surface = ExmartBlack,
    onSurface = ExmartWhite,
    error = Color.Red,
    onError = Color.Black
)

@Composable
fun ExmartCitiesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}