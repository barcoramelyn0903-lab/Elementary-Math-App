package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.UserProfileEntity
import com.example.data.model.*
import com.example.ui.components.KidButton
import com.example.ui.components.KidCard
import com.example.ui.components.MathQuestTopBar
import com.example.ui.components.VoiceSpeakerButton
import com.example.ui.components.vibrantPolkaDotBackground
import com.example.ui.theme.*
import com.example.ui.viewmodel.MathQuestViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvatarShopScreen(
    viewModel: MathQuestViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val userProfile by viewModel.userProfile.collectAsState()
    val unlockedItemIds by viewModel.unlockedItemIds.collectAsState()
    val unlockedBadgeIds by viewModel.unlockedBadgeIds.collectAsState()
    val isSpeaking by viewModel.voiceNarrator.isSpeaking.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }
    val currentTheme = if (userProfile.currentThemeId == "space") WorldTheme.SPACE else WorldTheme.JUNGLE
    val currentAvatar = DEFAULT_AVATARS.find { it.id == userProfile.equippedAvatarId } ?: DEFAULT_AVATARS.first()
    val currentHat = DEFAULT_ACCESSORIES.find { it.id == userProfile.equippedHatId }

    Scaffold(
        topBar = {
            MathQuestTopBar(
                userProfile = userProfile,
                onAvatarClick = {},
                onParentDashboardClick = onBack,
                onToggleSound = { viewModel.toggleSound() }
            )
        },
        containerColor = if (currentTheme == WorldTheme.SPACE) SpaceDark else VibrantLime
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .vibrantPolkaDotBackground(
                    dotColor = if (currentTheme == WorldTheme.SPACE) Color.White.copy(alpha = 0.08f) else ForestGreenDark.copy(alpha = 0.12f)
                )
                .padding(horizontal = 16.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(CardWhite)
                            .border(2.dp, ForestGreenBorder, CircleShape)
                            .testTag("shop_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = ForestGreenDark
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "REWARDS CLUB",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black
                        ),
                        color = if (currentTheme == WorldTheme.SPACE) Color.White else ForestGreenDark
                    )
                }

                VoiceSpeakerButton(
                    isSpeaking = isSpeaking,
                    isVoiceEnabled = userProfile.voiceEnabled,
                    onClick = {
                        viewModel.voiceNarrator.speak("Welcome to the Rewards Club! Use your gems to unlock cool animal explorers and hats!")
                    }
                )
            }

            // Equipped Character Card
            KidCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                backgroundColor = CardWhite,
                borderColor = CardBorderGray,
                bottomBorderThickness = 6.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(VibrantOrange)
                            .border(3.dp, CardWhite, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = currentAvatar.emoji, fontSize = 38.sp)
                        if (currentHat != null) {
                            Text(
                                text = currentHat.emoji,
                                fontSize = 20.sp,
                                modifier = Modifier.align(Alignment.TopEnd)
                            )
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${currentAvatar.name} the ${currentAvatar.title}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            color = TextDarkSlate
                        )
                        if (currentHat != null) {
                            Text(
                                text = "Wearing: ${currentHat.emoji} ${currentHat.name}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = VibrantOrangeDark,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = currentAvatar.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMutedSlate
                        )
                    }
                }
            }

            // Tab Selector Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("🦁 Avatars", "👑 Gear", "🏆 Badges").forEachIndexed { index, title ->
                    val isSelected = selectedTab == index
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .drawBehind {
                                if (isSelected) {
                                    drawRoundRect(
                                        color = VibrantGoldDark,
                                        topLeft = Offset(0f, size.height - 3.dp.toPx()),
                                        size = Size(size.width, 3.dp.toPx()),
                                        cornerRadius = CornerRadius(14.dp.toPx(), 14.dp.toPx())
                                    )
                                }
                            }
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isSelected) VibrantGold else CardWhite)
                            .border(2.dp, if (isSelected) VibrantGoldBorder else CardBorderGray, RoundedCornerShape(14.dp))
                            .clickable { selectedTab = index }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            color = TextDarkSlate
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Tab Content
            when (selectedTab) {
                0 -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 32.dp)
                    ) {
                        items(DEFAULT_AVATARS) { avatar ->
                            val isUnlocked = avatar.isDefaultUnlocked || unlockedItemIds.contains(avatar.id)
                            val isEquipped = userProfile.equippedAvatarId == avatar.id

                            VibrantAvatarItemCard(
                                avatar = avatar,
                                isUnlocked = isUnlocked,
                                isEquipped = isEquipped,
                                userCoins = userProfile.coins,
                                onEquip = { viewModel.equipAvatar(avatar.id) },
                                onPurchase = {
                                    viewModel.purchaseAvatar(
                                        avatar = avatar,
                                        onSuccess = {
                                            Toast.makeText(context, "Unlocked ${avatar.name}!", Toast.LENGTH_SHORT).show()
                                        },
                                        onError = {
                                            Toast.makeText(context, "Need more gems! Solve more quests.", Toast.LENGTH_SHORT).show()
                                        }
                                    )
                                }
                            )
                        }
                    }
                }
                1 -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 32.dp)
                    ) {
                        items(DEFAULT_ACCESSORIES) { accessory ->
                            val isUnlocked = unlockedItemIds.contains(accessory.id)
                            val isEquipped = userProfile.equippedHatId == accessory.id

                            VibrantAccessoryCard(
                                accessory = accessory,
                                isUnlocked = isUnlocked,
                                isEquipped = isEquipped,
                                userCoins = userProfile.coins,
                                onEquip = {
                                    if (isEquipped) viewModel.equipAccessory(null) else viewModel.equipAccessory(accessory.id)
                                },
                                onPurchase = {
                                    viewModel.purchaseAccessory(
                                        accessory = accessory,
                                        onSuccess = {
                                            Toast.makeText(context, "Unlocked ${accessory.name}!", Toast.LENGTH_SHORT).show()
                                        },
                                        onError = {
                                            Toast.makeText(context, "Need more gems!", Toast.LENGTH_SHORT).show()
                                        }
                                    )
                                }
                            )
                        }
                    }
                }
                2 -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 32.dp)
                    ) {
                        items(DEFAULT_BADGES) { badge ->
                            val isUnlocked = unlockedBadgeIds.contains(badge.id)
                            VibrantBadgeCard(badge = badge, isUnlocked = isUnlocked)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VibrantAvatarItemCard(
    avatar: Avatar,
    isUnlocked: Boolean,
    isEquipped: Boolean,
    userCoins: Int,
    onEquip: () -> Unit,
    onPurchase: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawRoundRect(
                    color = CardBorderGray,
                    topLeft = Offset(0f, size.height - 5.dp.toPx()),
                    size = Size(size.width, 5.dp.toPx()),
                    cornerRadius = CornerRadius(22.dp.toPx(), 22.dp.toPx())
                )
            }
            .clip(RoundedCornerShape(22.dp))
            .background(CardWhite)
            .border(2.dp, if (isEquipped) MintSuccess else CardBorderGray, RoundedCornerShape(22.dp))
            .padding(14.dp)
            .testTag("avatar_card_${avatar.id}")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(VibrantGold.copy(alpha = if (isUnlocked) 1f else 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = avatar.emoji, fontSize = 30.sp)
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = avatar.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = TextDarkSlate
                )
                Text(
                    text = avatar.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = VibrantOrangeDark,
                    fontWeight = FontWeight.Bold
                )
            }

            if (isEquipped) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MintSuccess)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "EQUIPPED",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 11.sp
                    )
                }
            } else if (isUnlocked) {
                KidButton(
                    text = "Equip",
                    onClick = onEquip,
                    backgroundColor = VibrantGold,
                    darkShadowColor = VibrantGoldDark,
                    textColor = TextDarkSlate,
                    height = 42.dp,
                    testTag = "equip_avatar_${avatar.id}"
                )
            } else {
                KidButton(
                    text = "💎 ${avatar.costCoins}",
                    onClick = onPurchase,
                    backgroundColor = if (userCoins >= avatar.costCoins) VibrantGold else Color(0xFFE2E8F0),
                    darkShadowColor = if (userCoins >= avatar.costCoins) VibrantGoldDark else Color(0xFFCBD5E1),
                    textColor = TextDarkSlate,
                    enabled = userCoins >= avatar.costCoins,
                    height = 42.dp,
                    testTag = "buy_avatar_${avatar.id}"
                )
            }
        }
    }
}

@Composable
fun VibrantAccessoryCard(
    accessory: Accessory,
    isUnlocked: Boolean,
    isEquipped: Boolean,
    userCoins: Int,
    onEquip: () -> Unit,
    onPurchase: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawRoundRect(
                    color = CardBorderGray,
                    topLeft = Offset(0f, size.height - 5.dp.toPx()),
                    size = Size(size.width, 5.dp.toPx()),
                    cornerRadius = CornerRadius(22.dp.toPx(), 22.dp.toPx())
                )
            }
            .clip(RoundedCornerShape(22.dp))
            .background(CardWhite)
            .border(2.dp, if (isEquipped) MintSuccess else CardBorderGray, RoundedCornerShape(22.dp))
            .padding(14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFEF3C7)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = accessory.emoji, fontSize = 28.sp)
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = accessory.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = TextDarkSlate
                )
                Text(
                    text = accessory.description,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 12.sp,
                    color = TextMutedSlate
                )
            }

            if (isEquipped) {
                KidButton(
                    text = "Unequip",
                    onClick = onEquip,
                    backgroundColor = Color(0xFFE2E8F0),
                    darkShadowColor = Color(0xFFCBD5E1),
                    height = 40.dp
                )
            } else if (isUnlocked) {
                KidButton(
                    text = "Wear",
                    onClick = onEquip,
                    backgroundColor = MintSuccess,
                    darkShadowColor = Color(0xFF047857),
                    textColor = Color.White,
                    height = 40.dp
                )
            } else {
                KidButton(
                    text = "💎 ${accessory.costCoins}",
                    onClick = onPurchase,
                    backgroundColor = if (userCoins >= accessory.costCoins) VibrantGold else Color(0xFFE2E8F0),
                    darkShadowColor = if (userCoins >= accessory.costCoins) VibrantGoldDark else Color(0xFFCBD5E1),
                    textColor = TextDarkSlate,
                    enabled = userCoins >= accessory.costCoins,
                    height = 40.dp
                )
            }
        }
    }
}

@Composable
fun VibrantBadgeCard(
    badge: Badge,
    isUnlocked: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawRoundRect(
                    color = CardBorderGray,
                    topLeft = Offset(0f, size.height - 5.dp.toPx()),
                    size = Size(size.width, 5.dp.toPx()),
                    cornerRadius = CornerRadius(22.dp.toPx(), 22.dp.toPx())
                )
            }
            .clip(RoundedCornerShape(22.dp))
            .background(CardWhite)
            .border(2.dp, if (isUnlocked) VibrantGold else CardBorderGray, RoundedCornerShape(22.dp))
            .padding(14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(if (isUnlocked) VibrantGold.copy(alpha = 0.25f) else Color(0xFFE2E8F0)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isUnlocked) badge.iconEmoji else "🔒",
                    fontSize = 26.sp
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = badge.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = if (isUnlocked) TextDarkSlate else Color.Gray
                )
                Text(
                    text = badge.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMutedSlate
                )
            }

            if (isUnlocked) {
                Text(text = "⭐ EARNED", fontWeight = FontWeight.Black, fontSize = 11.sp, color = VibrantGoldDark)
            }
        }
    }
}
