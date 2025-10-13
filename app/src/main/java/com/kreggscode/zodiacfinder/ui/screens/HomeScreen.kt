package com.kreggscode.zodiacfinder.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kreggscode.zodiacfinder.data.model.ZodiacSign
import com.kreggscode.zodiacfinder.ui.components.*
import com.kreggscode.zodiacfinder.ui.theme.*
import com.kreggscode.zodiacfinder.ui.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToZodiacFinder: () -> Unit,
    onNavigateToHoroscope: () -> Unit,
    onNavigateToCompatibility: () -> Unit,
    onNavigateToReadingHub: () -> Unit,
    onNavigateToProfile: () -> Unit = {},
    onNavigateToTarot: () -> Unit = {},
    onNavigateToMoonPhase: () -> Unit = {},
    onNavigateToGemstones: () -> Unit = {},
    onNavigateToBirthChart: () -> Unit = {},
    onNavigateToElements: () -> Unit = {},
    onNavigateToCrystals: () -> Unit = {},
    onNavigateToPalmReading: () -> Unit = {},
    onNavigateToAnalyzePalm: () -> Unit = {},
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit,
    viewModel: HomeViewModel = viewModel()
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
            // Header with theme toggle and profile
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "✨ ZodiacAI",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Discover Your Cosmic Journey",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        GlassIconButton(onClick = onNavigateToProfile) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        
                        GlassIconButton(onClick = onThemeToggle) {
                            Icon(
                                imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Toggle theme",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
            
            // Daily Horoscope Preview
            item {
                GradientGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    gradientColors = listOf(
                        GradientStart.copy(alpha = 0.3f),
                        GradientEnd.copy(alpha = 0.3f)
                    )
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 12.dp)
                        ) {
                            Text(
                                text = "🌟",
                                fontSize = 32.sp,
                                modifier = Modifier.padding(end = 12.dp)
                            )
                            Column {
                                Text(
                                    text = "Daily Horoscope",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Your cosmic guidance for today",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }
                        
                        if (uiState.userSign != null) {
                            Text(
                                text = "${uiState.userSign!!.emoji} ${uiState.userSign!!.displayName}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            
                            if (uiState.dailyHoroscope != null) {
                                Text(
                                    text = uiState.dailyHoroscope!!.overall.take(150) + "...",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                    lineHeight = 20.sp
                                )
                            }
                        } else {
                            Text(
                                text = "Find your zodiac sign to get your daily horoscope",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        NeumorphicButton(
                            onClick = onNavigateToHoroscope,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("View Full Horoscope")
                        }
                    }
                }
            }
            
            // Quick Actions
            item {
                Text(
                    text = "Quick Actions",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            item {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.height(1000.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        QuickActionCard(
                            title = "Find Your Sign",
                            icon = "🔮",
                            color = MysticPurple,
                            onClick = onNavigateToZodiacFinder
                        )
                    }
                    
                    item {
                        QuickActionCard(
                            title = "Compatibility",
                            icon = "💕",
                            color = TertiaryLight,
                            onClick = onNavigateToCompatibility
                        )
                    }
                    
                    item {
                        QuickActionCard(
                            title = "Birth Chart",
                            icon = "⭐",
                            color = MysticPurple,
                            onClick = onNavigateToBirthChart
                        )
                    }
                    
                    item {
                        QuickActionCard(
                            title = "Analyze Palm",
                            icon = "📸",
                            color = LuckyGold,
                            onClick = onNavigateToAnalyzePalm,
                            badge = "NEW"
                        )
                    }
                    
                    item {
                        QuickActionCard(
                            title = "Reading Hub",
                            icon = "📚",
                            color = MysticPurple,
                            onClick = onNavigateToReadingHub,
                            badge = "ALL"
                        )
                    }
                    
                    item {
                        QuickActionCard(
                            title = "Tarot Cards",
                            icon = "🎴",
                            color = CosmicBlue,
                            onClick = onNavigateToTarot,
                            badge = "78 Cards"
                        )
                    }
                    
                    item {
                        QuickActionCard(
                            title = "Crystals",
                            icon = "💎",
                            color = FireElement,
                            onClick = onNavigateToCrystals,
                            badge = "NEW"
                        )
                    }
                    
                    item {
                        QuickActionCard(
                            title = "Palm Reading",
                            icon = "🖐️",
                            color = SecondaryLight,
                            onClick = onNavigateToPalmReading,
                            badge = "NEW"
                        )
                    }
                    
                    item {
                        QuickActionCard(
                            title = "Gemstones",
                            icon = "💍",
                            color = TertiaryLight,
                            onClick = onNavigateToGemstones
                        )
                    }
                    
                    item {
                        QuickActionCard(
                            title = "Moon Phases",
                            icon = "🌙",
                            color = WaterElement,
                            onClick = onNavigateToMoonPhase
                        )
                    }
                }
            }
            
            // Zodiac Elements Info
            item {
                Text(
                    text = "Zodiac Elements",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ElementCard(
                        emoji = "🔥",
                        element = "Fire",
                        signs = "Aries, Leo, Sagittarius",
                        description = "Passionate, dynamic, and temperamental. Fire signs are driven by enthusiasm and inspiration.",
                        color = FireElement,
                        onClick = onNavigateToElements
                    )
                    ElementCard(
                        emoji = "🌍",
                        element = "Earth",
                        signs = "Taurus, Virgo, Capricorn",
                        description = "Grounded, practical, and reliable. Earth signs are stable and focused on the material world.",
                        color = EarthElement,
                        onClick = onNavigateToElements
                    )
                    ElementCard(
                        emoji = "💨",
                        element = "Air",
                        signs = "Gemini, Libra, Aquarius",
                        description = "Intellectual, communicative, and social. Air signs are all about ideas and connections.",
                        color = AirElement,
                        onClick = onNavigateToElements
                    )
                    ElementCard(
                        emoji = "💧",
                        element = "Water",
                        signs = "Cancer, Scorpio, Pisces",
                        description = "Emotional, intuitive, and sensitive. Water signs are deeply connected to their feelings.",
                        color = WaterElement,
                        onClick = onNavigateToElements
                    )
                    ElementCard(
                        emoji = "⚡",
                        element = "Ether (Spirit)",
                        signs = "The Fifth Element",
                        description = "The mystical force that binds all elements together. Represents consciousness and divine energy.",
                        color = MysticPurple,
                        onClick = onNavigateToElements
                    )
                }
            }
            
            // Bottom padding for floating nav bar
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun ElementCard(
    emoji: String,
    element: String,
    signs: String,
    description: String,
    color: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit = {}
) {
    GradientGlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        gradientColors = listOf(
            color.copy(alpha = 0.3f),
            color.copy(alpha = 0.1f)
        )
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = emoji,
                    fontSize = 48.sp,
                    modifier = Modifier.padding(end = 16.dp)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = element,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = signs,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                lineHeight = 18.sp
            )
        }
    }
}
