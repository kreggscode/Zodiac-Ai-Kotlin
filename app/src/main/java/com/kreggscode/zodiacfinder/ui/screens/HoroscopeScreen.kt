package com.kreggscode.zodiacfinder.ui.screens

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
import com.kreggscode.zodiacfinder.ui.viewmodel.HoroscopeViewModel

@Composable
fun HoroscopeScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: HoroscopeViewModel = viewModel()
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
                    title = "🌟 Daily Horoscope",
                    subtitle = "Your cosmic guidance for today",
                    onBackClick = onNavigateBack
                )
            }
            
            // Zodiac Sign Selector
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(ZodiacSign.values()) { sign ->
                        ZodiacSignCard(
                            sign = sign,
                            onClick = { viewModel.selectSign(sign) },
                            modifier = Modifier.width(120.dp).height(140.dp),
                            isSelected = sign == uiState.selectedSign
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
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                CustomLoadingIndicator(
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Consulting the stars...",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }
            }
            
            // Horoscope Content
            uiState.horoscope?.let { horoscope ->
                // Lucky Info Card
                item {
                    GradientGlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        gradientColors = listOf(
                            LuckyGold.copy(alpha = 0.3f),
                            LuckyGold.copy(alpha = 0.1f)
                        )
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "🍀",
                                    fontSize = 32.sp
                                )
                                Text(
                                    text = horoscope.luckyNumber.toString(),
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Lucky Number",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                            
                            Divider(
                                modifier = Modifier
                                    .height(60.dp)
                                    .width(1.dp),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                            )
                            
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "🎨",
                                    fontSize = 32.sp
                                )
                                Text(
                                    text = horoscope.luckyColor,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Lucky Color",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }
                
                // Formatted Horoscope Content
                item {
                    FormattedHoroscopeCard(
                        sign = uiState.selectedSign,
                        overall = horoscope.overall,
                        love = horoscope.love,
                        career = horoscope.career,
                        finance = horoscope.finance,
                        health = horoscope.health,
                        luckyColor = horoscope.luckyColor,
                        luckyNumber = horoscope.luckyNumber
                    )
                }
                
                // Refresh Button
                item {
                    NeumorphicButton(
                        onClick = { viewModel.loadHoroscope(uiState.selectedSign) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("🔄 Refresh Horoscope")
                    }
                }
            }
            
            // Error State
            uiState.error?.let { error ->
                item {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
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
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            NeumorphicButton(
                                onClick = { viewModel.loadHoroscope(uiState.selectedSign) }
                            ) {
                                Text("Try Again")
                            }
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
