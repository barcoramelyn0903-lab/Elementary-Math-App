package com.example.data.model

data class Badge(
    val id: String,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val requiredProgress: String,
    val isUnlocked: Boolean = false
)

val DEFAULT_BADGES = listOf(
    Badge("first_steps", "First Math Steps", "Completed your very first math challenge!", "🌟", "Solve 1 problem"),
    Badge("addition_hero", "Addition Ace", "Mastered 10 addition challenges!", "➕", "10 Addition clears"),
    Badge("subtraction_wizard", "Subtraction Star", "Cleared subtraction hurdles with no mistakes!", "➖", "10 Subtraction clears"),
    Badge("mult_multiplier", "Array Architect", "Built awesome multiplication arrays!", "✖️", "10 Multiplication clears"),
    Badge("fraction_master", "Fraction Chef", "Mastered half, third, and quarter slices!", "🍕", "10 Fraction clears"),
    Badge("jungle_explorer", "Jungle Champion", "Completed the full Jungle Safari campaign!", "🌴", "Unlock Jungle Boss Chest"),
    Badge("space_cosmonaut", "Cosmic Legend", "Reached the edge of the galaxy map!", "🚀", "Unlock Space Boss Chest"),
    Badge("super_streak", "Speedy Streak", "Answered 5 questions correctly in a row!", "⚡", "5-streak"),
    Badge("collector", "Fashion Explorer", "Unlocked 3 unique animal avatars!", "🦁", "Unlock 3 avatars")
)
