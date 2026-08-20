package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.CardBorderGray
import com.example.ui.theme.CardWhite
import com.example.ui.theme.ForestGreenDark

@Composable
fun KidCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = CardWhite.copy(alpha = 0.95f),
    borderColor: Color = CardBorderGray,
    bottomBorderThickness: Dp = 6.dp,
    cornerRadius: Dp = 28.dp,
    elevation: Dp = 4.dp,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)

    Box(
        modifier = modifier
            .shadow(elevation, shape = shape, spotColor = Color(0x20000000))
            .drawBehind {
                if (bottomBorderThickness > 0.dp) {
                    drawRoundRect(
                        color = borderColor,
                        topLeft = Offset(0f, size.height - bottomBorderThickness.toPx()),
                        size = Size(size.width, bottomBorderThickness.toPx()),
                        cornerRadius = CornerRadius(cornerRadius.toPx(), cornerRadius.toPx())
                    )
                }
            }
            .clip(shape)
            .background(backgroundColor)
            .border(2.dp, borderColor, shape)
            .padding(contentPadding)
    ) {
        Column {
            content()
        }
    }
}

// Background modifier that draws subtle polka dots (from design: radial-gradient(#065F46 2px, transparent 2px))
fun Modifier.vibrantPolkaDotBackground(
    dotColor: Color = ForestGreenDark.copy(alpha = 0.12f),
    spacing: Float = 60f,
    radius: Float = 3.5f
): Modifier = this.drawBehind {
    val cols = (size.width / spacing).toInt() + 1
    val rows = (size.height / spacing).toInt() + 1
    for (r in 0..rows) {
        for (c in 0..cols) {
            drawCircle(
                color = dotColor,
                radius = radius,
                center = Offset(c * spacing, r * spacing)
            )
        }
    }
}
