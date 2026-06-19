package com.sachinkps.hanumanChalisa.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val OrangeLight = Color(0xFFFF6F00)
private val OrangeDark = Color(0xFFFFB300)
private val SaffronLight = Color(0xFFFF8F00)
private val RedDeep = Color(0xFFB71C1C)
private val GoldLight = Color(0xFFFFC107)

private val DarkColorScheme = darkColorScheme(
    primary = OrangeDark,
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF4A2400),
    onPrimaryContainer = OrangeDark,
    secondary = GoldLight,
    onSecondary = Color.Black,
    background = Color(0xFF1A0A00),
    onBackground = Color(0xFFFFF3E0),
    surface = Color(0xFF2D1500),
    onSurface = Color(0xFFFFF8E1),
    error = Color(0xFFCF6679)
)

private val LightColorScheme = lightColorScheme(
    primary = OrangeLight,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFE0B2),
    onPrimaryContainer = Color(0xFF3E1A00),
    secondary = SaffronLight,
    onSecondary = Color.White,
    background = Color(0xFFFFF8F0),
    onBackground = Color(0xFF1A0A00),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1A0A00),
    error = Color(0xFFB00020)
)

@Composable
fun HanumanChalisaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
