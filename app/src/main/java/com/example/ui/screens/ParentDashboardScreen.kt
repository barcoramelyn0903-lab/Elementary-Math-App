package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.SessionLogEntity
import com.example.data.entity.UserProfileEntity
import com.example.data.model.MathTopic
import com.example.ui.components.KidButton
import com.example.ui.components.KidCard
import com.example.ui.components.vibrantPolkaDotBackground
import com.example.ui.theme.*
import com.example.ui.viewmodel.MathQuestViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentDashboardScreen(
    viewModel: MathQuestViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val userProfile by viewModel.userProfile.collectAsState()
    val recentSessions by viewModel.recentSessions.collectAsState()

    var childName by remember(userProfile.childName) { mutableStateOf(userProfile.childName) }
    var selectedAge by remember(userProfile.age) { mutableIntStateOf(userProfile.age) }
    var selectedGrade by remember(userProfile.gradeLevel) { mutableStateOf(userProfile.gradeLevel) }
    var dailyLimitMins by remember(userProfile.dailyTimeLimitMinutes) { mutableIntStateOf(userProfile.dailyTimeLimitMinutes) }
    var parentPin by remember(userProfile.parentPin) { mutableStateOf(userProfile.parentPin) }

    val totalAttempts = recentSessions.sumOf { it.questionsAttempted }
    val totalCorrect = recentSessions.sumOf { it.correctCount }
    val overallAccuracy = if (totalAttempts > 0) ((totalCorrect.toFloat() / totalAttempts) * 100).toInt() else 100

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "PARENT & EDUCATOR ZONE",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black
                        ),
                        color = ForestGreenDark
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(CardWhite)
                            .border(2.dp, ForestGreenBorder, CircleShape)
                            .testTag("parent_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = ForestGreenDark
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VibrantLime
                )
            )
        },
        containerColor = VibrantLime
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .vibrantPolkaDotBackground(dotColor = ForestGreenDark.copy(alpha = 0.12f))
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 4.dp, bottom = 48.dp)
        ) {
            // COPPA Certificate Banner
            item {
                KidCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Color(0xFFECFDF5),
                    borderColor = MintSuccess,
                    bottomBorderThickness = 4.dp
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(MintSuccess),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = "Security",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "COPPA Certified & Kid-Safe",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF065F46)
                            )
                            Text(
                                text = "Zero third-party trackers, no advertisements, and 100% on-device learning privacy.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF047857),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // High-Level Learning Stats
            item {
                Text(
                    text = "LEARNING OVERVIEW",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = ForestGreenDark
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    VibrantParentStatCard(
                        title = "Accuracy",
                        value = "$overallAccuracy%",
                        emoji = "🎯",
                        color = VibrantOrange,
                        modifier = Modifier.weight(1f)
                    )

                    VibrantParentStatCard(
                        title = "Practice",
                        value = "${userProfile.totalMinutesPlayed}m",
                        emoji = "⏱️",
                        color = VibrantGoldDark,
                        modifier = Modifier.weight(1f)
                    )

                    VibrantParentStatCard(
                        title = "Solved",
                        value = "$totalCorrect",
                        emoji = "⭐",
                        color = MintSuccess,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Skill Mastery Breakdown
            item {
                Text(
                    text = "SKILL MASTERY BREAKDOWN",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = ForestGreenDark
                )

                Spacer(modifier = Modifier.height(4.dp))

                KidCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = CardWhite,
                    borderColor = CardBorderGray,
                    bottomBorderThickness = 6.dp
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        MathTopic.values().forEach { topic ->
                            val topicSessions = recentSessions.filter { it.topic == topic.name }
                            val attempts = topicSessions.sumOf { it.questionsAttempted }
                            val correct = topicSessions.sumOf { it.correctCount }
                            val accuracy = if (attempts > 0) ((correct.toFloat() / attempts) * 100).toInt() else 95

                            VibrantSkillProgressBar(
                                topic = topic,
                                accuracyPct = accuracy,
                                completedCount = correct
                            )
                        }
                    }
                }
            }

            // Child Profile Settings
            item {
                Text(
                    text = "CHILD PROFILE SETTINGS",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = ForestGreenDark
                )

                Spacer(modifier = Modifier.height(4.dp))

                KidCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = CardWhite,
                    borderColor = CardBorderGray,
                    bottomBorderThickness = 6.dp
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = childName,
                            onValueChange = { childName = it },
                            label = { Text("Explorer Name") },
                            modifier = Modifier.fillMaxWidth().testTag("parent_child_name_input"),
                            singleLine = true
                        )

                        Text(
                            text = "Target Age (Ages 6 - 9):",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextDarkSlate
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(6, 7, 8, 9).forEach { age ->
                                val isSelected = selectedAge == age
                                FilterChip(
                                    selected = isSelected,
                                    onClick = {
                                        selectedAge = age
                                        selectedGrade = when (age) {
                                            6 -> "1st Grade"
                                            7 -> "2nd Grade"
                                            8 -> "3rd Grade"
                                            else -> "4th Grade"
                                        }
                                    },
                                    label = { Text("Age $age") },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        Text(
                            text = "Grade Level: $selectedGrade",
                            style = MaterialTheme.typography.bodyMedium,
                            color = ForestGreenDark,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Parental Controls
            item {
                Text(
                    text = "PARENT CONTROLS & SCREEN TIME",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = ForestGreenDark
                )

                Spacer(modifier = Modifier.height(4.dp))

                KidCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = CardWhite,
                    borderColor = CardBorderGray,
                    bottomBorderThickness = 6.dp
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "Daily Screen-Time Limit:",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextDarkSlate
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf(15, 30, 45, 60).forEach { mins ->
                                val isSelected = dailyLimitMins == mins
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { dailyLimitMins = mins },
                                    label = { Text("${mins}m") },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        OutlinedTextField(
                            value = parentPin,
                            onValueChange = { parentPin = it },
                            label = { Text("Parent PIN (4 Digits)") },
                            modifier = Modifier.fillMaxWidth().testTag("parent_pin_input"),
                            singleLine = true
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "Voice Narration", fontWeight = FontWeight.Bold, color = TextDarkSlate)
                                Text(text = "Reads math questions aloud", fontSize = 12.sp, color = TextMutedSlate)
                            }
                            Switch(
                                checked = userProfile.voiceEnabled,
                                onCheckedChange = { viewModel.toggleVoice() }
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        KidButton(
                            text = "SAVE SETTINGS 💾",
                            onClick = {
                                viewModel.updateChildProfile(childName, selectedAge, selectedGrade)
                                viewModel.updateParentSettings(dailyLimitMins, parentPin)
                                Toast.makeText(context, "Parent settings saved!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = VibrantGold,
                            darkShadowColor = VibrantGoldDark,
                            textColor = TextDarkSlate,
                            testTag = "save_parent_settings_button"
                        )
                    }
                }
            }

            // Recent Sessions
            item {
                Text(
                    text = "RECENT ACTIVITY LOG",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = ForestGreenDark
                )
            }

            if (recentSessions.isEmpty()) {
                item {
                    Text(
                        text = "No recent sessions yet. Completed math quests will appear here!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = ForestGreenDark.copy(alpha = 0.8f)
                    )
                }
            } else {
                items(recentSessions.take(5)) { session ->
                    VibrantSessionLogCard(session = session)
                }
            }
        }
    }
}

@Composable
fun VibrantParentStatCard(
    title: String,
    value: String,
    emoji: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .drawBehind {
                drawRoundRect(
                    color = CardBorderGray,
                    topLeft = Offset(0f, size.height - 4.dp.toPx()),
                    size = Size(size.width, 4.dp.toPx()),
                    cornerRadius = CornerRadius(18.dp.toPx(), 18.dp.toPx())
                )
            }
            .clip(RoundedCornerShape(18.dp))
            .background(CardWhite)
            .border(2.dp, CardBorderGray, RoundedCornerShape(18.dp))
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = emoji, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = color
            )
            Text(
                text = title.uppercase(),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = TextDarkSlate
            )
        }
    }
}

@Composable
fun VibrantSkillProgressBar(
    topic: MathTopic,
    accuracyPct: Int,
    completedCount: Int
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = topic.iconEmoji, fontSize = 16.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = topic.title,
                    fontWeight = FontWeight.Bold,
                    color = TextDarkSlate
                )
            }
            Text(
                text = "$accuracyPct% ($completedCount solved)",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextMutedSlate
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        LinearProgressIndicator(
            progress = { accuracyPct / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(5.dp)),
            color = topic.color,
            trackColor = Color(0xFFE2E8F0)
        )
    }
}

@Composable
fun VibrantSessionLogCard(session: SessionLogEntity) {
    val dateFormat = remember { SimpleDateFormat("MMM d, h:mm a", Locale.getDefault()) }
    val formattedDate = remember(session.timestamp) { dateFormat.format(Date(session.timestamp)) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawRoundRect(
                    color = CardBorderGray,
                    topLeft = Offset(0f, size.height - 3.dp.toPx()),
                    size = Size(size.width, 3.dp.toPx()),
                    cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx())
                )
            }
            .clip(RoundedCornerShape(16.dp))
            .background(CardWhite)
            .border(1.5.dp, CardBorderGray, RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "${session.topic} Mission",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = TextDarkSlate
                )
                Text(
                    text = formattedDate,
                    fontSize = 11.sp,
                    color = TextMutedSlate
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "${session.correctCount}/${session.questionsAttempted} Correct",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MintSuccess
                )

                Text(
                    text = "${session.accuracyPct}%",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = VibrantOrange
                )
            }
        }
    }
}
