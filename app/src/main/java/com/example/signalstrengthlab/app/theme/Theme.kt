package com.example.signalstrengthlab.app.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Green,
    primaryVariant = DarkGreen,
    onPrimary = Color.White,
    secondary = Orange,
    onSecondary = Color.White,
)

@Composable
fun SignalStrengthTheme(
//    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = DarkColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}