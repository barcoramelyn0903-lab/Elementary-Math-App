package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EquationSlotPosition
import com.example.data.model.ProblemType
import com.example.data.model.WorldTheme
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.MathQuestViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    viewModel: MathQuestViewModel,
    onBack: () -> Unit,
    onNavigateToMap: () -> Unit
) {
    val gameState by viewModel.gameState.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val isSpeaking by viewModel.voiceNarrator.isSpeaking.collectAsState()

    val currentProblem = gameState.currentProblem
    val worldTheme = gameState.worldTheme

    // Celebration Dialog when level completes
    if (gameState.isLevelCompleted) {
        val totalCount = gameState.problems.size.coerceAtLeast(1)
        val accuracy = ((gameState.correctCount.toFloat() / totalCount) * 100).toInt()
        val stars = when {
            accuracy >= 90 -> 3
            accuracy >= 60 -> 2
            else -> 1
        }
        StarCelebrationDialog(
            starsEarned = stars,
            coinsEarned = gameState.coinsEarned + (stars * 10),
            accuracyPct = accuracy,
            onNextLevel = { viewModel.advanceToNextLevel() },
            onReplay = { viewModel.replayLevel() },
            onMap = onNavigateToMap
        )
    }

    Scaffold(
        topBar = {
            MathQuestTopBar(
                userProfile = userProfile,
                onAvatarClick = {},
                onParentDashboardClick = onBack,
                onToggleSound = { viewModel.toggleSound() }
            )
        },
        containerColor = if (worldTheme == WorldTheme.SPACE) SpaceDark else VibrantLime
    ) { innerPadding ->
        if (currentProblem == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = VibrantOrange)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .vibrantPolkaDotBackground(
                        dotColor = if (worldTheme == WorldTheme.SPACE) Color.White.copy(alpha = 0.08f) else ForestGreenDark.copy(alpha = 0.12f)
                    )
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(top = 4.dp, bottom = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Level Progress Bar
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(22.dp)
                                .clip(CircleShape)
                                .background(if (worldTheme == WorldTheme.SPACE) Color(0xFF1E1B4B) else ForestGreenDeep.copy(alpha = 0.25f))
                                .border(1.dp, ForestGreenBorder.copy(alpha = 0.4f), CircleShape)
                                .padding(3.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(gameState.progressPct.coerceIn(0.1f, 1f))
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
                                text = "LEVEL ${gameState.levelIndex}: ${worldTheme.title.uppercase()}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = if (worldTheme == WorldTheme.SPACE) VibrantCyan else ForestGreenDark
                            )
                            Text(
                                text = "QUESTION ${gameState.currentProblemIndex + 1} OF ${gameState.problems.size}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = if (worldTheme == WorldTheme.SPACE) VibrantGold else ForestGreenDark
                            )
                        }
                    }
                }

                // Main Puzzle Time Card with 3D bottom border & rotating badge
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        // Main White Container with 8px bottom border
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .drawBehind {
                                    drawRoundRect(
                                        color = CardBorderGray,
                                        topLeft = Offset(0f, size.height - 8.dp.toPx()),
                                        size = Size(size.width, 8.dp.toPx()),
                                        cornerRadius = CornerRadius(36.dp.toPx(), 36.dp.toPx())
                                    )
                                }
                                .clip(RoundedCornerShape(36.dp))
                                .background(CardWhite)
                                .border(2.dp, CardBorderGray, RoundedCornerShape(36.dp))
                                .padding(horizontal = 20.dp, vertical = 24.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = if (worldTheme == WorldTheme.SPACE) "🚀" else "🎒",
                                    fontSize = 44.sp
                                )

                                Text(
                                    text = "PUZZLE TIME!",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 1.sp
                                    ),
                                    color = TextDarkSlate
                                )

                                // Math Equation layout based on slot position
                                when (currentProblem.slotPosition) {
                                    EquationSlotPosition.RIGHT_RESULT -> {
                                        // Standard: num1 + num2 = [ ? ]
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center,
                                            modifier = Modifier.padding(vertical = 6.dp)
                                        ) {
                                            Text(
                                                text = "${currentProblem.num1}",
                                                fontSize = 38.sp,
                                                fontWeight = FontWeight.Black,
                                                color = TextDarkSlate
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = currentProblem.operatorSymbol,
                                                fontSize = 38.sp,
                                                fontWeight = FontWeight.Black,
                                                color = VibrantOrange
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "${currentProblem.num2}",
                                                fontSize = 38.sp,
                                                fontWeight = FontWeight.Black,
                                                color = TextDarkSlate
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "=",
                                                fontSize = 38.sp,
                                                fontWeight = FontWeight.Black,
                                                color = VibrantOrange
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            AnswerDropTarget(
                                                selectedAnswer = gameState.selectedAnswer,
                                                isCorrect = gameState.isAnswerCorrect,
                                                onClear = { viewModel.clearAnswer() }
                                            )
                                        }
                                    }

                                    EquationSlotPosition.MIDDLE_OPERAND -> {
                                        // Missing Addend: num1 + [ ? ] = result
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center,
                                            modifier = Modifier.padding(vertical = 6.dp)
                                        ) {
                                            Text(
                                                text = "${currentProblem.num1}",
                                                fontSize = 38.sp,
                                                fontWeight = FontWeight.Black,
                                                color = TextDarkSlate
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = currentProblem.operatorSymbol,
                                                fontSize = 38.sp,
                                                fontWeight = FontWeight.Black,
                                                color = VibrantOrange
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            AnswerDropTarget(
                                                selectedAnswer = gameState.selectedAnswer,
                                                isCorrect = gameState.isAnswerCorrect,
                                                onClear = { viewModel.clearAnswer() }
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "=",
                                                fontSize = 38.sp,
                                                fontWeight = FontWeight.Black,
                                                color = VibrantOrange
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "${currentProblem.resultNumber}",
                                                fontSize = 38.sp,
                                                fontWeight = FontWeight.Black,
                                                color = TextDarkSlate
                                            )
                                        }
                                    }

                                    EquationSlotPosition.NONE -> {
                                        // Fractions / Word Stories
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            Text(
                                                text = currentProblem.promptText,
                                                style = MaterialTheme.typography.titleLarge.copy(
                                                    fontWeight = FontWeight.Black
                                                ),
                                                color = TextDarkSlate,
                                                textAlign = TextAlign.Center
                                            )
                                            AnswerDropTarget(
                                                selectedAnswer = gameState.selectedAnswer,
                                                isCorrect = gameState.isAnswerCorrect,
                                                onClear = { viewModel.clearAnswer() }
                                            )
                                        }
                                    }
                                }

                                // Visual Visualizers
                                when (currentProblem.problemType) {
                                    ProblemType.VISUAL_COUNTERS -> {
                                        VisualItemCounters(
                                            num1 = currentProblem.num1,
                                            num2 = currentProblem.num2,
                                            operator = currentProblem.operatorSymbol,
                                            itemEmoji = currentProblem.visualItemEmoji
                                        )
                                    }
                                    ProblemType.MULTIPLICATION_ARRAY -> {
                                        MultiplicationArrayVisualizer(
                                            rows = currentProblem.arrayRows,
                                            cols = currentProblem.arrayCols,
                                            itemEmoji = currentProblem.visualItemEmoji
                                        )
                                    }
                                    ProblemType.FRACTION_PIE, ProblemType.COMPARE_FRACTIONS -> {
                                        currentProblem.fractionData?.let { fraction ->
                                            FractionPieVisualizer(
                                                fraction = fraction,
                                                shadedColor = if (worldTheme == WorldTheme.SPACE) VibrantCyan else VibrantGold
                                            )
                                        }
                                    }
                                    else -> {}
                                }
                            }
                        }

                        // Difficulty Tag rotated 12 degrees
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = 6.dp, y = (-8).dp)
                                .rotate(12f)
                                .shadow(6.dp, CircleShape)
                                .clip(CircleShape)
                                .background(VibrantRose)
                                .border(2.dp, Color.White, CircleShape)
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "QUEST!",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    }
                }

                // Voice instruction pill
                item {
                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (worldTheme == WorldTheme.SPACE) Color(0xFF1E1B4B) else ForestGreenDeep.copy(alpha = 0.15f))
                            .clickable { viewModel.readCurrentInstruction() }
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "🔊", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "\"${currentProblem.voiceInstruction}\"",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (worldTheme == WorldTheme.SPACE) Color.White else ForestGreenDark
                        )
                    }
                }

                // Option Tiles Grid
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        currentProblem.options.forEachIndexed { index, optionText ->
                            val isSelected = (gameState.selectedAnswer == optionText)
                            DraggableOptionTile(
                                text = optionText,
                                isSelected = isSelected,
                                onSelect = { viewModel.selectAnswer(optionText) },
                                backgroundColor = if (isSelected) VibrantGold else CardWhite,
                                borderColor = if (isSelected) VibrantGoldBorder else CardBorderGray,
                                testTag = "option_tile_$index"
                            )
                        }
                    }
                }

                // Hint helper
                if (gameState.isHintVisible && currentProblem.hintText.isNotBlank()) {
                    item {
                        KidCard(
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = Color(0xFFFEF3C7),
                            borderColor = VibrantGold,
                            bottomBorderThickness = 4.dp
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(text = "💡", fontSize = 28.sp)
                                Column {
                                    Text(
                                        text = "Math Buddy Hint",
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFFB45309)
                                    )
                                    Text(
                                        text = currentProblem.hintText,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = TextDarkSlate
                                    )
                                }
                            }
                        }
                    }
                }

                // Action Bar (Hint button & Check Answer / Next Question)
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Hint Button
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .drawBehind {
                                    drawRoundRect(
                                        color = VibrantGoldDark,
                                        topLeft = Offset(0f, size.height - 4.dp.toPx()),
                                        size = Size(size.width, 4.dp.toPx()),
                                        cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx())
                                    )
                                }
                                .clip(RoundedCornerShape(16.dp))
                                .background(VibrantGold)
                                .border(2.dp, VibrantGoldDark, RoundedCornerShape(16.dp))
                                .clickable { viewModel.toggleHint() }
                                .testTag("hint_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = "Hint",
                                tint = TextDarkSlate,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        // Main Action Button
                        if (!gameState.isAnswerChecked) {
                            KidButton(
                                text = "CHECK ANSWER ✨",
                                onClick = { viewModel.checkAnswer() },
                                modifier = Modifier.weight(1f),
                                backgroundColor = if (gameState.selectedAnswer != null) VibrantGold else Color(0xFFE2E8F0),
                                darkShadowColor = if (gameState.selectedAnswer != null) VibrantGoldDark else Color(0xFFCBD5E1),
                                textColor = TextDarkSlate,
                                enabled = gameState.selectedAnswer != null,
                                testTag = "check_answer_button"
                            )
                        } else {
                            KidButton(
                                text = "NEXT QUESTION ➡️",
                                onClick = { viewModel.nextProblem() },
                                modifier = Modifier.weight(1f),
                                backgroundColor = VibrantOrange,
                                darkShadowColor = VibrantOrangeDark,
                                textColor = Color.White,
                                testTag = "next_question_button"
                            )
                        }
                    }
                }
            }
        }
    }
}
