package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.MintSuccess
import com.example.ui.theme.SpaceElectricCyan
import com.example.ui.theme.SunnyYellow
import com.example.ui.theme.SunnyYellowDark

@Composable
fun StarCelebrationDialog(
    starsEarned: Int,
    coinsEarned: Int,
    accuracyPct: Int,
    onNextLevel: () -> Unit,
    onReplay: () -> Unit,
    onMap: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "stars")
    val bounceScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce"
    )

    Dialog(onDismissRequest = {}) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(16.dp, RoundedCornerShape(32.dp))
                .clip(RoundedCornerShape(32.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(4.dp, SunnyYellow, RoundedCornerShape(32.dp))
                .padding(24.dp)
                .testTag("celebration_dialog"),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header badge
                Box(
                    modifier = Modifier
                        .scale(bounceScale)
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(SunnyYellow)
                        .border(3.dp, SunnyYellowDark, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🏆", fontSize = 44.sp)
                }

                Text(
                    text = if (starsEarned >= 3) "SUPERSTAR MATH MASTER!" else "AWESOME JOB!",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Black
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )

                // 3 Stars Display
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 1..3) {
                        val isLit = i <= starsEarned
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(if (isLit) SunnyYellow.copy(alpha = 0.2f) else Color.LightGray.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isLit) Icons.Default.Star else Icons.Default.StarOutline,
                                contentDescription = "Star $i",
                                tint = if (isLit) SunnyYellow else Color.Gray,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                }

                // Stats row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "🪙 +$coinsEarned", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = SunnyYellowDark)
                        Text(text = "Coins Earned", style = MaterialTheme.typography.bodyMedium)
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "$accuracyPct%", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MintSuccess)
                        Text(text = "Accuracy", style = MaterialTheme.typography.bodyMedium)
                    }
                }

                // Buttons
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    KidButton(
                        text = "Next Mission 🚀",
                        onClick = onNextLevel,
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = MintSuccess,
                        darkShadowColor = Color(0xFF047857),
                        textColor = Color.White,
                        testTag = "next_level_button"
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        KidButton(
                            text = "Replay 🔄",
                            onClick = onReplay,
                            modifier = Modifier.weight(1f),
                            backgroundColor = Color(0xFFE2E8F0),
                            darkShadowColor = Color.Gray,
                            height = 48.dp,
                            testTag = "replay_button"
                        )

                        KidButton(
                            text = "Map 🗺️",
                            onClick = onMap,
                            modifier = Modifier.weight(1f),
                            backgroundColor = SpaceElectricCyan,
                            darkShadowColor = Color(0xFF0891B2),
                            textColor = Color.White,
                            height = 48.dp,
                            testTag = "map_button"
                        )
                    }
                }
            }
        }
    }
}
