package com.kreggscode.zodiacfinder.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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

data class EncyclopediaItem(
    val title: String,
    val emoji: String,
    val description: String,
    val route: String,
    val gradientColors: List<androidx.compose.ui.graphics.Color>
)

@Composable
fun EncyclopediaScreenNew(
    onNavigateBack: () -> Unit = {},
    onNavigateToTarot: () -> Unit = {},
    onNavigateToCrystals: () -> Unit = {},
    onNavigateToElements: () -> Unit = {},
    onNavigateToGemstones: () -> Unit = {}
) {
    var selectedSign by remember { mutableStateOf<ZodiacSign?>(null) }
    
    val encyclopediaItems = listOf(
        EncyclopediaItem(
            title = "Tarot Cards",
            emoji = "🎴",
            description = "All 78 Tarot cards with meanings",
            route = "tarot",
            gradientColors = listOf(MysticPurple.copy(0.3f), CosmicBlue.copy(0.2f))
        ),
        EncyclopediaItem(
            title = "Crystals & Gems",
            emoji = "💎",
            description = "Healing crystals and their properties",
            route = "crystals",
            gradientColors = listOf(LuckyGold.copy(0.3f), FireElement.copy(0.2f))
        ),
        EncyclopediaItem(
            title = "Elements",
            emoji = "🔥",
            description = "Fire, Water, Earth, and Air",
            route = "elements",
            gradientColors = listOf(FireElement.copy(0.3f), WaterElement.copy(0.2f))
        ),
        EncyclopediaItem(
            title = "Gemstones",
            emoji = "🌟",
            description = "Lucky gemstones for each sign",
            route = "gemstones",
            gradientColors = listOf(EarthElement.copy(0.3f), AirElement.copy(0.2f))
        )
    )
    
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
                    title = "📚 Encyclopedia",
                    subtitle = "Explore mystical knowledge",
                    onBackClick = onNavigateBack
                )
            }
            
            // Hero Section
            item {
                GradientGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    gradientColors = listOf(
                        MysticPurple.copy(alpha = 0.3f),
                        CosmicBlue.copy(alpha = 0.2f)
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("✨", fontSize = 64.sp)
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = "Mystical Knowledge Base",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Explore Tarot, Crystals, Elements, and Zodiac wisdom",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(0.7f)
                        )
                    }
                }
            }
            
            // Encyclopedia Categories
            item {
                Text(
                    text = "📖 Browse Categories",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            items(encyclopediaItems.size) { index ->
                val item = encyclopediaItems[index]
                GradientGlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            when (item.route) {
                                "tarot" -> onNavigateToTarot()
                                "crystals" -> onNavigateToCrystals()
                                "elements" -> onNavigateToElements()
                                "gemstones" -> onNavigateToGemstones()
                            }
                        },
                    gradientColors = item.gradientColors
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(item.emoji, fontSize = 48.sp)
                            Spacer(Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = item.title,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = item.description,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(0.7f)
                                )
                            }
                        }
                        Text("→", fontSize = 24.sp, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
            
            // Zodiac Signs Section
            item {
                Text(
                    text = "♈ Zodiac Signs",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            // Grid of Zodiac Cards
            item {
                val gridHeight = ((ZodiacSign.values().size + 1) / 2) * 200
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.height(gridHeight.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    userScrollEnabled = false
                ) {
                    items(ZodiacSign.values().toList()) { sign ->
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
            
            // Personality Traits
            item {
                GradientGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    gradientColors = listOf(
                        MysticPurple.copy(alpha = 0.2f),
                        CosmicBlue.copy(alpha = 0.1f)
                    )
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "🌟 Key Personality Traits",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(0.1f))
                        Text(
                            text = getPersonalityTraits(sign),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                            lineHeight = 22.sp
                        )
                    }
                }
            }
            
            // Strengths & Weaknesses
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GlassCard(modifier = Modifier.weight(1f)) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "💪 Strengths",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = getStrengths(sign),
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                                lineHeight = 20.sp
                            )
                        }
                    }
                    
                    GlassCard(modifier = Modifier.weight(1f)) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "⚠️ Challenges",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = getChallenges(sign),
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }
            
            // Love & Relationships
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "💕 Love & Relationships",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(0.1f))
                        Text(
                            text = getLoveCompatibility(sign),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                            lineHeight = 22.sp
                        )
                    }
                }
            }
            
            // Career & Money
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "💼 Career & Money",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(0.1f))
                        Text(
                            text = getCareerInfo(sign),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                            lineHeight = 22.sp
                        )
                    }
                }
            }
            
            // Lucky Elements
            item {
                GradientGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    gradientColors = listOf(
                        LuckyGold.copy(alpha = 0.3f),
                        LuckyGold.copy(alpha = 0.1f)
                    )
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "🍀 Lucky Elements",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = LuckyGold
                        )
                        HorizontalDivider(color = LuckyGold.copy(0.3f))
                        Text(
                            text = getLuckyElements(sign),
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
        ZodiacSign.ARIES -> "Bold, ambitious, and confident. Aries are natural leaders who love challenges and new beginnings. As the first sign of the zodiac, they embody the raw energy of spring and new life. Ruled by Mars, the planet of action and desire, Aries individuals are pioneers who fearlessly charge into new territories."
        ZodiacSign.TAURUS -> "Reliable, patient, and devoted. Taurus values stability, comfort, and the finer things in life. Ruled by Venus, the planet of love and beauty, Taureans have an innate appreciation for sensory pleasures and material security. They are the builders of the zodiac, creating lasting foundations."
        ZodiacSign.GEMINI -> "Curious, adaptable, and communicative. Gemini thrives on variety and intellectual stimulation. Ruled by Mercury, the planet of communication, Geminis are the social butterflies of the zodiac. Their dual nature allows them to see multiple perspectives and adapt to any situation."
        ZodiacSign.CANCER -> "Intuitive, emotional, and nurturing. Cancer is deeply connected to home, family, and feelings. Ruled by the Moon, Cancers are highly sensitive to emotional undercurrents. They are the nurturers of the zodiac, creating safe spaces for themselves and loved ones."
        ZodiacSign.LEO -> "Confident, generous, and charismatic. Leo loves to shine and inspire others with their warmth. Ruled by the Sun, Leos naturally radiate confidence and creativity. They are the performers of the zodiac, bringing joy and drama to everything they do."
        ZodiacSign.VIRGO -> "Analytical, practical, and detail-oriented. Virgo seeks perfection and loves to be of service. Ruled by Mercury, Virgos possess sharp analytical minds and excellent organizational skills. They are the healers and helpers of the zodiac, always seeking to improve and refine."
        ZodiacSign.LIBRA -> "Diplomatic, charming, and fair-minded. Libra values harmony, beauty, and balanced relationships. Ruled by Venus, Libras have refined aesthetic sensibilities and natural diplomatic skills. They are the peacemakers of the zodiac, always seeking balance and justice."
        ZodiacSign.SCORPIO -> "Passionate, resourceful, and intense. Scorpio seeks deep transformation and authentic connections. Ruled by Pluto and Mars, Scorpios possess incredible emotional depth and transformative power. They are the alchemists of the zodiac, capable of profound regeneration."
        ZodiacSign.SAGITTARIUS -> "Optimistic, adventurous, and philosophical. Sagittarius loves freedom, travel, and seeking truth. Ruled by Jupiter, the planet of expansion, Sagittarians are eternal seekers of wisdom and experience. They are the philosophers and adventurers of the zodiac."
        ZodiacSign.CAPRICORN -> "Ambitious, disciplined, and responsible. Capricorn is driven to achieve and build lasting success. Ruled by Saturn, the planet of structure and discipline, Capricorns possess remarkable patience and determination. They are the achievers of the zodiac, building empires brick by brick."
        ZodiacSign.AQUARIUS -> "Independent, innovative, and humanitarian. Aquarius thinks outside the box and values progress. Ruled by Uranus, the planet of revolution, Aquarians are visionaries who see the future before it arrives. They are the innovators of the zodiac, championing progress and equality."
        ZodiacSign.PISCES -> "Compassionate, artistic, and intuitive. Pisces is deeply empathetic and connected to the spiritual realm. Ruled by Neptune, the planet of dreams and spirituality, Pisceans possess extraordinary imagination and empathy. They are the mystics of the zodiac, bridging the material and spiritual worlds."
    }
}

private fun getPersonalityTraits(sign: ZodiacSign): String {
    return when (sign) {
        ZodiacSign.ARIES -> "• Courageous and determined\n• Direct and honest communicator\n• Competitive and energetic\n• Independent and self-reliant\n• Enthusiastic and optimistic\n• Quick to take action\n• Natural leadership abilities"
        ZodiacSign.TAURUS -> "• Patient and reliable\n• Practical and grounded\n• Loyal and devoted\n• Appreciates beauty and comfort\n• Strong-willed and determined\n• Sensual and pleasure-seeking\n• Excellent with finances"
        ZodiacSign.GEMINI -> "• Intellectually curious\n• Adaptable and flexible\n• Excellent communicator\n• Social and outgoing\n• Quick-witted and clever\n• Versatile and multi-talented\n• Youthful and playful spirit"
        ZodiacSign.CANCER -> "• Deeply emotional and intuitive\n• Nurturing and protective\n• Family-oriented\n• Excellent memory\n• Creative and imaginative\n• Empathetic and compassionate\n• Strong connection to the past"
        ZodiacSign.LEO -> "• Confident and charismatic\n• Generous and warm-hearted\n• Creative and dramatic\n• Natural leader\n• Loyal and protective\n• Loves attention and recognition\n• Strong sense of pride"
        ZodiacSign.VIRGO -> "• Detail-oriented and analytical\n• Practical and efficient\n• Service-oriented\n• Perfectionist tendencies\n• Excellent problem-solver\n• Health-conscious\n• Modest and humble"
        ZodiacSign.LIBRA -> "• Diplomatic and fair-minded\n• Charming and sociable\n• Appreciates beauty and art\n• Seeks balance and harmony\n• Excellent mediator\n• Romantic and idealistic\n• Indecisive at times"
        ZodiacSign.SCORPIO -> "• Intense and passionate\n• Mysterious and private\n• Extremely loyal\n• Resourceful and strategic\n• Emotionally deep\n• Transformative nature\n• Strong intuition"
        ZodiacSign.SAGITTARIUS -> "• Optimistic and enthusiastic\n• Adventurous and freedom-loving\n• Philosophical and wise\n• Honest and direct\n• Generous and good-humored\n• Loves learning and travel\n• Independent spirit"
        ZodiacSign.CAPRICORN -> "• Ambitious and goal-oriented\n• Disciplined and responsible\n• Patient and persistent\n• Practical and realistic\n• Traditional values\n• Excellent leadership\n• Dry sense of humor"
        ZodiacSign.AQUARIUS -> "• Independent and original\n• Humanitarian and progressive\n• Intellectual and analytical\n• Unconventional thinking\n• Friendly and egalitarian\n• Visionary and innovative\n• Values freedom highly"
        ZodiacSign.PISCES -> "• Compassionate and empathetic\n• Artistic and creative\n• Intuitive and psychic\n• Adaptable and flexible\n• Romantic and idealistic\n• Spiritual and mystical\n• Selfless and giving"
    }
}

private fun getStrengths(sign: ZodiacSign): String {
    return when (sign) {
        ZodiacSign.ARIES -> "• Leadership\n• Courage\n• Initiative\n• Confidence\n• Energy"
        ZodiacSign.TAURUS -> "• Reliability\n• Patience\n• Loyalty\n• Practicality\n• Determination"
        ZodiacSign.GEMINI -> "• Communication\n• Adaptability\n• Intelligence\n• Versatility\n• Wit"
        ZodiacSign.CANCER -> "• Empathy\n• Intuition\n• Nurturing\n• Loyalty\n• Creativity"
        ZodiacSign.LEO -> "• Confidence\n• Generosity\n• Charisma\n• Creativity\n• Leadership"
        ZodiacSign.VIRGO -> "• Analysis\n• Organization\n• Efficiency\n• Reliability\n• Attention to detail"
        ZodiacSign.LIBRA -> "• Diplomacy\n• Fairness\n• Charm\n• Balance\n• Social grace"
        ZodiacSign.SCORPIO -> "• Passion\n• Loyalty\n• Resourcefulness\n• Determination\n• Intuition"
        ZodiacSign.SAGITTARIUS -> "• Optimism\n• Honesty\n• Adventure\n• Wisdom\n• Generosity"
        ZodiacSign.CAPRICORN -> "• Ambition\n• Discipline\n• Responsibility\n• Patience\n• Wisdom"
        ZodiacSign.AQUARIUS -> "• Innovation\n• Independence\n• Humanitarianism\n• Intelligence\n• Originality"
        ZodiacSign.PISCES -> "• Compassion\n• Creativity\n• Intuition\n• Adaptability\n• Spirituality"
    }
}

private fun getChallenges(sign: ZodiacSign): String {
    return when (sign) {
        ZodiacSign.ARIES -> "• Impatience\n• Impulsiveness\n• Aggression\n• Self-centeredness\n• Lack of follow-through"
        ZodiacSign.TAURUS -> "• Stubbornness\n• Possessiveness\n• Materialism\n• Resistance to change\n• Laziness"
        ZodiacSign.GEMINI -> "• Inconsistency\n• Superficiality\n• Nervousness\n• Indecisiveness\n• Restlessness"
        ZodiacSign.CANCER -> "• Moodiness\n• Over-sensitivity\n• Clinginess\n• Defensiveness\n• Living in the past"
        ZodiacSign.LEO -> "• Arrogance\n• Stubbornness\n• Need for attention\n• Domineering\n• Pride"
        ZodiacSign.VIRGO -> "• Perfectionism\n• Over-critical\n• Worry\n• Fussiness\n• Harsh self-judgment"
        ZodiacSign.LIBRA -> "• Indecisiveness\n• People-pleasing\n• Avoidance of conflict\n• Superficiality\n• Dependency"
        ZodiacSign.SCORPIO -> "• Jealousy\n• Secretiveness\n• Vindictiveness\n• Obsessiveness\n• Control issues"
        ZodiacSign.SAGITTARIUS -> "• Tactlessness\n• Restlessness\n• Overconfidence\n• Commitment issues\n• Carelessness"
        ZodiacSign.CAPRICORN -> "• Pessimism\n• Rigidity\n• Workaholism\n• Coldness\n• Unforgiving nature"
        ZodiacSign.AQUARIUS -> "• Detachment\n• Stubbornness\n• Aloofness\n• Unpredictability\n• Rebelliousness"
        ZodiacSign.PISCES -> "• Escapism\n• Over-idealism\n• Victim mentality\n• Lack of boundaries\n• Indecisiveness"
    }
}

private fun getLoveCompatibility(sign: ZodiacSign): String {
    return when (sign) {
        ZodiacSign.ARIES -> "In love, Aries is passionate, direct, and exciting. They fall in love quickly and pursue their romantic interests with enthusiasm. Best matches: Leo, Sagittarius, Gemini, Aquarius. They need a partner who can match their energy and independence while appreciating their spontaneous nature."
        ZodiacSign.TAURUS -> "Taurus is sensual, loyal, and devoted in relationships. They take time to commit but are incredibly faithful partners. Best matches: Virgo, Capricorn, Cancer, Pisces. They need stability, physical affection, and a partner who appreciates the finer things in life."
        ZodiacSign.GEMINI -> "Gemini seeks intellectual connection and variety in love. They're playful, communicative, and need mental stimulation. Best matches: Libra, Aquarius, Aries, Leo. They need a partner who can keep up with their quick mind and social nature."
        ZodiacSign.CANCER -> "Cancer is nurturing, emotional, and deeply committed in love. They create strong emotional bonds and value family. Best matches: Scorpio, Pisces, Taurus, Virgo. They need emotional security, understanding, and a partner who values home and family."
        ZodiacSign.LEO -> "Leo is romantic, generous, and passionate in relationships. They love grand gestures and being adored. Best matches: Aries, Sagittarius, Gemini, Libra. They need appreciation, loyalty, and a partner who supports their ambitions."
        ZodiacSign.VIRGO -> "Virgo is practical, devoted, and shows love through acts of service. They're selective but deeply caring. Best matches: Taurus, Capricorn, Cancer, Scorpio. They need patience, appreciation for their efforts, and intellectual connection."
        ZodiacSign.LIBRA -> "Libra is romantic, charming, and seeks partnership above all. They're natural relationship experts. Best matches: Gemini, Aquarius, Leo, Sagittarius. They need balance, beauty, intellectual stimulation, and harmonious communication."
        ZodiacSign.SCORPIO -> "Scorpio is intense, passionate, and seeks deep emotional and physical connection. They're fiercely loyal. Best matches: Cancer, Pisces, Virgo, Capricorn. They need trust, honesty, emotional depth, and complete commitment."
        ZodiacSign.SAGITTARIUS -> "Sagittarius is adventurous, honest, and needs freedom in love. They're optimistic and fun-loving partners. Best matches: Aries, Leo, Libra, Aquarius. They need independence, adventure, intellectual growth, and a sense of humor."
        ZodiacSign.CAPRICORN -> "Capricorn is traditional, loyal, and takes relationships seriously. They're committed for the long haul. Best matches: Taurus, Virgo, Scorpio, Pisces. They need respect, ambition, stability, and a partner who values tradition."
        ZodiacSign.AQUARIUS -> "Aquarius values friendship in love and needs intellectual connection. They're unconventional and independent. Best matches: Gemini, Libra, Sagittarius, Aries. They need freedom, mental stimulation, and acceptance of their uniqueness."
        ZodiacSign.PISCES -> "Pisces is romantic, empathetic, and seeks soulmate connections. They're selfless and dreamy in love. Best matches: Cancer, Scorpio, Taurus, Capricorn. They need emotional depth, creativity, understanding, and spiritual connection."
    }
}

private fun getCareerInfo(sign: ZodiacSign): String {
    return when (sign) {
        ZodiacSign.ARIES -> "Best careers: Entrepreneur, athlete, surgeon, military, sales, firefighter. Aries excels in competitive environments and leadership roles. They're natural pioneers who thrive when starting new projects. Money comes through bold initiatives and taking calculated risks."
        ZodiacSign.TAURUS -> "Best careers: Banking, real estate, chef, artist, musician, gardener, financial advisor. Taurus excels in stable careers with tangible results. They're excellent with money management and building wealth slowly but surely through practical investments."
        ZodiacSign.GEMINI -> "Best careers: Writer, journalist, teacher, salesperson, social media manager, translator. Gemini thrives in communication-based roles with variety. They're good at multiple income streams and making money through networking and information exchange."
        ZodiacSign.CANCER -> "Best careers: Nurse, teacher, chef, real estate agent, counselor, childcare provider. Cancer excels in nurturing professions. They're good at building financial security for their family and investing in property and home-based businesses."
        ZodiacSign.LEO -> "Best careers: Actor, CEO, creative director, politician, entertainer, luxury brand manager. Leo shines in leadership and creative roles. They attract wealth through their charisma and aren't afraid to invest in themselves and their image."
        ZodiacSign.VIRGO -> "Best careers: Doctor, analyst, editor, accountant, nutritionist, researcher, quality control. Virgo excels in detail-oriented service roles. They're excellent at budgeting, saving, and finding practical ways to increase income."
        ZodiacSign.LIBRA -> "Best careers: Lawyer, diplomat, designer, counselor, HR manager, art dealer, mediator. Libra thrives in partnership-based and aesthetic fields. They're good at negotiating salaries and making money through collaborations."
        ZodiacSign.SCORPIO -> "Best careers: Detective, psychologist, surgeon, researcher, financial analyst, investigator. Scorpio excels in transformative and investigative roles. They're strategic with money and excel at investments and uncovering hidden opportunities."
        ZodiacSign.SAGITTARIUS -> "Best careers: Professor, travel guide, philosopher, publisher, international business, coach. Sagittarius thrives in educational and travel-related fields. They attract money through teaching, publishing, and international ventures."
        ZodiacSign.CAPRICORN -> "Best careers: Executive, architect, engineer, government official, project manager, banker. Capricorn excels in structured, hierarchical environments. They're natural wealth builders who achieve financial success through discipline and long-term planning."
        ZodiacSign.AQUARIUS -> "Best careers: Scientist, programmer, humanitarian worker, inventor, astrologer, tech entrepreneur. Aquarius thrives in innovative and humanitarian fields. They make money through unique ideas and technology-based ventures."
        ZodiacSign.PISCES -> "Best careers: Artist, musician, therapist, spiritual healer, photographer, marine biologist. Pisces excels in creative and healing professions. They attract money through artistic talents and helping others, though they need to develop practical financial skills."
    }
}

private fun getLuckyElements(sign: ZodiacSign): String {
    return when (sign) {
        ZodiacSign.ARIES -> "Lucky Numbers: 1, 9\nLucky Colors: Red, Scarlet\nLucky Day: Tuesday\nLucky Gemstone: Diamond, Ruby\nLucky Metal: Iron\nLucky Direction: East"
        ZodiacSign.TAURUS -> "Lucky Numbers: 6, 15, 24\nLucky Colors: Green, Pink\nLucky Day: Friday\nLucky Gemstone: Emerald, Sapphire\nLucky Metal: Copper\nLucky Direction: South"
        ZodiacSign.GEMINI -> "Lucky Numbers: 5, 14, 23\nLucky Colors: Yellow, Light Green\nLucky Day: Wednesday\nLucky Gemstone: Agate, Aquamarine\nLucky Metal: Mercury\nLucky Direction: West"
        ZodiacSign.CANCER -> "Lucky Numbers: 2, 7, 11\nLucky Colors: White, Silver\nLucky Day: Monday\nLucky Gemstone: Pearl, Moonstone\nLucky Metal: Silver\nLucky Direction: North"
        ZodiacSign.LEO -> "Lucky Numbers: 1, 4, 10\nLucky Colors: Gold, Orange\nLucky Day: Sunday\nLucky Gemstone: Ruby, Amber\nLucky Metal: Gold\nLucky Direction: East"
        ZodiacSign.VIRGO -> "Lucky Numbers: 5, 14, 23\nLucky Colors: Navy Blue, Grey\nLucky Day: Wednesday\nLucky Gemstone: Sapphire, Peridot\nLucky Metal: Mercury\nLucky Direction: West"
        ZodiacSign.LIBRA -> "Lucky Numbers: 6, 15, 24\nLucky Colors: Pink, Blue\nLucky Day: Friday\nLucky Gemstone: Opal, Lapis Lazuli\nLucky Metal: Copper\nLucky Direction: West"
        ZodiacSign.SCORPIO -> "Lucky Numbers: 8, 11, 18\nLucky Colors: Deep Red, Black\nLucky Day: Tuesday\nLucky Gemstone: Topaz, Garnet\nLucky Metal: Iron, Steel\nLucky Direction: North"
        ZodiacSign.SAGITTARIUS -> "Lucky Numbers: 3, 12, 21\nLucky Colors: Purple, Blue\nLucky Day: Thursday\nLucky Gemstone: Turquoise, Topaz\nLucky Metal: Tin\nLucky Direction: East"
        ZodiacSign.CAPRICORN -> "Lucky Numbers: 8, 17, 26\nLucky Colors: Brown, Black\nLucky Day: Saturday\nLucky Gemstone: Garnet, Onyx\nLucky Metal: Lead, Silver\nLucky Direction: South"
        ZodiacSign.AQUARIUS -> "Lucky Numbers: 4, 13, 22\nLucky Colors: Turquoise, Electric Blue\nLucky Day: Saturday\nLucky Gemstone: Amethyst, Aquamarine\nLucky Metal: Uranium, Aluminum\nLucky Direction: South"
        ZodiacSign.PISCES -> "Lucky Numbers: 7, 16, 25\nLucky Colors: Sea Green, Lavender\nLucky Day: Thursday\nLucky Gemstone: Aquamarine, Amethyst\nLucky Metal: Tin\nLucky Direction: North"
    }
}
