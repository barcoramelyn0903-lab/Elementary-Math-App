package com.example.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlin.math.roundToInt

@Composable
fun AnswerDropTarget(
    selectedAnswer: String?,
    isCorrect: Boolean?,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "?"
) {
    val borderColor = when (isCorrect) {
        true -> MintSuccess
        false -> VibrantRose
        null -> if (selectedAnswer != null) VibrantOrange else CardBorderGray
    }

    val backgroundColor = when (isCorrect) {
        true -> Color(0xFFDCFCE7)
        false -> Color(0xFFFFE4E6)
        null -> if (selectedAnswer != null) Color(0xFFFFEDD5) else Color(0xFFF8FAFC)
    }

    val shape = RoundedCornerShape(24.dp)

    Box(
        modifier = modifier
            .size(76.dp)
            .shadow(4.dp, shape)
            .drawBehind {
                drawRoundRect(
                    color = borderColor,
                    topLeft = Offset(0f, size.height - 6.dp.toPx()),
                    size = Size(size.width, 6.dp.toPx()),
                    cornerRadius = CornerRadius(24.dp.toPx(), 24.dp.toPx())
                )
            }
            .clip(shape)
            .background(backgroundColor)
            .border(2.5.dp, borderColor, shape)
            .clickable(enabled = selectedAnswer != null) { onClear() }
            .testTag("answer_drop_target"),
        contentAlignment = Alignment.Center
    ) {
        if (selectedAnswer != null) {
            Text(
                text = selectedAnswer,
                fontSize = 34.sp,
                fontWeight = FontWeight.Black,
                color = when (isCorrect) {
                    true -> Color(0xFF15803D)
                    false -> VibrantRoseDark
                    null -> TextDarkSlate
                },
                textAlign = TextAlign.Center
            )
        } else {
            Text(
                text = placeholder,
                fontSize = 34.sp,
                fontWeight = FontWeight.Black,
                color = ForestGreenDark.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
fun DraggableOptionTile(
    text: String,
    isSelected: Boolean,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = CardWhite,
    borderColor: Color = CardBorderGray,
    testTag: String = "option_tile"
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }

    val shape = RoundedCornerShape(24.dp)
    val tileBg = if (isSelected) VibrantGold else backgroundColor
    val tileBorder = if (isSelected) VibrantGoldBorder else borderColor
    val tileShadow = if (isSelected) VibrantGoldDark else CardBorderGray

    Box(
        modifier = modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .size(76.dp)
            .drawBehind {
                drawRoundRect(
                    color = tileShadow,
                    topLeft = Offset(0f, size.height - 8.dp.toPx()),
                    size = Size(size.width, 8.dp.toPx()),
                    cornerRadius = CornerRadius(24.dp.toPx(), 24.dp.toPx())
                )
            }
            .clip(shape)
            .background(tileBg)
            .border(2.dp, tileBorder, shape)
            .pointerInput(text) {
                detectDragGestures(
                    onDragStart = { isDragging = true },
                    onDragEnd = {
                        isDragging = false
                        if (offsetY < -80f) {
                            onSelect(text)
                        }
                        offsetX = 0f
                        offsetY = 0f
                    },
                    onDragCancel = {
                        isDragging = false
                        offsetX = 0f
                        offsetY = 0f
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    }
                )
            }
            .clickable { onSelect(text) }
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            color = TextDarkSlate,
            textAlign = TextAlign.Center
        )
    }
}
