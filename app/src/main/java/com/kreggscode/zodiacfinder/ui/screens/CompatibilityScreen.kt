package com.kreggscode.zodiacfinder.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kreggscode.zodiacfinder.data.model.ZodiacSign
import com.kreggscode.zodiacfinder.ui.components.*
import com.kreggscode.zodiacfinder.ui.theme.*
import com.kreggscode.zodiacfinder.ui.viewmodel.CompatibilityViewModel

@Composable
fun CompatibilityScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: CompatibilityViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surface
                    )
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header with Back Button
            item {
                TopBarWithBack(
                    title = "💕 Compatibility",
                    subtitle = "Discover your cosmic connection",
                    onBackClick = onNavigateBack
                )
            }
            
            // Your Sign Selection
            item {
                Text(
                    text = "Your Sign",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(ZodiacSign.values()) { sign ->
                        ZodiacSignCard(
                            sign = sign,
                            onClick = { viewModel.selectSign1(sign) },
                            modifier = Modifier.width(100.dp).height(120.dp),
                            isSelected = sign == uiState.sign1
                        )
                    }
                }
            }
            
            // Partner's Sign Selection
            item {
                Text(
                    text = "Partner's Sign",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(ZodiacSign.values()) { sign ->
                        ZodiacSignCard(
                            sign = sign,
                            onClick = { viewModel.selectSign2(sign) },
                            modifier = Modifier.width(100.dp).height(120.dp),
                            isSelected = sign == uiState.sign2
                        )
                    }
                }
            }
            
            // Loading State
            if (uiState.isLoading) {
                item {
                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CustomLoadingIndicator(
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Analyzing cosmic compatibility...",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }
            }
            
            // Compatibility Results
            uiState.result?.let { result ->
                // Overall Score
                item {
                    GradientGlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        gradientColors = listOf(
                            TertiaryLight.copy(alpha = 0.4f),
                            SecondaryLight.copy(alpha = 0.3f)
                        )
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "💫",
                                fontSize = 48.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "${result.overallScore}%",
                                fontSize = 56.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Overall Compatibility",
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "${result.sign1.emoji} ${result.sign1.displayName} + ${result.sign2.emoji} ${result.sign2.displayName}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
                
                // Detailed Scores
                item {
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            Text(
                                text = "Detailed Analysis",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                ScoreIndicator(
                                    label = "Love",
                                    score = result.loveScore,
                                    color = TertiaryLight
                                )
                                ScoreIndicator(
                                    label = "Marriage",
                                    score = result.marriageScore,
                                    color = SecondaryLight
                                )
                            }
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                ScoreIndicator(
                                    label = "Career",
                                    score = result.careerScore,
                                    color = CosmicBlue
                                )
                                ScoreIndicator(
                                    label = "Friendship",
                                    score = result.friendshipScore,
                                    color = MysticPurple
                                )
                            }
                        }
                    }
                }
                
                // Insights
                item {
                    GradientGlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        gradientColors = listOf(
                            MysticPurple.copy(alpha = 0.2f),
                            CosmicBlue.copy(alpha = 0.1f)
                        )
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = "💡",
                                    fontSize = 32.sp
                                )
                                Text(
                                    text = "Insights",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MysticPurple
                                )
                            }
                            
                            HorizontalDivider(
                                color = MysticPurple.copy(alpha = 0.3f),
                                thickness = 2.dp
                            )
                            
                            Text(
                                text = result.insights,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.95f),
                                lineHeight = 24.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }
                }
                
                // Suggestions
                if (result.suggestions.isNotEmpty()) {
                    item {
                        GradientGlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            gradientColors = listOf(
                                LuckyGold.copy(alpha = 0.2f),
                                LuckyGold.copy(alpha = 0.05f)
                            )
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Text(
                                        text = "✨",
                                        fontSize = 32.sp
                                    )
                                    Text(
                                        text = "Suggestions",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = LuckyGold
                                    )
                                }
                                
                                HorizontalDivider(
                                    color = LuckyGold.copy(alpha = 0.3f),
                                    thickness = 2.dp
                                )
                                
                                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    result.suggestions.forEach { suggestion ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Text(
                                                text = "🌟",
                                                fontSize = 16.sp
                                            )
                                            Text(
                                                text = suggestion,
                                                fontSize = 15.sp,
                                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                                                lineHeight = 22.sp,
                                                modifier = Modifier.weight(1f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                
                // Reset Button
                item {
                    NeumorphicButton(
                        onClick = { viewModel.reset() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("🔄 Check Another Match")
                    }
                }
            }
            
            // Error State
            uiState.error?.let { error ->
                item {
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "⚠️",
                                fontSize = 48.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = error,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
            
            // Bottom padding
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}
