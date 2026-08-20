package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val VibrantDarkColorScheme = darkColorScheme(
    primary = VibrantGold,
    onPrimary = TextDarkSlate,
    primaryContainer = SpacePurple,
    onPrimaryContainer = Color.White,
    secondary = VibrantCyan,
    onSecondary = TextDarkSlate,
    tertiary = VibrantRose,
    background = SpaceDark,
    surface = SpaceCardBg,
    onBackground = Color.White,
    onSurface = Color.White,
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFFF1F5F9)
)

private val VibrantLightColorScheme = lightColorScheme(
    primary = VibrantOrange,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFEDD5),
    onPrimaryContainer = ForestGreenDark,
    secondary = ForestGreenDark,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFDCFCE7),
    onSecondaryContainer = ForestGreenDeep,
    tertiary = VibrantGold,
    background = VibrantLime,
    surface = CardWhite,
    onBackground = ForestGreenDark,
    onSurface = TextDarkSlate,
    surfaceVariant = Color(0xFFF8FAFC),
    onSurfaceVariant = TextDarkSlate
)

@Composable
fun MathQuestTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) VibrantDarkColorScheme else VibrantLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
