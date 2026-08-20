package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserProfileEntity::class,
        LevelProgressEntity::class,
        AvatarUnlockEntity::class,
        SessionLogEntity::class,
        BadgeEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MathQuestDatabase : RoomDatabase() {

    abstract fun mathQuestDao(): MathQuestDao

    companion object {
        @Volatile
        private var INSTANCE: MathQuestDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope = CoroutineScope(Dispatchers.IO)): MathQuestDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MathQuestDatabase::class.java,
                    "mathquest_kids_database"
                )
                .addCallback(DatabaseCallback(scope))
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch {
                        populateInitialData(database.mathQuestDao())
                    }
                }
            }

            private suspend fun populateInitialData(dao: MathQuestDao) {
                // Default Profile
                dao.insertOrUpdateProfile(
                    UserProfileEntity(
                        id = 1,
                        childName = "Math Explorer",
                        age = 7,
                        gradeLevel = "2nd Grade",
                        currentThemeId = "jungle",
                        equippedAvatarId = "tiger_leo",
                        coins = 50,
                        totalStars = 6,
                        voiceEnabled = true,
                        soundEffectsEnabled = true,
                        dailyTimeLimitMinutes = 30,
                        totalMinutesPlayed = 15,
                        parentPin = "1234",
                        isCoppaAgreed = true
                    )
                )

                // Initial default avatars unlocked
                dao.unlockItems(
                    listOf(
                        AvatarUnlockEntity("tiger_leo", "avatar"),
                        AvatarUnlockEntity("bear_cosmo", "avatar"),
                        AvatarUnlockEntity("first_steps", "badge")
                    )
                )

                // Populate initial level progression for Jungle and Space
                val initialLevels = mutableListOf<LevelProgressEntity>()
                val worlds = listOf("jungle", "space")
                val topics = listOf("ADDITION", "SUBTRACTION", "MULTIPLICATION", "FRACTIONS")

                worlds.forEach { world ->
                    topics.forEach { topic ->
                        for (level in 1..5) {
                            val isFirst = (level == 1)
                            initialLevels.add(
                                LevelProgressEntity(
                                    worldThemeId = world,
                                    topic = topic,
                                    levelIndex = level,
                                    stars = if (isFirst) 1 else 0,
                                    isUnlocked = isFirst,
                                    isCompleted = isFirst,
                                    bestAccuracyPct = if (isFirst) 100 else 0
                                )
                            )
                        }
                    }
                }
                dao.insertInitialProgressList(initialLevels)

                // Seed some initial friendly session logs for the Parent Dashboard analytics
                val now = System.currentTimeMillis()
                val oneDay = 24 * 60 * 60 * 1000L
                dao.insertSessionLog(
                    SessionLogEntity(
                        timestamp = now - (oneDay * 2),
                        topic = "ADDITION",
                        worldTheme = "jungle",
                        questionsAttempted = 5,
                        correctCount = 5,
                        accuracyPct = 100,
                        durationSeconds = 180,
                        starsEarned = 3
                    )
                )
                dao.insertSessionLog(
                    SessionLogEntity(
                        timestamp = now - oneDay,
                        topic = "SUBTRACTION",
                        worldTheme = "space",
                        questionsAttempted = 5,
                        correctCount = 4,
                        accuracyPct = 80,
                        durationSeconds = 240,
                        starsEarned = 2
                    )
                )
                dao.insertSessionLog(
                    SessionLogEntity(
                        timestamp = now - (1000 * 60 * 60 * 2),
                        topic = "MULTIPLICATION",
                        worldTheme = "jungle",
                        questionsAttempted = 5,
                        correctCount = 5,
                        accuracyPct = 100,
                        durationSeconds = 200,
                        starsEarned = 3
                    )
                )

                dao.unlockBadge(BadgeEntity("first_steps"))
            }
        }
    }
}
