package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SpaceElectricCyan
import com.example.ui.theme.SunnyYellow

@Composable
fun VoiceSpeakerButton(
    isSpeaking: Boolean,
    isVoiceEnabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Read Aloud"
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = if (isSpeaking) 1.25f else 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(CircleShape)
            .background(if (isVoiceEnabled) SpaceElectricCyan.copy(alpha = 0.2f) else Color.LightGray.copy(alpha = 0.3f))
            .border(
                2.dp,
                if (isSpeaking) SunnyYellow else SpaceElectricCyan,
                CircleShape
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .testTag("voice_speaker_button")
    ) {
        Box(
            modifier = Modifier
                .scale(if (isSpeaking) pulseScale else 1.0f)
                .size(32.dp)
                .clip(CircleShape)
                .background(if (isSpeaking) SunnyYellow else SpaceElectricCyan),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isVoiceEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                contentDescription = "Voice Narration",
                tint = Color.Black,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = if (isSpeaking) "Speaking..." else label,
            style = MaterialTheme.typography.labelLarge,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
