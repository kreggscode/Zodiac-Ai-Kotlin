package com.kreggscode.zodiacfinder.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kreggscode.zodiacfinder.data.model.MoonPhase
import com.kreggscode.zodiacfinder.ui.components.*
import com.kreggscode.zodiacfinder.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MoonPhaseScreen(
    onNavigateBack: () -> Unit = {}
) {
    val currentPhase = remember { MoonPhase.current() }
    val upcomingPhases = remember { MoonPhase.getUpcoming() }
    val currentDate = remember { SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault()).format(Date()) }
    
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
            // Header
            item {
                TopBarWithBack(
                    title = "🌙 Moon Phases",
                    subtitle = "Lunar guidance and wisdom",
                    onBackClick = onNavigateBack
                )
            }
            
            // Current Date
            item {
                GradientGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    gradientColors = listOf(
                        SecondaryLight.copy(alpha = 0.2f),
                        CosmicBlue.copy(alpha = 0.1f)
                    )
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "📅",
                            fontSize = 24.sp
                        )
                        Text(
                            text = currentDate,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
            
            // Current Moon Phase
            item {
                GradientGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    gradientColors = listOf(
                        MysticPurple.copy(alpha = 0.3f),
                        SecondaryLight.copy(alpha = 0.2f)
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Current Moon Phase",
                            fontSize = 16.sp,
                            color = MysticPurple,
                            fontWeight = FontWeight.SemiBold
                        )
                        
                        Text(
                            text = currentPhase.emoji,
                            fontSize = 80.sp
                        )
                        
                        Text(
                            text = currentPhase.displayName,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
            
            // Current Phase Details
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text(
                            text = "🌙 About This Phase",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                        )
                        
                        Text(
                            text = currentPhase.description,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                            lineHeight = 24.sp
                        )
                    }
                }
            }
            
            // Energy
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
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "✨",
                                fontSize = 28.sp
                            )
                            Text(
                                text = "Current Energy",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = LuckyGold
                            )
                        }
                        
                        HorizontalDivider(
                            color = LuckyGold.copy(alpha = 0.3f),
                            thickness = 2.dp
                        )
                        
                        Text(
                            text = currentPhase.energy,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                            lineHeight = 22.sp
                        )
                    }
                }
            }
            
            // Recommendations
            item {
                GradientGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    gradientColors = listOf(
                        CosmicBlue.copy(alpha = 0.2f),
                        MysticPurple.copy(alpha = 0.1f)
                    )
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "💫",
                                fontSize = 28.sp
                            )
                            Text(
                                text = "Recommended Actions",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = CosmicBlue
                            )
                        }
                        
                        HorizontalDivider(
                            color = CosmicBlue.copy(alpha = 0.3f),
                            thickness = 2.dp
                        )
                        
                        Text(
                            text = currentPhase.recommendations,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                            lineHeight = 22.sp
                        )
                    }
                }
            }
            
            // Upcoming Phases
            item {
                Text(
                    text = "🔮 Upcoming Phases",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            
            items(upcomingPhases) { phase ->
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = phase.emoji,
                            fontSize = 48.sp
                        )
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = phase.displayName,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Coming Soon",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
            
            // Moon Phase Guide
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "📖 Moon Phase Guide",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                        )
                        
                        Text(
                            text = "The Moon cycles through 8 primary phases approximately every 29.5 days. Each phase brings different energies that can influence our emotions, intuition, and manifestation power. Working with the moon's natural rhythm can help you align your intentions and actions for maximum effect.",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }
    }
}
