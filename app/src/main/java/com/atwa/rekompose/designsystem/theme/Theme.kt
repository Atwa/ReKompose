package com.atwa.rekompose.designsystem.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColorScheme(
    primary = Purple200,
    primaryContainer = Purple700,
    secondary = Teal200,
    onSecondary = Color.Black
)

private val LightColorPalette = lightColorScheme(
    primary = Purple500,
    primaryContainer = Purple700,
    secondary = Teal200,
    onSecondary = Color.White
    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun rekomposeSampleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    val systemUiController = rememberSystemUiController()
    DisposableEffect(systemUiController, darkTheme) {
        systemUiController.setSystemBarsColor(
            color = colors.onSecondary,
            darkIcons = !darkTheme
        )

        onDispose { }
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}