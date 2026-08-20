package com.example.data.repository

import com.example.data.db.MathQuestDao
import com.example.data.entity.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MathQuestRepository(private val dao: MathQuestDao) {

    val userProfileFlow: Flow<UserProfileEntity?> = dao.getUserProfileFlow()

    fun getLevelProgressForWorld(worldId: String): Flow<List<LevelProgressEntity>> {
        return dao.getLevelProgressForWorldFlow(worldId)
    }

    val unlockedItemIdsFlow: Flow<List<String>> = dao.getUnlockedItemIdsFlow()

    val recentSessionsFlow: Flow<List<SessionLogEntity>> = dao.getRecentSessionLogsFlow()

    val unlockedBadgeIdsFlow: Flow<List<String>> = dao.getUnlockedBadgeIdsFlow()

    suspend fun getUserProfile(): UserProfileEntity {
        return dao.getUserProfile() ?: UserProfileEntity()
    }

    suspend fun updateProfile(profile: UserProfileEntity) {
        dao.insertOrUpdateProfile(profile)
    }

    suspend fun switchTheme(themeId: String) {
        val current = getUserProfile()
        dao.insertOrUpdateProfile(current.copy(currentThemeId = themeId))
    }

    suspend fun setEquippedAvatar(avatarId: String) {
        val current = getUserProfile()
        dao.insertOrUpdateProfile(current.copy(equippedAvatarId = avatarId))
    }

    suspend fun setEquippedHat(hatId: String?) {
        val current = getUserProfile()
        dao.insertOrUpdateProfile(current.copy(equippedHatId = hatId))
    }

    suspend fun purchaseItem(itemId: String, cost: Int, itemType: String): Boolean {
        val current = getUserProfile()
        if (current.coins >= cost) {
            val updatedProfile = current.copy(coins = current.coins - cost)
            dao.insertOrUpdateProfile(updatedProfile)
            dao.unlockItem(AvatarUnlockEntity(itemId = itemId, itemType = itemType))
            // Check collector badge
            checkCollectorBadge()
            return true
        }
        return false
    }

    suspend fun completeLevel(
        worldId: String,
        topic: String,
        levelIndex: Int,
        starsEarned: Int,
        accuracyPct: Int,
        coinsEarned: Int,
        durationSeconds: Int,
        questionsCount: Int,
        correctCount: Int
    ) {
        // Update level progress
        dao.insertOrUpdateProgress(
            LevelProgressEntity(
                worldThemeId = worldId,
                topic = topic,
                levelIndex = levelIndex,
                stars = starsEarned,
                isUnlocked = true,
                isCompleted = true,
                bestAccuracyPct = accuracyPct
            )
        )

        // Unlock next level if levelIndex < 5
        if (levelIndex < 5) {
            dao.insertOrUpdateProgress(
                LevelProgressEntity(
                    worldThemeId = worldId,
                    topic = topic,
                    levelIndex = levelIndex + 1,
                    stars = 0,
                    isUnlocked = true,
                    isCompleted = false,
                    bestAccuracyPct = 0
                )
            )
        }

        // Insert session log
        dao.insertSessionLog(
            SessionLogEntity(
                topic = topic,
                worldTheme = worldId,
                questionsAttempted = questionsCount,
                correctCount = correctCount,
                accuracyPct = accuracyPct,
                durationSeconds = durationSeconds,
                starsEarned = starsEarned
            )
        )

        // Update user stats
        val profile = getUserProfile()
        val totalStars = profile.totalStars + starsEarned
        val totalCoins = profile.coins + coinsEarned
        val totalMins = profile.totalMinutesPlayed + (durationSeconds / 60).coerceAtLeast(1)

        dao.insertOrUpdateProfile(
            profile.copy(
                totalStars = totalStars,
                coins = totalCoins,
                totalMinutesPlayed = totalMins
            )
        )

        // Evaluate Badges
        checkAndUnlockBadges(topic, worldId, levelIndex, accuracyPct)
    }

    private suspend fun checkAndUnlockBadges(
        topic: String,
        worldId: String,
        levelIndex: Int,
        accuracyPct: Int
    ) {
        dao.unlockBadge(BadgeEntity("first_steps"))

        when (topic) {
            "ADDITION" -> dao.unlockBadge(BadgeEntity("addition_hero"))
            "SUBTRACTION" -> dao.unlockBadge(BadgeEntity("subtraction_wizard"))
            "MULTIPLICATION" -> dao.unlockBadge(BadgeEntity("mult_multiplier"))
            "FRACTIONS" -> dao.unlockBadge(BadgeEntity("fraction_master"))
        }

        if (levelIndex >= 5) {
            if (worldId == "jungle") dao.unlockBadge(BadgeEntity("jungle_explorer"))
            if (worldId == "space") dao.unlockBadge(BadgeEntity("space_cosmonaut"))
        }

        if (accuracyPct == 100) {
            dao.unlockBadge(BadgeEntity("super_streak"))
        }
    }

    private suspend fun checkCollectorBadge() {
        dao.unlockBadge(BadgeEntity("collector"))
    }
}
