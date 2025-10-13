package com.kreggscode.zodiacfinder.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EncyclopediaScreen() {
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
        if (selectedSign != null) {
            // Detail View
            ZodiacDetailView(
                sign = selectedSign!!,
                onBack = { selectedSign = null }
            )
        } else {
            // List View
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                item {
                    Column {
                        Text(
                            text = "📚 Encyclopedia",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Explore all zodiac signs",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
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
                            singleLine = true
                        )
                    }
                }
                
                // Zodiac Signs Grid
                items(filteredSigns) { sign ->
                    ZodiacEncyclopediaCard(
                        sign = sign,
                        onClick = { selectedSign = sign }
                    )
                }
                
                // Bottom padding
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

@Composable
private fun ZodiacEncyclopediaCard(
    sign: ZodiacSign,
    onClick: () -> Unit
) {
    val backgroundColor = when (sign.element) {
        com.kreggscode.zodiacfinder.data.model.ZodiacElement.FIRE -> FireElement.copy(alpha = 0.3f)
        com.kreggscode.zodiacfinder.data.model.ZodiacElement.EARTH -> EarthElement.copy(alpha = 0.3f)
        com.kreggscode.zodiacfinder.data.model.ZodiacElement.AIR -> AirElement.copy(alpha = 0.3f)
        com.kreggscode.zodiacfinder.data.model.ZodiacElement.WATER -> WaterElement.copy(alpha = 0.3f)
    }
    
    GradientGlassCard(
        modifier = Modifier.fillMaxWidth(),
        gradientColors = listOf(
            backgroundColor,
            backgroundColor.copy(alpha = 0.1f)
        )
    ) {
        androidx.compose.material3.Surface(
            onClick = onClick,
            color = androidx.compose.ui.graphics.Color.Transparent
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Emoji and Symbol
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Text(
                        text = sign.emoji,
                        fontSize = 48.sp
                    )
                    Text(
                        text = sign.symbol,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                // Info
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = sign.displayName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = sign.dateRange,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = sign.element.emoji,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = sign.element.displayName,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ZodiacDetailView(
    sign: ZodiacSign,
    onBack: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Back Button and Header
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                GlassIconButton(onClick = onBack) {
                    Text("←", fontSize = 24.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "${sign.emoji} ${sign.displayName}",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = sign.dateRange,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
        
        // Symbol and Element
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = sign.symbol,
                            fontSize = 64.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Symbol",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = sign.element.emoji,
                            fontSize = 48.sp
                        )
                        Text(
                            text = sign.element.displayName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Element",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
        
        // Traits
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(
                        text = "✨ Key Traits",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Text(
                        text = getTraitsForSign(sign),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        lineHeight = 20.sp
                    )
                }
            }
        }
        
        // Strengths
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(
                        text = "💪 Strengths",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Text(
                        text = getStrengthsForSign(sign),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        lineHeight = 20.sp
                    )
                }
            }
        }
        
        // Weaknesses
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(
                        text = "⚠️ Challenges",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Text(
                        text = getWeaknessesForSign(sign),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        lineHeight = 20.sp
                    )
                }
            }
        }
        
        // Ruling Planet
        item {
            GradientGlassCard(
                modifier = Modifier.fillMaxWidth(),
                gradientColors = listOf(
                    CosmicBlue.copy(alpha = 0.3f),
                    MysticPurple.copy(alpha = 0.2f)
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = "🪐 Ruling Planet",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = getRulingPlanetForSign(sign),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
        
        // Compatibility
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(
                        text = "💞 Best Compatibility",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Text(
                        text = getCompatibilityForSign(sign),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        lineHeight = 20.sp
                    )
                }
            }
        }
        
        // Famous People
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(
                        text = "🌟 Famous People",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Text(
                        text = getFamousPeopleForSign(sign),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        lineHeight = 20.sp
                    )
                }
            }
        }
        
        // Ask AI Button
        item {
            NeumorphicButton(
                onClick = { /* Navigate to Chat with pre-filled question about this sign */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "💬",
                        fontSize = 20.sp
                    )
                    Text("Ask AI About ${sign.displayName}")
                }
            }
        }
        
        // Bottom padding
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

// Helper functions for sign details
private fun getTraitsForSign(sign: ZodiacSign): String {
    return when (sign) {
        ZodiacSign.ARIES -> "Bold, ambitious, confident, enthusiastic, and courageous. Natural leaders who love challenges."
        ZodiacSign.TAURUS -> "Reliable, patient, practical, devoted, responsible, and stable. Appreciates beauty and comfort."
        ZodiacSign.GEMINI -> "Gentle, affectionate, curious, adaptable, and quick learners. Excellent communicators."
        ZodiacSign.CANCER -> "Tenacious, loyal, emotional, sympathetic, and persuasive. Deeply intuitive and sentimental."
        ZodiacSign.LEO -> "Creative, passionate, generous, warm-hearted, cheerful, and humorous. Natural performers."
        ZodiacSign.VIRGO -> "Loyal, analytical, kind, hardworking, and practical. Detail-oriented perfectionists."
        ZodiacSign.LIBRA -> "Cooperative, diplomatic, gracious, fair-minded, and social. Seeks balance and harmony."
        ZodiacSign.SCORPIO -> "Resourceful, brave, passionate, stubborn, and a true friend. Intensely emotional."
        ZodiacSign.SAGITTARIUS -> "Generous, idealistic, great sense of humor, optimistic, and loves freedom."
        ZodiacSign.CAPRICORN -> "Responsible, disciplined, self-control, good managers, and ambitious achievers."
        ZodiacSign.AQUARIUS -> "Progressive, original, independent, humanitarian, and intellectual visionaries."
        ZodiacSign.PISCES -> "Compassionate, artistic, intuitive, gentle, wise, and musical. Deeply empathetic."
    }
}

private fun getStrengthsForSign(sign: ZodiacSign): String {
    return when (sign) {
        ZodiacSign.ARIES -> "Courageous, determined, confident, enthusiastic, optimistic, honest, passionate"
        ZodiacSign.TAURUS -> "Reliable, patient, practical, devoted, responsible, stable"
        ZodiacSign.GEMINI -> "Gentle, affectionate, curious, adaptable, ability to learn quickly"
        ZodiacSign.CANCER -> "Tenacious, highly imaginative, loyal, emotional, sympathetic, persuasive"
        ZodiacSign.LEO -> "Creative, passionate, generous, warm-hearted, cheerful, humorous"
        ZodiacSign.VIRGO -> "Loyal, analytical, kind, hardworking, practical"
        ZodiacSign.LIBRA -> "Cooperative, diplomatic, gracious, fair-minded, social"
        ZodiacSign.SCORPIO -> "Resourceful, brave, passionate, stubborn, a true friend"
        ZodiacSign.SAGITTARIUS -> "Generous, idealistic, great sense of humor"
        ZodiacSign.CAPRICORN -> "Responsible, disciplined, self-control, good managers"
        ZodiacSign.AQUARIUS -> "Progressive, original, independent, humanitarian"
        ZodiacSign.PISCES -> "Compassionate, artistic, intuitive, gentle, wise, musical"
    }
}

private fun getWeaknessesForSign(sign: ZodiacSign): String {
    return when (sign) {
        ZodiacSign.ARIES -> "Impatient, moody, short-tempered, impulsive, aggressive"
        ZodiacSign.TAURUS -> "Stubborn, possessive, uncompromising"
        ZodiacSign.GEMINI -> "Nervous, inconsistent, indecisive"
        ZodiacSign.CANCER -> "Moody, pessimistic, suspicious, manipulative, insecure"
        ZodiacSign.LEO -> "Arrogant, stubborn, self-centered, lazy, inflexible"
        ZodiacSign.VIRGO -> "Shyness, worry, overly critical of self and others"
        ZodiacSign.LIBRA -> "Indecisive, avoids confrontations, will carry a grudge"
        ZodiacSign.SCORPIO -> "Distrusting, jealous, secretive, violent"
        ZodiacSign.SAGITTARIUS -> "Promises more than can deliver, very impatient, will say anything"
        ZodiacSign.CAPRICORN -> "Know-it-all, unforgiving, condescending, expecting the worst"
        ZodiacSign.AQUARIUS -> "Runs from emotional expression, temperamental, uncompromising"
        ZodiacSign.PISCES -> "Fearful, overly trusting, sad, desire to escape reality"
    }
}

private fun getRulingPlanetForSign(sign: ZodiacSign): String {
    return when (sign) {
        ZodiacSign.ARIES -> "Mars - Planet of action and desire"
        ZodiacSign.TAURUS -> "Venus - Planet of love and beauty"
        ZodiacSign.GEMINI -> "Mercury - Planet of communication"
        ZodiacSign.CANCER -> "Moon - Planet of emotions and nurturing"
        ZodiacSign.LEO -> "Sun - Planet of self and vitality"
        ZodiacSign.VIRGO -> "Mercury - Planet of analysis and service"
        ZodiacSign.LIBRA -> "Venus - Planet of harmony and relationships"
        ZodiacSign.SCORPIO -> "Pluto & Mars - Planets of transformation"
        ZodiacSign.SAGITTARIUS -> "Jupiter - Planet of expansion and wisdom"
        ZodiacSign.CAPRICORN -> "Saturn - Planet of discipline and responsibility"
        ZodiacSign.AQUARIUS -> "Uranus & Saturn - Planets of innovation"
        ZodiacSign.PISCES -> "Neptune & Jupiter - Planets of dreams and intuition"
    }
}

private fun getCompatibilityForSign(sign: ZodiacSign): String {
    return when (sign) {
        ZodiacSign.ARIES -> "Most compatible with Leo, Sagittarius, Gemini, and Aquarius. These signs match Aries' energy and adventurous spirit."
        ZodiacSign.TAURUS -> "Most compatible with Virgo, Capricorn, Cancer, and Pisces. These signs appreciate Taurus' stability and loyalty."
        ZodiacSign.GEMINI -> "Most compatible with Libra, Aquarius, Aries, and Leo. These signs enjoy Gemini's wit and intellectual curiosity."
        ZodiacSign.CANCER -> "Most compatible with Scorpio, Pisces, Taurus, and Virgo. These signs understand Cancer's emotional depth."
        ZodiacSign.LEO -> "Most compatible with Aries, Sagittarius, Gemini, and Libra. These signs appreciate Leo's warmth and generosity."
        ZodiacSign.VIRGO -> "Most compatible with Taurus, Capricorn, Cancer, and Scorpio. These signs value Virgo's practical nature."
        ZodiacSign.LIBRA -> "Most compatible with Gemini, Aquarius, Leo, and Sagittarius. These signs appreciate Libra's balanced approach."
        ZodiacSign.SCORPIO -> "Most compatible with Cancer, Pisces, Virgo, and Capricorn. These signs handle Scorpio's intensity well."
        ZodiacSign.SAGITTARIUS -> "Most compatible with Aries, Leo, Libra, and Aquarius. These signs match Sagittarius' love for adventure."
        ZodiacSign.CAPRICORN -> "Most compatible with Taurus, Virgo, Scorpio, and Pisces. These signs respect Capricorn's ambition."
        ZodiacSign.AQUARIUS -> "Most compatible with Gemini, Libra, Sagittarius, and Aries. These signs appreciate Aquarius' uniqueness."
        ZodiacSign.PISCES -> "Most compatible with Cancer, Scorpio, Taurus, and Capricorn. These signs understand Pisces' sensitivity."
    }
}

private fun getFamousPeopleForSign(sign: ZodiacSign): String {
    return when (sign) {
        ZodiacSign.ARIES -> "Lady Gaga, Robert Downey Jr., Emma Watson, Leonardo da Vinci, Vincent van Gogh"
        ZodiacSign.TAURUS -> "Adele, Dwayne 'The Rock' Johnson, David Beckham, Queen Elizabeth II, William Shakespeare"
        ZodiacSign.GEMINI -> "Marilyn Monroe, Johnny Depp, Angelina Jolie, Kanye West, Paul McCartney"
        ZodiacSign.CANCER -> "Selena Gomez, Tom Hanks, Princess Diana, Elon Musk, Frida Kahlo"
        ZodiacSign.LEO -> "Barack Obama, Jennifer Lopez, Madonna, Daniel Radcliffe, Napoleon Bonaparte"
        ZodiacSign.VIRGO -> "Beyoncé, Keanu Reeves, Zendaya, Michael Jackson, Mother Teresa"
        ZodiacSign.LIBRA -> "Kim Kardashian, Will Smith, Serena Williams, Gandhi, John Lennon"
        ZodiacSign.SCORPIO -> "Leonardo DiCaprio, Katy Perry, Pablo Picasso, Bill Gates, Marie Curie"
        ZodiacSign.SAGITTARIUS -> "Taylor Swift, Brad Pitt, Jay-Z, Walt Disney, Winston Churchill"
        ZodiacSign.CAPRICORN -> "Michelle Obama, Denzel Washington, Elvis Presley, Martin Luther King Jr., Isaac Newton"
        ZodiacSign.AQUARIUS -> "Oprah Winfrey, Harry Styles, Ellen DeGeneres, Abraham Lincoln, Galileo Galilei"
        ZodiacSign.PISCES -> "Rihanna, Albert Einstein, Steve Jobs, George Washington, Michelangelo"
    }
}
