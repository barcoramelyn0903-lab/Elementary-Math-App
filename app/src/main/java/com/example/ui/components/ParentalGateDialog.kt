package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.JungleMangoOrange
import com.example.ui.theme.MintSuccess
import com.example.ui.theme.TextDark
import kotlin.random.Random

@Composable
fun ParentalGateDialog(
    onDismiss: () -> Unit,
    onSuccess: () -> Unit,
    parentPin: String = "1234"
) {
    // Generate an adult verification challenge
    val factor1 = remember { Random.nextInt(7, 13) }
    val factor2 = remember { Random.nextInt(6, 12) }
    val correctAnswer = remember { factor1 * factor2 }

    var inputAnswer by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(16.dp, RoundedCornerShape(28.dp))
                .clip(RoundedCornerShape(28.dp))
                .background(Color.White)
                .border(3.dp, JungleMangoOrange, RoundedCornerShape(28.dp))
                .padding(24.dp)
                .testTag("parental_gate_dialog"),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "🔒 Grown-Ups Only",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Black
                    ),
                    color = JungleMangoOrange
                )

                Text(
                    text = "Please solve this problem or enter your parent PIN to access learning reports & parental controls.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = TextDark
                )

                // Math Challenge Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFF1F5F9))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "What is $factor1 × $factor2 = ?",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                }

                OutlinedTextField(
                    value = inputAnswer,
                    onValueChange = {
                        inputAnswer = it
                        isError = false
                    },
                    label = { Text("Enter Answer or PIN (default 1234)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = isError,
                    supportingText = {
                        if (isError) {
                            Text("Incorrect answer. Please try again.", color = Color.Red)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("parent_gate_input")
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    KidButton(
                        text = "Cancel",
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        backgroundColor = Color(0xFFE2E8F0),
                        darkShadowColor = Color.Gray,
                        textColor = Color.Black,
                        height = 48.dp,
                        testTag = "parent_gate_cancel"
                    )

                    KidButton(
                        text = "Unlock",
                        onClick = {
                            val trimmed = inputAnswer.trim()
                            if (trimmed == correctAnswer.toString() || trimmed == parentPin) {
                                onSuccess()
                            } else {
                                isError = true
                            }
                        },
                        modifier = Modifier.weight(1f),
                        backgroundColor = MintSuccess,
                        darkShadowColor = Color(0xFF047857),
                        textColor = Color.White,
                        height = 48.dp,
                        testTag = "parent_gate_submit"
                    )
                }
            }
        }
    }
}
