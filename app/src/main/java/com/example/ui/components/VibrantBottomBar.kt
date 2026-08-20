package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun VibrantBottomBar(
    currentRoute: String,
    onNavigateHome: () -> Unit,
    onNavigateMap: () -> Unit,
    onNavigatePlay: () -> Unit,
    onNavigateShop: () -> Unit,
    onNavigateParent: () -> Unit,
    modifier: Modifier = Modifier
) {
    val barShape = RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Bottom Bar Background & Navigation Items
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(12.dp, shape = barShape)
                .clip(barShape)
                .background(CardWhite)
                .border(1.dp, SurfaceBorderLight, barShape)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Home
                NavIconButton(
                    iconEmoji = "🏠",
                    label = "Home",
                    isSelected = currentRoute == "home",
                    onClick = onNavigateHome,
                    testTag = "nav_home"
                )

                // Map
                NavIconButton(
                    iconEmoji = "🗺️",
                    label = "Map",
                    isSelected = currentRoute == "world_map",
                    onClick = onNavigateMap,
                    testTag = "nav_map"
                )

                // Spacer for center button
                Spacer(modifier = Modifier.width(60.dp))

                // Rewards / Badges
                NavIconButton(
                    iconEmoji = "🎁",
                    label = "Rewards",
                    isSelected = currentRoute == "avatar_shop",
                    onClick = onNavigateShop,
                    testTag = "nav_shop"
                )

                // Parents
                NavIconButton(
                    iconEmoji = "🔒",
                    label = "Parents",
                    isSelected = currentRoute == "parent_dashboard",
                    onClick = onNavigateParent,
                    testTag = "nav_parents"
                )
            }
        }

        // Center Floating Play Action Button
        Box(
            modifier = Modifier
                .offset(y = (-20).dp)
                .size(68.dp)
                .shadow(10.dp, CircleShape)
                .clip(CircleShape)
                .background(VibrantOrange)
                .border(5.dp, CardWhite, CircleShape)
                .clickable { onNavigatePlay() }
                .testTag("center_floating_action_button"),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "⚡",
                fontSize = 32.sp
            )
        }
    }
}

@Composable
private fun NavIconButton(
    iconEmoji: String,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .testTag(testTag)
    ) {
        Text(
            text = iconEmoji,
            fontSize = 20.sp,
            color = if (isSelected) VibrantOrange else TextMutedSlate.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label.uppercase(),
            fontSize = 10.sp,
            fontWeight = FontWeight.Black,
            color = if (isSelected) VibrantOrange else TextMutedSlate
        )
    }
}
