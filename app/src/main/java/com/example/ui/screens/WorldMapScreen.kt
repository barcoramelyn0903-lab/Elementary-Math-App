package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.UserProfileEntity
import com.example.data.model.Difficulty
import com.example.data.model.MathTopic
import com.example.data.model.WorldTheme
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.MathQuestViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorldMapScreen(
    viewModel: MathQuestViewModel,
    onBack: () -> Unit,
    onStartLevel: () -> Unit
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val isSpeaking by viewModel.voiceNarrator.isSpeaking.collectAsState()

    val currentTheme = if (userProfile.currentThemeId == "space") WorldTheme.SPACE else WorldTheme.JUNGLE
    val progressList by if (currentTheme == WorldTheme.SPACE) viewModel.spaceProgress.collectAsState() else viewModel.jungleProgress.collectAsState()

    var selectedTopic by remember { mutableStateOf(MathTopic.ADDITION) }
    val topicProgress = progressList.filter { it.topic == selectedTopic.name }

    Scaffold(
        topBar = {
            MathQuestTopBar(
                userProfile = userProfile,
                onAvatarClick = {},
                onParentDashboardClick = onBack,
                onToggleSound = { viewModel.toggleSound() }
            )
        },
        containerColor = if (currentTheme == WorldTheme.SPACE) SpaceDark else VibrantLime
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .vibrantPolkaDotBackground(
                    dotColor = if (currentTheme == WorldTheme.SPACE) Color.White.copy(alpha = 0.08f) else ForestGreenDark.copy(alpha = 0.12f)
                )
        ) {
            // Header bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(CardWhite)
                            .border(2.dp, ForestGreenBorder, CircleShape)
                            .testTag("map_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = ForestGreenDark
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "${currentTheme.title.uppercase()} TRAIL",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black
                        ),
                        color = if (currentTheme == WorldTheme.SPACE) Color.White else ForestGreenDark
                    )
                }

                VoiceSpeakerButton(
                    isSpeaking = isSpeaking,
                    isVoiceEnabled = userProfile.voiceEnabled,
                    onClick = {
                        viewModel.voiceNarrator.speak("Welcome to the math trail! Pick a mission to climb!")
                    }
                )
            }

            // Topic Selector Chips
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(MathTopic.values()) { topic ->
                    val isSelected = topic == selectedTopic
                    Box(
                        modifier = Modifier
                            .drawBehind {
                                if (isSelected) {
                                    drawRoundRect(
                                        color = VibrantGoldDark,
                                        topLeft = Offset(0f, size.height - 3.dp.toPx()),
                                        size = Size(size.width, 3.dp.toPx()),
                                        cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx())
                                    )
                                }
                            }
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) VibrantGold else CardWhite)
                            .border(2.dp, if (isSelected) VibrantGoldBorder else CardBorderGray, RoundedCornerShape(16.dp))
                            .clickable { selectedTopic = topic }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                            .testTag("map_topic_${topic.name.lowercase()}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = topic.iconEmoji, fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = topic.title,
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp,
                                color = TextDarkSlate
                            )
                        }
                    }
                }
            }

            // Progression Trail Nodes
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
                contentPadding = PaddingValues(top = 10.dp, bottom = 48.dp)
            ) {
                items(5) { index ->
                    val levelIndex = index + 1
                    val levelEntity = topicProgress.find { it.levelIndex == levelIndex }
                    val isUnlocked = levelEntity?.isUnlocked ?: (levelIndex == 1)
                    val stars = levelEntity?.stars ?: 0
                    val isCompleted = levelEntity?.isCompleted ?: false
                    val isBossNode = (levelIndex == 5)

                    val difficulty = when (levelIndex) {
                        1 -> Difficulty.EASY
                        2, 3 -> Difficulty.MEDIUM
                        else -> Difficulty.HARD
                    }

                    val alignment = when (index % 3) {
                        0 -> Alignment.CenterStart
                        1 -> Alignment.Center
                        else -> Alignment.CenterEnd
                    }

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = alignment
                    ) {
                        VibrantMapNode(
                            levelIndex = levelIndex,
                            topic = selectedTopic,
                            difficulty = difficulty,
                            isUnlocked = isUnlocked,
                            isCompleted = isCompleted,
                            isBossNode = isBossNode,
                            stars = stars,
                            worldTheme = currentTheme,
                            onClick = {
                                if (isUnlocked) {
                                    viewModel.startMission(selectedTopic, difficulty, currentTheme, levelIndex)
                                    onStartLevel()
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VibrantMapNode(
    levelIndex: Int,
    topic: MathTopic,
    difficulty: Difficulty,
    isUnlocked: Boolean,
    isCompleted: Boolean,
    isBossNode: Boolean,
    stars: Int,
    worldTheme: WorldTheme,
    onClick: () -> Unit
) {
    val nodeColor = if (isUnlocked) {
        if (isBossNode) VibrantOrange else VibrantGold
    } else {
        Color(0xFFE2E8F0)
    }

    val shadowColor = if (isUnlocked) {
        if (isBossNode) VibrantOrangeDark else VibrantGoldDark
    } else {
        Color(0xFFCBD5E1)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(enabled = isUnlocked, onClick = onClick)
            .padding(6.dp)
            .testTag("map_node_$levelIndex")
    ) {
        Box(
            modifier = Modifier
                .size(if (isBossNode) 92.dp else 80.dp)
                .drawBehind {
                    drawRoundRect(
                        color = shadowColor,
                        topLeft = Offset(0f, size.height - 8.dp.toPx()),
                        size = Size(size.width, 8.dp.toPx()),
                        cornerRadius = CornerRadius(30.dp.toPx(), 30.dp.toPx())
                    )
                }
                .clip(RoundedCornerShape(30.dp))
                .background(nodeColor)
                .border(3.dp, if (isUnlocked) CardWhite else Color(0xFF94A3B8), RoundedCornerShape(30.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (isUnlocked) {
                if (isBossNode) {
                    Text(text = "👑", fontSize = 42.sp)
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "LVL $levelIndex",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = TextDarkSlate
                        )
                        Text(
                            text = if (worldTheme == WorldTheme.JUNGLE) "🌴" else "🚀",
                            fontSize = 20.sp
                        )
                    }
                }
            } else {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Locked",
                    tint = Color(0xFF64748B),
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        if (isUnlocked) {
            Row(horizontalArrangement = Arrangement.Center) {
                for (s in 1..3) {
                    val filled = s <= stars
                    Text(
                        text = if (filled) "⭐" else "☆",
                        fontSize = 14.sp,
                        color = if (filled) VibrantGoldDark else Color.Gray
                    )
                }
            }
        }

        Text(
            text = if (isBossNode) "Treasure Chest" else difficulty.label.uppercase(),
            style = MaterialTheme.typography.labelLarge,
            fontSize = 10.sp,
            fontWeight = FontWeight.Black,
            color = if (isUnlocked) TextDarkSlate else Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}
