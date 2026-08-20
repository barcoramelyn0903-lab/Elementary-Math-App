package com.example.data.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.AdditionBlue
import com.example.ui.theme.FractionTeal
import com.example.ui.theme.MultiplicationPurple
import com.example.ui.theme.SubtractionOrange

enum class MathTopic(
    val title: String,
    val subtitle: String,
    val iconEmoji: String,
    val ageRange: String,
    val color: Color,
    val description: String
) {
    ADDITION(
        title = "Addition",
        subtitle = "Combine & Blast Off",
        iconEmoji = "➕",
        ageRange = "Ages 6-8",
        color = AdditionBlue,
        description = "Add numbers together, count jungle fruits, and power up your star rockets!"
    ),
    SUBTRACTION(
        title = "Subtraction",
        subtitle = "Take Away & Find Leftovers",
        iconEmoji = "➖",
        ageRange = "Ages 6-8",
        color = SubtractionOrange,
        description = "Subtract numbers, find differences, and clear space obstacles!"
    ),
    MULTIPLICATION(
        title = "Multiplication",
        subtitle = "Equal Groups & Fast Arrays",
        iconEmoji = "✖️",
        ageRange = "Ages 7-9",
        color = MultiplicationPurple,
        description = "Multiply groups of items, build rectangular arrays, and skip count!"
    ),
    FRACTIONS(
        title = "Fractions",
        subtitle = "Parts of a Whole & Slices",
        iconEmoji = "🍕",
        ageRange = "Ages 7-9",
        color = FractionTeal,
        description = "Slice pizzas, charge planetary energy spheres, and understand halves, thirds, & quarters!"
    );

    fun getVoiceIntroduction(): String {
        return when (this) {
            ADDITION -> "Let's explore Addition! Combine numbers and count together!"
            SUBTRACTION -> "Time for Subtraction! Find out how many are left over!"
            MULTIPLICATION -> "Awesome! Let's multiply equal groups and discover patterns!"
            FRACTIONS -> "Super fun Fractions! Let's explore parts of a whole!"
        }
    }
}
