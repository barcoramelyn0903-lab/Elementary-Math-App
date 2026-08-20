package com.example.data.db

import androidx.room.*
import com.example.data.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MathQuestDao {

    // User Profile
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfileFlow(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    suspend fun getUserProfile(): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfileEntity)

    // Level Progress
    @Query("SELECT * FROM level_progress WHERE worldThemeId = :worldId")
    fun getLevelProgressForWorldFlow(worldId: String): Flow<List<LevelProgressEntity>>

    @Query("SELECT * FROM level_progress WHERE worldThemeId = :worldId AND topic = :topic ORDER BY levelIndex ASC")
    fun getLevelProgressForTopicFlow(worldId: String, topic: String): Flow<List<LevelProgressEntity>>

    @Query("SELECT * FROM level_progress")
    fun getAllLevelProgressFlow(): Flow<List<LevelProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProgress(progress: LevelProgressEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertInitialProgressList(progressList: List<LevelProgressEntity>)

    // Unlocks
    @Query("SELECT itemId FROM avatar_unlocks")
    fun getUnlockedItemIdsFlow(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun unlockItem(item: AvatarUnlockEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun unlockItems(items: List<AvatarUnlockEntity>)

    // Session Logs
    @Query("SELECT * FROM session_logs ORDER BY timestamp DESC LIMIT 50")
    fun getRecentSessionLogsFlow(): Flow<List<SessionLogEntity>>

    @Insert
    suspend fun insertSessionLog(log: SessionLogEntity)

    // Badges
    @Query("SELECT badgeId FROM unlocked_badges")
    fun getUnlockedBadgeIdsFlow(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun unlockBadge(badge: BadgeEntity)

    // Stats aggregates
    @Query("SELECT SUM(questionsAttempted) FROM session_logs")
    suspend fun getTotalQuestionsAttempted(): Int?

    @Query("SELECT SUM(correctCount) FROM session_logs")
    suspend fun getTotalCorrectQuestions(): Int?

    @Query("SELECT SUM(durationSeconds) FROM session_logs")
    suspend fun getTotalDurationSeconds(): Int?
}
