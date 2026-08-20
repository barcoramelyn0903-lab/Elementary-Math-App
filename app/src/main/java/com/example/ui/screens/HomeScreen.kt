package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.UserProfileEntity
import com.example.data.model.DEFAULT_ACCESSORIES
import com.example.data.model.DEFAULT_AVATARS
import com.example.data.model.Difficulty
import com.example.data.model.MathTopic
import com.example.data.model.WorldTheme
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.MathQuestViewModel

@Composable
fun HomeScreen(
    viewModel: MathQuestViewModel,
    onNavigateToWorldMap: () -> Unit,
    onNavigateToGame: () -> Unit,
    onNavigateToShop: () -> Unit,
    onNavigateToParentDashboard: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val isSpeaking by viewModel.voiceNarrator.isSpeaking.collectAsState()

    var showParentalGate by remember { mutableStateOf(false) }

    val currentTheme = if (userProfile.currentThemeId == "space") WorldTheme.SPACE else WorldTheme.JUNGLE
    val currentAvatar = DEFAULT_AVATARS.find { it.id == userProfile.equippedAvatarId } ?: DEFAULT_AVATARS.first()
    val currentHat = DEFAULT_ACCESSORIES.find { it.id == userProfile.equippedHatId }

    if (showParentalGate) {
        ParentalGateDialog(
            onDismiss = { showParentalGate = false },
            onSuccess = {
                showParentalGate = false
                onNavigateToParentDashboard()
            },
            parentPin = userProfile.parentPin
        )
    }

    Scaffold(
        topBar = {
            MathQuestTopBar(
                userProfile = userProfile,
                onAvatarClick = onNavigateToShop,
                onParentDashboardClick = { showParentalGate = true },
                onToggleSound = { viewModel.toggleSound() }
            )
        },
        bottomBar = {
            VibrantBottomBar(
                currentRoute = "home",
                onNavigateHome = {},
                onNavigateMap = onNavigateToWorldMap,
                onNavigatePlay = {
                    viewModel.startMission(MathTopic.ADDITION, Difficulty.EASY, currentTheme, 1)
                    onNavigateToGame()
                },
                onNavigateShop = onNavigateToShop,
                onNavigateParent = { showParentalGate = true }
            )
        },
        containerColor = if (currentTheme == WorldTheme.SPACE) SpaceDark else VibrantLime
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .vibrantPolkaDotBackground(
                    dotColor = if (currentTheme == WorldTheme.SPACE) Color.White.copy(alpha = 0.08f) else ForestGreenDark.copy(alpha = 0.12f)
                )
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 4.dp, bottom = 48.dp)
        ) {
            // Level & XP Progress Banner
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp)
                            .clip(CircleShape)
                            .background(if (currentTheme == WorldTheme.SPACE) Color(0xFF1E1B4B) else ForestGreenDeep.copy(alpha = 0.25f))
                            .border(1.dp, ForestGreenBorder.copy(alpha = 0.4f), CircleShape)
                            .padding(3.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(0.65f)
                                .clip(CircleShape)
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(VibrantOrange, Color(0xFFFB923C))
                                    )
                                )
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "MISSION 4: ${currentTheme.title.uppercase()}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = if (currentTheme == WorldTheme.SPACE) VibrantCyan else ForestGreenDark
                        )
                        Text(
                            text = "650 / 1000 XP ⭐",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = if (currentTheme == WorldTheme.SPACE) VibrantGold else ForestGreenDark
                        )
                    }
                }
            }

            // World Theme Switcher (Jungle vs Space)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(CardWhite.copy(alpha = 0.9f))
                        .border(2.dp, ForestGreenBorder, RoundedCornerShape(20.dp))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    WorldTabButton(
                        title = "🌴 Jungle Safari",
                        isSelected = currentTheme == WorldTheme.JUNGLE,
                        activeColor = ForestGreenBorder,
                        onClick = { viewModel.switchTheme(WorldTheme.JUNGLE) },
                        modifier = Modifier.weight(1f),
                        testTag = "tab_jungle"
                    )

                    WorldTabButton(
                        title = "🚀 Space Odyssey",
                        isSelected = currentTheme == WorldTheme.SPACE,
                        activeColor = SpacePurple,
                        onClick = { viewModel.switchTheme(WorldTheme.SPACE) },
                        modifier = Modifier.weight(1f),
                        testTag = "tab_space"
                    )
                }
            }

            // Hero Adventure Banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .shadow(6.dp, RoundedCornerShape(32.dp))
                        .clip(RoundedCornerShape(32.dp))
                        .border(3.dp, if (currentTheme == WorldTheme.SPACE) VibrantGold else ForestGreenBorder, RoundedCornerShape(32.dp))
                ) {
                    Image(
                        painter = painterResource(id = currentTheme.imageRes),
                        contentDescription = currentTheme.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.75f))
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = currentTheme.title,
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Black
                            ),
                            color = Color.White
                        )
                        Text(
                            text = currentTheme.subtitle,
                            style = MaterialTheme.typography.bodyLarge,
                            color = VibrantGold
                        )
                    }
                }
            }

            // Companion Speech Bubble Card with 3D drop border
            item {
                KidCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = CardWhite.copy(alpha = 0.95f),
                    borderColor = CardBorderGray,
                    bottomBorderThickness = 6.dp
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(VibrantOrange)
                                .border(3.dp, CardWhite, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = currentAvatar.emoji, fontSize = 32.sp)
                            if (currentHat != null) {
                                Text(
                                    text = currentHat.emoji,
                                    fontSize = 18.sp,
                                    modifier = Modifier.align(Alignment.TopEnd)
                                )
                            }
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Hey ${userProfile.childName}! ${currentAvatar.emoji}",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Black
                                ),
                                color = TextDarkSlate
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Solve math quests in the ${currentTheme.title} to unlock cool avatars and badges!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = ForestGreenDark
                            )
                        }

                        VoiceSpeakerButton(
                            isSpeaking = isSpeaking,
                            isVoiceEnabled = userProfile.voiceEnabled,
                            onClick = {
                                viewModel.voiceNarrator.speak("Hey ${userProfile.childName}! Ready for puzzle time? Pick a quest below!")
                            },
                            label = "Listen"
                        )
                    }
                }
            }

            // Main Action Buttons (3D Tactile Buttons)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    KidButton(
                        text = "World Map 🗺️",
                        onClick = onNavigateToWorldMap,
                        modifier = Modifier.weight(1f),
                        backgroundColor = VibrantGold,
                        darkShadowColor = VibrantGoldDark,
                        textColor = TextDarkSlate,
                        testTag = "btn_world_map"
                    )

                    KidButton(
                        text = "Rewards Shop 🎁",
                        onClick = onNavigateToShop,
                        modifier = Modifier.weight(1f),
                        backgroundColor = VibrantOrange,
                        darkShadowColor = VibrantOrangeDark,
                        textColor = Color.White,
                        testTag = "btn_avatar_shop"
                    )
                }
            }

            // Math Topics Section Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CHOOSE A QUEST",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black
                        ),
                        color = if (currentTheme == WorldTheme.SPACE) Color.White else ForestGreenDark
                    )

                    Text(
                        text = "AGES 6-9",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Black,
                        color = if (currentTheme == WorldTheme.SPACE) VibrantCyan else ForestGreenDark.copy(alpha = 0.8f)
                    )
                }
            }

            // 4 Math Topic Cards
            items(MathTopic.values().size) { index ->
                val topic = MathTopic.values()[index]
                VibrantTopicCard(
                    topic = topic,
                    onPlay = {
                        viewModel.startMission(topic, Difficulty.EASY, currentTheme, 1)
                        onNavigateToGame()
                    }
                )
            }

            // COPPA & Safety Guarantee
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardWhite.copy(alpha = 0.85f))
                        .border(1.5.dp, ForestGreenBorder.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Safe",
                        tint = MintSuccess,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "100% Ad-Free • Safe & COPPA Certified • No In-App Purchases",
                        style = MaterialTheme.typography.labelLarge,
                        fontSize = 11.sp,
                        color = ForestGreenDark
                    )
                }
            }
        }
    }
}

@Composable
fun VibrantTopicCard(
    topic: MathTopic,
    onPlay: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawRoundRect(
                    color = CardBorderGray,
                    topLeft = Offset(0f, size.height - 6.dp.toPx()),
                    size = Size(size.width, 6.dp.toPx()),
                    cornerRadius = CornerRadius(24.dp.toPx(), 24.dp.toPx())
                )
            }
            .clip(RoundedCornerShape(24.dp))
            .background(CardWhite)
            .border(2.dp, CardBorderGray, RoundedCornerShape(24.dp))
            .clickable(onClick = onPlay)
            .padding(16.dp)
            .testTag("topic_card_${topic.name.lowercase()}")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Icon frame
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(topic.color.copy(alpha = 0.2f))
                    .border(2.dp, topic.color, RoundedCornerShape(18.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = topic.iconEmoji, fontSize = 28.sp)
            }

            // Description
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = topic.title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black
                        ),
                        color = TextDarkSlate
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(topic.color)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = topic.ageRange,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = topic.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMutedSlate
                )
            }

            // Play Button
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(VibrantOrange),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Start",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
fun WorldTabButton(
    title: String,
    isSelected: Boolean,
    activeColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) activeColor else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                fontSize = 14.sp
            ),
            color = if (isSelected) Color.White else TextDarkSlate
        )
    }
}

