package com.example.data.model

data class Avatar(
    val id: String,
    val name: String,
    val title: String,
    val emoji: String,
    val costCoins: Int,
    val worldTheme: WorldTheme,
    val description: String,
    val isDefaultUnlocked: Boolean = false
)

data class Accessory(
    val id: String,
    val name: String,
    val emoji: String,
    val type: String, // "hat", "gear", "pet"
    val costCoins: Int,
    val description: String
)

val DEFAULT_AVATARS = listOf(
    Avatar(
        id = "tiger_leo",
        name = "Leo",
        title = "Safari Explorer Tiger",
        emoji = "🐯",
        costCoins = 0,
        worldTheme = WorldTheme.JUNGLE,
        description = "Brave tiger cub who loves counting jungle coconuts!",
        isDefaultUnlocked = true
    ),
    Avatar(
        id = "bear_cosmo",
        name = "Cosmo",
        title = "Stellar Astro Bear",
        emoji = "🐻‍🚀",
        costCoins = 0,
        worldTheme = WorldTheme.SPACE,
        description = "Cosmic navigator who calculates star trajectories!",
        isDefaultUnlocked = true
    ),
    Avatar(
        id = "fox_luna",
        name = "Luna",
        title = "Astro Fox",
        emoji = "🦊",
        costCoins = 30,
        worldTheme = WorldTheme.SPACE,
        description = "Quick-thinking fox who leaps through asteroid belts!"
    ),
    Avatar(
        id = "toucan_pip",
        name = "Pip",
        title = "Rainforest Toucan",
        emoji = "🦜",
        costCoins = 30,
        worldTheme = WorldTheme.JUNGLE,
        description = "Colorful bird master of multiplying tropical fruits!"
    ),
    Avatar(
        id = "monkey_milo",
        name = "Milo",
        title = "Vine Jumper Monkey",
        emoji = "🐵",
        costCoins = 50,
        worldTheme = WorldTheme.JUNGLE,
        description = "Playful monkey who solves addition puzzles mid-air!"
    ),
    Avatar(
        id = "robot_nova",
        name = "Nova",
        title = "Quantum Robot",
        emoji = "🤖",
        costCoins = 60,
        worldTheme = WorldTheme.SPACE,
        description = "Supercomputer buddy programmed for high-speed fractions!"
    ),
    Avatar(
        id = "alien_zog",
        name = "Zog",
        title = "Friendly Martian",
        emoji = "👽",
        costCoins = 80,
        worldTheme = WorldTheme.SPACE,
        description = "Three-eyed friendly alien who loves symmetry & shapes!"
    ),
    Avatar(
        id = "lion_rex",
        name = "Rex",
        title = "Jungle King Cub",
        emoji = "🦁",
        costCoins = 100,
        worldTheme = WorldTheme.JUNGLE,
        description = "Proud jungle prince with a shiny golden math crown!"
    )
)

val DEFAULT_ACCESSORIES = listOf(
    Accessory("hat_explorer", "Safari Pith Hat", "🤠", "hat", 20, "Keeps the jungle sun away!"),
    Accessory("hat_helmet", "Astro Glass Helmet", "🪖", "hat", 25, "Provides pure oxygen in deep space!"),
    Accessory("hat_crown", "Math Champion Crown", "👑", "hat", 50, "Shines brightly when you master fractions!"),
    Accessory("gear_glasses", "Professor Goggles", "👓", "gear", 15, "Spot hidden patterns instantly!"),
    Accessory("gear_jetpack", "Mini Rocket Jetpack", "🚀", "gear", 40, "Zoom to the next math node!"),
    Accessory("pet_parrot", "Mini Robo Buddy", "🛸", "pet", 35, "Hovers beside you cheering every answer!")
)
