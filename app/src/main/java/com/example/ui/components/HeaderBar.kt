package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.UserProfileEntity
import com.example.data.model.DEFAULT_ACCESSORIES
import com.example.data.model.DEFAULT_AVATARS
import com.example.ui.theme.*

@Composable
fun MathQuestTopBar(
    userProfile: UserProfileEntity,
    onAvatarClick: () -> Unit,
    onParentDashboardClick: () -> Unit,
    onToggleSound: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentAvatar = DEFAULT_AVATARS.find { it.id == userProfile.equippedAvatarId } ?: DEFAULT_AVATARS.first()
    val currentHat = DEFAULT_ACCESSORIES.find { it.id == userProfile.equippedHatId }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Explorer Pill (White rounded-full with green border)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .shadow(2.dp, CircleShape)
                .clip(CircleShape)
                .background(CardWhite.copy(alpha = 0.95f))
                .border(2.dp, ForestGreenBorder, CircleShape)
                .clickable { onAvatarClick() }
                .padding(start = 4.dp, top = 4.dp, bottom = 4.dp, end = 14.dp)
                .testTag("top_bar_avatar_pill")
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(VibrantOrange)
                    .border(2.dp, CardWhite, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = currentAvatar.emoji, fontSize = 22.sp)
                if (currentHat != null) {
                    Text(
                        text = currentHat.emoji,
                        fontSize = 12.sp,
                        modifier = Modifier.align(Alignment.TopEnd)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = "EXPLORER",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreenDark.copy(alpha = 0.7f),
                    lineHeight = 10.sp
                )
                Text(
                    text = userProfile.childName.uppercase(),
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp,
                    color = ForestGreenDark
                )
            }
        }

        // Diamond/Gem and Action buttons with 3D shadow style
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Coins / Gems with drop shadow [0_4px_0_0_#854D0E]
            Box(
                modifier = Modifier
                    .height(38.dp)
                    .drawBehind {
                        drawRoundRect(
                            color = VibrantGoldDark,
                            topLeft = Offset(0f, size.height - 4.dp.toPx()),
                            size = Size(size.width, 4.dp.toPx()),
                            cornerRadius = CornerRadius(14.dp.toPx(), 14.dp.toPx())
                        )
                    }
                    .clip(RoundedCornerShape(14.dp))
                    .background(VibrantGold)
                    .border(2.dp, VibrantGoldDark, RoundedCornerShape(14.dp))
                    .padding(horizontal = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "💎", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${userProfile.coins}",
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        color = TextDarkSlate
                    )
                }
            }

            // Sound Toggle (White square rounded-2xl with 4px shadow)
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .drawBehind {
                        drawRoundRect(
                            color = ForestGreenBorder,
                            topLeft = Offset(0f, size.height - 4.dp.toPx()),
                            size = Size(size.width, 4.dp.toPx()),
                            cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx())
                        )
                    }
                    .clip(RoundedCornerShape(12.dp))
                    .background(CardWhite.copy(alpha = 0.95f))
                    .border(2.dp, ForestGreenBorder, RoundedCornerShape(12.dp))
                    .clickable { onToggleSound() }
                    .testTag("sound_toggle_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (userProfile.soundEffectsEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                    contentDescription = "Sound",
                    tint = ForestGreenDark,
                    modifier = Modifier.size(18.dp)
                )
            }

            // Parent Zone Lock
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .drawBehind {
                        drawRoundRect(
                            color = ForestGreenBorder,
                            topLeft = Offset(0f, size.height - 4.dp.toPx()),
                            size = Size(size.width, 4.dp.toPx()),
                            cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx())
                        )
                    }
                    .clip(RoundedCornerShape(12.dp))
                    .background(CardWhite.copy(alpha = 0.95f))
                    .border(2.dp, ForestGreenBorder, RoundedCornerShape(12.dp))
                    .clickable { onParentDashboardClick() }
                    .testTag("parent_zone_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Parent Dashboard",
                    tint = ForestGreenDark,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
