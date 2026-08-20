package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FractionData
import com.example.ui.theme.*

@Composable
fun VisualItemCounters(
    num1: Int,
    num2: Int,
    operator: String,
    itemEmoji: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Group 1
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            ItemGrid(count = num1, emoji = itemEmoji)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$num1",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Operator
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(SunnyYellow),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = operator,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = Color.Black
            )
        }

        // Group 2
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            ItemGrid(count = num2, emoji = itemEmoji)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$num2",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
fun ItemGrid(count: Int, emoji: String, maxPerRow: Int = 5) {
    val boundedCount = count.coerceIn(1, 20)
    val rows = (boundedCount + maxPerRow - 1) / maxPerRow

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        for (r in 0 until rows) {
            Row(horizontalArrangement = Arrangement.Center) {
                for (c in 0 until maxPerRow) {
                    val index = r * maxPerRow + c
                    if (index < boundedCount) {
                        Text(
                            text = emoji,
                            fontSize = 22.sp,
                            modifier = Modifier.padding(2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MultiplicationArrayVisualizer(
    rows: Int,
    cols: Int,
    itemEmoji: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .border(2.dp, MultiplicationPurple.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$rows rows × $cols columns",
            style = MaterialTheme.typography.titleMedium,
            color = MultiplicationPurple,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            for (r in 0 until rows) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (c in 0 until cols) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MultiplicationPurple.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = itemEmoji, fontSize = 20.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FractionPieVisualizer(
    fraction: FractionData,
    modifier: Modifier = Modifier,
    shadedColor: Color = SpaceElectricCyan,
    unshadedColor: Color = Color(0xFFE2E8F0),
    borderColor: Color = Color(0xFF1E293B)
) {
    Column(
        modifier = modifier
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(160.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val radius = size.minDimension / 2 - 8
                val center = Offset(size.width / 2, size.height / 2)
                val totalParts = fraction.denominator.coerceAtLeast(1)
                val shadedParts = fraction.numerator.coerceIn(0, totalParts)
                val sweepAngle = 360f / totalParts

                // Draw background circle
                drawCircle(
                    color = unshadedColor,
                    radius = radius,
                    center = center
                )

                // Draw shaded arcs
                for (i in 0 until shadedParts) {
                    val startAngle = -90f + (i * sweepAngle)
                    drawArc(
                        color = shadedColor,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = true,
                        topLeft = Offset(center.x - radius, center.y - radius),
                        size = Size(radius * 2, radius * 2)
                    )
                }

                // Draw slice dividing lines
                for (i in 0 until totalParts) {
                    val angleRad = Math.toRadians((-90.0 + (i * sweepAngle)))
                    val endX = (center.x + radius * Math.cos(angleRad)).toFloat()
                    val endY = (center.y + radius * Math.sin(angleRad)).toFloat()
                    drawLine(
                        color = borderColor,
                        start = center,
                        end = Offset(endX, endY),
                        strokeWidth = 4f
                    )
                }

                // Outer border circle
                drawCircle(
                    color = borderColor,
                    radius = radius,
                    center = center,
                    style = Stroke(width = 6f)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "${fraction.numerator} of ${fraction.denominator} parts shaded",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
