package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val childName: String = "Math Explorer",
    val age: Int = 7,
    val gradeLevel: String = "2nd Grade",
    val currentThemeId: String = "jungle",
    val equippedAvatarId: String = "tiger_leo",
    val equippedHatId: String? = null,
    val coins: Int = 50,
    val totalStars: Int = 3,
    val voiceEnabled: Boolean = true,
    val soundEffectsEnabled: Boolean = true,
    val dailyTimeLimitMinutes: Int = 30,
    val totalMinutesPlayed: Int = 12,
    val parentPin: String = "1234",
    val isCoppaAgreed: Boolean = true
)

@Entity(tableName = "level_progress", primaryKeys = ["worldThemeId", "topic", "levelIndex"])
data class LevelProgressEntity(
    val worldThemeId: String,
    val topic: String,
    val levelIndex: Int, // 1 to 5 per topic
    val stars: Int, // 0 to 3
    val isUnlocked: Boolean,
    val isCompleted: Boolean,
    val bestAccuracyPct: Int
)

@Entity(tableName = "avatar_unlocks")
data class AvatarUnlockEntity(
    @PrimaryKey val itemId: String, // avatar id or accessory id
    val itemType: String, // "avatar" or "accessory"
    val isUnlocked: Boolean = true,
    val unlockedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "session_logs")
data class SessionLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val topic: String,
    val worldTheme: String,
    val questionsAttempted: Int,
    val correctCount: Int,
    val accuracyPct: Int,
    val durationSeconds: Int,
    val starsEarned: Int
)

@Entity(tableName = "unlocked_badges")
data class BadgeEntity(
    @PrimaryKey val badgeId: String,
    val unlockedAt: Long = System.currentTimeMillis()
)
