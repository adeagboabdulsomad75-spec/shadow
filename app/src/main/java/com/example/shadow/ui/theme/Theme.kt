package com.example.shadow.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val NeonColorScheme = darkColorScheme(
    primary = OperatorButtonColor,
    secondary = ActionButtonColor,
    background = BackgroundDark,
    surface = DisplayBackground,
    onPrimary = White,
    onSecondary = White,
    onBackground = White,
    onSurface = White,
    surfaceVariant = NumberButtonColor,
    onSurfaceVariant = White
)

@Composable
fun ShadowTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = NeonColorScheme,
        typography = Typography,
        content = content
    )
}
