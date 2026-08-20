package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.SoundEffects
import com.example.audio.VoiceNarrator
import com.example.data.db.MathQuestDatabase
import com.example.data.entity.LevelProgressEntity
import com.example.data.entity.SessionLogEntity
import com.example.data.entity.UserProfileEntity
import com.example.data.model.*
import com.example.data.repository.MathQuestRepository
import com.example.logic.MathEngine
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class GameState(
    val topic: MathTopic = MathTopic.ADDITION,
    val difficulty: Difficulty = Difficulty.EASY,
    val worldTheme: WorldTheme = WorldTheme.JUNGLE,
    val levelIndex: Int = 1,
    val problems: List<MathProblem> = emptyList(),
    val currentProblemIndex: Int = 0,
    val selectedAnswer: String? = null,
    val isAnswerChecked: Boolean = false,
    val isAnswerCorrect: Boolean? = null,
    val streakCount: Int = 0,
    val correctCount: Int = 0,
    val coinsEarned: Int = 0,
    val isLevelCompleted: Boolean = false,
    val isHintVisible: Boolean = false,
    val startTimeMillis: Long = 0L
) {
    val currentProblem: MathProblem?
        get() = problems.getOrNull(currentProblemIndex)

    val progressPct: Float
        get() = if (problems.isNotEmpty()) (currentProblemIndex + 1).toFloat() / problems.size else 0f
}

class MathQuestViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MathQuestRepository
    val voiceNarrator: VoiceNarrator
    val soundEffects: SoundEffects

    val userProfile: StateFlow<UserProfileEntity>
    val unlockedItemIds: StateFlow<List<String>>
    val jungleProgress: StateFlow<List<LevelProgressEntity>>
    val spaceProgress: StateFlow<List<LevelProgressEntity>>
    val recentSessions: StateFlow<List<SessionLogEntity>>
    val unlockedBadgeIds: StateFlow<List<String>>

    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    init {
        val db = MathQuestDatabase.getDatabase(application, viewModelScope)
        repository = MathQuestRepository(db.mathQuestDao())
        voiceNarrator = VoiceNarrator(application)
        soundEffects = SoundEffects(viewModelScope)

        userProfile = repository.userProfileFlow
            .map { it ?: UserProfileEntity() }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserProfileEntity())

        unlockedItemIds = repository.unlockedItemIdsFlow
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf("tiger_leo", "bear_cosmo"))

        jungleProgress = repository.getLevelProgressForWorld("jungle")
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        spaceProgress = repository.getLevelProgressForWorld("space")
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        recentSessions = repository.recentSessionsFlow
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        unlockedBadgeIds = repository.unlockedBadgeIdsFlow
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf("first_steps"))

        // Sync sound / voice settings when profile changes
        viewModelScope.launch {
            userProfile.collect { profile ->
                voiceNarrator.isVoiceEnabled = profile.voiceEnabled
                soundEffects.isSoundEnabled = profile.soundEffectsEnabled
            }
        }
    }

    fun switchTheme(world: WorldTheme) {
        viewModelScope.launch {
            repository.switchTheme(world.id)
            soundEffects.playTap()
        }
    }

    fun toggleVoice() {
        val current = userProfile.value
        val updated = !current.voiceEnabled
        viewModelScope.launch {
            repository.updateProfile(current.copy(voiceEnabled = updated))
        }
        if (!updated) voiceNarrator.stop()
    }

    fun toggleSound() {
        val current = userProfile.value
        val updated = !current.soundEffectsEnabled
        viewModelScope.launch {
            repository.updateProfile(current.copy(soundEffectsEnabled = updated))
        }
    }

    fun startMission(
        topic: MathTopic,
        difficulty: Difficulty,
        worldTheme: WorldTheme,
        levelIndex: Int = 1
    ) {
        val problems = MathEngine.generateProblems(topic, difficulty, worldTheme, count = 5)
        _gameState.value = GameState(
            topic = topic,
            difficulty = difficulty,
            worldTheme = worldTheme,
            levelIndex = levelIndex,
            problems = problems,
            currentProblemIndex = 0,
            selectedAnswer = null,
            isAnswerChecked = false,
            isAnswerCorrect = null,
            streakCount = 0,
            correctCount = 0,
            coinsEarned = 0,
            isLevelCompleted = false,
            isHintVisible = false,
            startTimeMillis = System.currentTimeMillis()
        )

        // Play intro voice instruction
        speakCurrentProblemInstruction()
    }

    fun selectAnswer(answer: String) {
        if (_gameState.value.isAnswerChecked) return
        _gameState.value = _gameState.value.copy(selectedAnswer = answer)
        soundEffects.playTap()
    }

    fun clearAnswer() {
        if (_gameState.value.isAnswerChecked) return
        _gameState.value = _gameState.value.copy(selectedAnswer = null)
    }

    fun checkAnswer() {
        val current = _gameState.value
        val problem = current.currentProblem ?: return
        val chosen = current.selectedAnswer ?: return

        val isCorrect = (chosen.trim().lowercase() == problem.correctAnswer.trim().lowercase())

        if (isCorrect) {
            soundEffects.playCorrect()
            val newStreak = current.streakCount + 1
            val newCoins = current.coinsEarned + 10 + (if (newStreak >= 3) 5 else 0)
            _gameState.value = current.copy(
                isAnswerChecked = true,
                isAnswerCorrect = true,
                correctCount = current.correctCount + 1,
                streakCount = newStreak,
                coinsEarned = newCoins
            )
            voiceNarrator.speak("Awesome! That is correct!", android.speech.tts.TextToSpeech.QUEUE_FLUSH)
        } else {
            soundEffects.playWrong()
            _gameState.value = current.copy(
                isAnswerChecked = true,
                isAnswerCorrect = false,
                streakCount = 0
            )
            voiceNarrator.speak("Not quite! Let's try to understand the pattern.", android.speech.tts.TextToSpeech.QUEUE_FLUSH)
        }
    }

    fun toggleHint() {
        val current = _gameState.value
        val newVisibility = !current.isHintVisible
        _gameState.value = current.copy(isHintVisible = newVisibility)
        if (newVisibility) {
            current.currentProblem?.let { p ->
                if (p.hintText.isNotBlank()) {
                    voiceNarrator.speak("Hint: ${p.hintText}")
                }
            }
        }
    }

    fun nextProblem() {
        val current = _gameState.value
        val nextIdx = current.currentProblemIndex + 1

        if (nextIdx >= current.problems.size) {
            // Level Completed!
            finishLevel()
        } else {
            _gameState.value = current.copy(
                currentProblemIndex = nextIdx,
                selectedAnswer = null,
                isAnswerChecked = false,
                isAnswerCorrect = null,
                isHintVisible = false
            )
            speakCurrentProblemInstruction()
        }
    }

    private fun finishLevel() {
        val current = _gameState.value
        val totalProblems = current.problems.size.coerceAtLeast(1)
        val accuracyPct = ((current.correctCount.toFloat() / totalProblems) * 100).toInt()
        val stars = when {
            accuracyPct >= 90 -> 3
            accuracyPct >= 60 -> 2
            accuracyPct >= 40 -> 1
            else -> 1
        }
        val durationSeconds = ((System.currentTimeMillis() - current.startTimeMillis) / 1000).toInt().coerceAtLeast(10)

        _gameState.value = current.copy(isLevelCompleted = true)
        soundEffects.playVictoryFanfare()
        voiceNarrator.speak("You did it! Level complete! You earned $stars stars!")

        viewModelScope.launch {
            repository.completeLevel(
                worldId = current.worldTheme.id,
                topic = current.topic.name,
                levelIndex = current.levelIndex,
                starsEarned = stars,
                accuracyPct = accuracyPct,
                coinsEarned = current.coinsEarned + (stars * 10),
                durationSeconds = durationSeconds,
                questionsCount = totalProblems,
                correctCount = current.correctCount
            )
        }
    }

    fun replayLevel() {
        val current = _gameState.value
        startMission(current.topic, current.difficulty, current.worldTheme, current.levelIndex)
    }

    fun advanceToNextLevel() {
        val current = _gameState.value
        val nextLevelIdx = (current.levelIndex + 1).coerceAtMost(5)
        val nextDifficulty = when (nextLevelIdx) {
            1 -> Difficulty.EASY
            2, 3 -> Difficulty.MEDIUM
            else -> Difficulty.HARD
        }
        startMission(current.topic, nextDifficulty, current.worldTheme, nextLevelIdx)
    }

    fun readCurrentInstruction() {
        speakCurrentProblemInstruction()
    }

    private fun speakCurrentProblemInstruction() {
        _gameState.value.currentProblem?.let { problem ->
            voiceNarrator.speak(problem.voiceInstruction)
        }
    }

    // Avatar Shop operations
    fun purchaseAvatar(avatar: Avatar, onSuccess: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            val success = repository.purchaseItem(avatar.id, avatar.costCoins, "avatar")
            if (success) {
                repository.setEquippedAvatar(avatar.id)
                soundEffects.playCorrect()
                onSuccess()
            } else {
                soundEffects.playWrong()
                onError()
            }
        }
    }

    fun purchaseAccessory(accessory: Accessory, onSuccess: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            val success = repository.purchaseItem(accessory.id, accessory.costCoins, "accessory")
            if (success) {
                repository.setEquippedHat(accessory.id)
                soundEffects.playCorrect()
                onSuccess()
            } else {
                soundEffects.playWrong()
                onError()
            }
        }
    }

    fun equipAvatar(avatarId: String) {
        viewModelScope.launch {
            repository.setEquippedAvatar(avatarId)
            soundEffects.playTap()
        }
    }

    fun equipAccessory(accessoryId: String?) {
        viewModelScope.launch {
            repository.setEquippedHat(accessoryId)
            soundEffects.playTap()
        }
    }

    // Parent Dashboard Operations
    fun updateChildProfile(name: String, age: Int, grade: String) {
        val current = userProfile.value
        viewModelScope.launch {
            repository.updateProfile(
                current.copy(
                    childName = name,
                    age = age,
                    gradeLevel = grade
                )
            )
        }
    }

    fun updateParentSettings(dailyLimitMins: Int, pin: String) {
        val current = userProfile.value
        viewModelScope.launch {
            repository.updateProfile(
                current.copy(
                    dailyTimeLimitMinutes = dailyLimitMins,
                    parentPin = pin
                )
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        voiceNarrator.shutdown()
        soundEffects.release()
    }
}
