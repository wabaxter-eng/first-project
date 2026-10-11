package com.wabaxter.firstproject.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val LaneDark = Color(0xFF0D1B2A)
val BallRed = Color(0xFFD62828)
val PinWhite = Color(0xFFFFFFFF)

private val ColorScheme = darkColorScheme(
    primary = BallRed,
    background = LaneDark,
    surface = LaneDark,
    onPrimary = PinWhite,
    onBackground = PinWhite,
    onSurface = PinWhite,
)

@Composable
fun FirstProjectTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = ColorScheme, content = content)
}
