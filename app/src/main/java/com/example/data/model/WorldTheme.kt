package com.example.data.model

import androidx.compose.ui.graphics.Color
import com.example.R
import com.example.ui.theme.*

enum class WorldTheme(
    val id: String,
    val title: String,
    val subtitle: String,
    val companionName: String,
    val companionEmoji: String,
    val imageRes: Int,
    val primaryColor: Color,
    val secondaryColor: Color,
    val bgColors: List<Color>
) {
    JUNGLE(
        id = "jungle",
        title = "Jungle Safari",
        subtitle = "Trek through the Wild Rain Forest",
        companionName = "Leo the Explorer Tiger",
        companionEmoji = "🐯",
        imageRes = R.drawable.img_jungle_adventure,
        primaryColor = JungleMangoOrange,
        secondaryColor = JungleLeafGreen,
        bgColors = listOf(Color(0xFF064E3B), Color(0xFF047857), Color(0xFF10B981))
    ),
    SPACE(
        id = "space",
        title = "Space Odyssey",
        subtitle = "Soar across the Stellar Galaxy",
        companionName = "Cosmo the Astro Bear",
        companionEmoji = "🐻‍🚀",
        imageRes = R.drawable.img_space_adventure,
        primaryColor = SpaceStarGold,
        secondaryColor = SpaceElectricCyan,
        bgColors = listOf(Color(0xFF0F172A), Color(0xFF1E1B4B), Color(0xFF311042))
    );

    fun getDisplayName(): String = title
}
