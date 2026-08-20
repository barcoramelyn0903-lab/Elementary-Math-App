package com.example.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun KidButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconEmoji: String? = null,
    backgroundColor: Color = VibrantGold,
    darkShadowColor: Color = VibrantGoldDark,
    textColor: Color = TextDarkSlate,
    enabled: Boolean = true,
    height: Dp = 56.dp,
    bottomShadowDepth: Dp = 6.dp,
    cornerRadius: Dp = 24.dp,
    testTag: String = "kid_button"
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val pressedOffset by animateDpAsState(
        targetValue = if (isPressed && enabled) bottomShadowDepth - 1.dp else 0.dp,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 500f),
        label = "button_offset"
    )

    val currentShadowHeight = if (isPressed && enabled) 1.dp else bottomShadowDepth
    val shape = RoundedCornerShape(cornerRadius)

    Box(
        modifier = modifier
            .testTag(testTag)
            .height(height + bottomShadowDepth)
            .padding(bottom = bottomShadowDepth - currentShadowHeight)
            .offset(y = pressedOffset)
            .drawBehind {
                if (enabled && bottomShadowDepth > 0.dp) {
                    drawRoundRect(
                        color = darkShadowColor,
                        topLeft = Offset(0f, size.height - currentShadowHeight.toPx()),
                        size = Size(size.width, currentShadowHeight.toPx() + 10.dp.toPx()),
                        cornerRadius = CornerRadius(cornerRadius.toPx(), cornerRadius.toPx())
                    )
                }
            }
            .clip(shape)
            .background(if (enabled) backgroundColor else Color(0xFFE2E8F0))
            .border(2.dp, if (enabled) darkShadowColor else Color(0xFFCBD5E1), shape)
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 18.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (iconEmoji != null) {
                Text(
                    text = iconEmoji,
                    fontSize = 22.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
            Text(
                text = text,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 17.sp
                ),
                color = if (enabled) textColor else Color(0xFF94A3B8),
                textAlign = TextAlign.Center
            )
        }
    }
}
