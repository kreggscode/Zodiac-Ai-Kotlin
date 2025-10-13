package com.kreggscode.zodiacfinder.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kreggscode.zodiacfinder.data.model.ZodiacSign
import com.kreggscode.zodiacfinder.ui.components.*
import com.kreggscode.zodiacfinder.ui.theme.*

@Composable
fun EncyclopediaScreenNew(
    onNavigateBack: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedSign by remember { mutableStateOf<ZodiacSign?>(null) }
    
    val filteredSigns = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            ZodiacSign.values().toList()
        } else {
            ZodiacSign.values().filter {
                it.displayName.contains(searchQuery, ignoreCase = true)
            }
        }
    }
    
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
                    title = "🔮 Encyclopedia",
                    subtitle = "Explore the wisdom of the stars",
                    onBackClick = onNavigateBack
                )
            }
            
            // Search Bar
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Search zodiac signs...") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        ),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                        singleLine = true
                    )
                }
            }
            
            // Grid of Zodiac Cards
            item {
                val gridHeight = ((filteredSigns.size + 1) / 2) * 200
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.height(gridHeight.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    userScrollEnabled = false
                ) {
                    items(filteredSigns) { sign ->
                        ZodiacSignCard(
                            sign = sign,
                            onClick = { selectedSign = sign },
                            modifier = Modifier.height(180.dp)
                        )
                    }
                }
            }
        }
        
        // Detail View Overlay
        selectedSign?.let { sign ->
            ZodiacDetailOverlay(
                sign = sign,
                onDismiss = { selectedSign = null }
            )
        }
    }
}

@Composable
private fun ZodiacDetailOverlay(
    sign: ZodiacSign,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header with Back Button
            item {
                TopBarWithBack(
                    title = "${sign.symbol} ${sign.displayName}",
                    subtitle = sign.element.name.lowercase().replaceFirstChar { it.uppercase() } + " Sign",
                    onBackClick = onDismiss
                )
            }
            
            // Sign Info Card
            item {
                GradientGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    gradientColors = listOf(
                        when (sign.element) {
                            com.kreggscode.zodiacfinder.data.model.ZodiacElement.FIRE -> FireElement.copy(alpha = 0.3f)
                            com.kreggscode.zodiacfinder.data.model.ZodiacElement.EARTH -> EarthElement.copy(alpha = 0.3f)
                            com.kreggscode.zodiacfinder.data.model.ZodiacElement.AIR -> AirElement.copy(alpha = 0.3f)
                            com.kreggscode.zodiacfinder.data.model.ZodiacElement.WATER -> WaterElement.copy(alpha = 0.3f)
                        },
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.1f)
                    )
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = sign.symbol,
                            fontSize = 72.sp
                        )
                        Text(
                            text = sign.displayName,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Element: ${sign.element.name.lowercase().replaceFirstChar { it.uppercase() }}",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    }
                }
            }
            
            // Description
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "✨ About",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = getDescriptionForSign(sign),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                            lineHeight = 22.sp
                        )
                    }
                }
            }
        }
    }
}

private fun getDescriptionForSign(sign: ZodiacSign): String {
    return when (sign) {
        ZodiacSign.ARIES -> "Bold, ambitious, and confident. Aries are natural leaders who love challenges and new beginnings."
        ZodiacSign.TAURUS -> "Reliable, patient, and devoted. Taurus values stability, comfort, and the finer things in life."
        ZodiacSign.GEMINI -> "Curious, adaptable, and communicative. Gemini thrives on variety and intellectual stimulation."
        ZodiacSign.CANCER -> "Intuitive, emotional, and nurturing. Cancer is deeply connected to home, family, and feelings."
        ZodiacSign.LEO -> "Confident, generous, and charismatic. Leo loves to shine and inspire others with their warmth."
        ZodiacSign.VIRGO -> "Analytical, practical, and detail-oriented. Virgo seeks perfection and loves to be of service."
        ZodiacSign.LIBRA -> "Diplomatic, charming, and fair-minded. Libra values harmony, beauty, and balanced relationships."
        ZodiacSign.SCORPIO -> "Passionate, resourceful, and intense. Scorpio seeks deep transformation and authentic connections."
        ZodiacSign.SAGITTARIUS -> "Optimistic, adventurous, and philosophical. Sagittarius loves freedom, travel, and seeking truth."
        ZodiacSign.CAPRICORN -> "Ambitious, disciplined, and responsible. Capricorn is driven to achieve and build lasting success."
        ZodiacSign.AQUARIUS -> "Independent, innovative, and humanitarian. Aquarius thinks outside the box and values progress."
        ZodiacSign.PISCES -> "Compassionate, artistic, and intuitive. Pisces is deeply empathetic and connected to the spiritual realm."
    }
}
