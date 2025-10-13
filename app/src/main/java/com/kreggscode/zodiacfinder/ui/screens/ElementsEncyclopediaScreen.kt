package com.kreggscode.zodiacfinder.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kreggscode.zodiacfinder.ui.components.*
import com.kreggscode.zodiacfinder.ui.theme.*

data class Element(
    val name: String,
    val emoji: String,
    val color: Color,
    val signs: List<String>,
    val description: String,
    val characteristics: String,
    val strengths: String,
    val challenges: String,
    val compatibility: String,
    val career: String,
    val health: String
)

@Composable
fun ElementsEncyclopediaScreen(
    onNavigateBack: () -> Unit = {}
) {
    val elements = remember { getElementsList() }
    var selectedElement by remember { mutableStateOf<Element?>(null) }
    
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
        if (selectedElement == null) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    TopBarWithBack(
                        title = "⚡ Elements Encyclopedia",
                        subtitle = "The five cosmic forces",
                        onBackClick = onNavigateBack
                    )
                }
                
                items(elements) { element ->
                    ElementListCard(
                        element = element,
                        onClick = { selectedElement = element }
                    )
                }
            }
        } else {
            ElementDetailScreen(
                element = selectedElement!!,
                onBack = { selectedElement = null }
            )
        }
    }
}

@Composable
private fun ElementListCard(
    element: Element,
    onClick: () -> Unit
) {
    GradientGlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        gradientColors = listOf(
            element.color.copy(alpha = 0.4f),
            element.color.copy(alpha = 0.2f)
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = element.emoji,
                fontSize = 56.sp
            )
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = element.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = element.signs.joinToString(" • "),
                    fontSize = 13.sp,
                    color = element.color,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = element.description.take(80) + "...",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
private fun ElementDetailScreen(
    element: Element,
    onBack: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            TopBarWithBack(
                title = "${element.emoji} ${element.name}",
                subtitle = element.signs.joinToString(", "),
                onBackClick = onBack
            )
        }
        
        // Overview
        item {
            GradientGlassCard(
                modifier = Modifier.fillMaxWidth(),
                gradientColors = listOf(
                    element.color.copy(alpha = 0.5f),
                    element.color.copy(alpha = 0.3f)
                )
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = element.emoji,
                        fontSize = 100.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = element.name,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "The ${element.name} Element",
                        fontSize = 16.sp,
                        color = element.color,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
        
        // Description
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "✨ About",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                    Text(
                        text = element.description,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                        lineHeight = 24.sp
                    )
                }
            }
        }
        
        // Characteristics
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
                        text = "🌟 Key Characteristics",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(0.1f))
                    Text(
                        text = element.characteristics,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                        lineHeight = 24.sp
                    )
                }
            }
        }
        
        // Strengths & Challenges
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
                            text = element.strengths,
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
                            text = element.challenges,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }
        
        // Compatibility
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "💕 Compatibility",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(0.1f))
                    Text(
                        text = element.compatibility,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                        lineHeight = 24.sp
                    )
                }
            }
        }
        
        // Career
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "💼 Career & Work Style",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(0.1f))
                    Text(
                        text = element.career,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                        lineHeight = 24.sp
                    )
                }
            }
        }
        
        // Health
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "🏥 Health & Wellness",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(0.1f))
                    Text(
                        text = element.health,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                        lineHeight = 24.sp
                    )
                }
            }
        }
    }
}

private fun getElementsList(): List<Element> {
    return listOf(
        Element(
            name = "Fire",
            emoji = "🔥",
            color = FireElement,
            signs = listOf("Aries", "Leo", "Sagittarius"),
            description = "Fire is the element of passion, energy, and transformation. It represents the spark of life, creativity, and the drive to take action. Fire signs are known for their enthusiasm, courage, and dynamic nature. They are the initiators of the zodiac, always ready to start new adventures and inspire others with their infectious energy.",
            characteristics = "• Passionate and enthusiastic\n• Courageous and confident\n• Creative and inspiring\n• Spontaneous and adventurous\n• Natural leaders\n• Optimistic and positive\n• Direct and honest\n• Energetic and active",
            strengths = "• Initiative\n• Courage\n• Passion\n• Leadership\n• Creativity\n• Enthusiasm\n• Confidence",
            challenges = "• Impatience\n• Impulsiveness\n• Aggression\n• Burnout\n• Insensitivity\n• Recklessness",
            compatibility = "Fire signs are most compatible with Air signs (Gemini, Libra, Aquarius) as air fuels fire. They share intellectual curiosity and love for freedom. Fire signs can also work well with other Fire signs, creating passionate and dynamic relationships, though they may compete for dominance. Earth signs can ground Fire, while Water signs may dampen their enthusiasm.",
            career = "Fire signs excel in careers that allow them to lead, create, and inspire. They thrive in entrepreneurship, sales, entertainment, sports, military, emergency services, and any role requiring courage and initiative. They need dynamic environments with challenges and opportunities for advancement. Routine and micromanagement stifle their spirit.",
            health = "Fire signs need to watch for burnout, stress-related issues, and inflammation. They benefit from high-energy activities like sports, martial arts, and dance. However, they must balance activity with rest. Common health areas: heart, blood pressure, fever, headaches. They should practice stress management and avoid excessive stimulants."
        ),
        Element(
            name = "Earth",
            emoji = "🌍",
            color = EarthElement,
            signs = listOf("Taurus", "Virgo", "Capricorn"),
            description = "Earth is the element of stability, practicality, and material manifestation. It represents the physical world, sensory experiences, and the ability to build lasting foundations. Earth signs are grounded, reliable, and focused on tangible results. They are the builders of the zodiac, creating security and structure in their lives and for others.",
            characteristics = "• Practical and realistic\n• Reliable and dependable\n• Patient and persistent\n• Grounded and stable\n• Sensual and pleasure-seeking\n• Hardworking and disciplined\n• Traditional and conservative\n• Financially savvy",
            strengths = "• Reliability\n• Practicality\n• Patience\n• Loyalty\n• Discipline\n• Resourcefulness\n• Stability",
            challenges = "• Stubbornness\n• Materialism\n• Resistance to change\n• Rigidity\n• Possessiveness\n• Pessimism",
            compatibility = "Earth signs are most compatible with Water signs (Cancer, Scorpio, Pisces) as water nourishes earth. They provide emotional depth to Earth's stability. Earth signs also work well with other Earth signs, creating secure and practical partnerships. Fire signs can energize Earth, while Air signs may seem too flighty for Earth's grounded nature.",
            career = "Earth signs excel in careers requiring precision, reliability, and long-term planning. They thrive in finance, real estate, agriculture, construction, healthcare, accounting, and management. They need stable environments with clear structures and tangible rewards. They build wealth through patience and strategic planning.",
            health = "Earth signs need to watch for issues related to rigidity, weight, and digestive problems. They benefit from regular exercise, healthy eating, and connection to nature. Common health areas: bones, joints, teeth, skin, digestive system. They should avoid becoming sedentary and maintain flexibility through yoga or stretching."
        ),
        Element(
            name = "Air",
            emoji = "💨",
            color = AirElement,
            signs = listOf("Gemini", "Libra", "Aquarius"),
            description = "Air is the element of intellect, communication, and social connection. It represents ideas, thoughts, and the exchange of information. Air signs are mental, analytical, and focused on understanding the world through logic and reason. They are the communicators of the zodiac, connecting people and ideas with ease and grace.",
            characteristics = "• Intellectual and analytical\n• Communicative and social\n• Objective and logical\n• Adaptable and flexible\n• Curious and inquisitive\n• Independent and freedom-loving\n• Innovative and progressive\n• Detached and rational",
            strengths = "• Communication\n• Intelligence\n• Adaptability\n• Objectivity\n• Social skills\n• Innovation\n• Open-mindedness",
            challenges = "• Detachment\n• Inconsistency\n• Overthinking\n• Superficiality\n• Indecisiveness\n• Aloofness",
            compatibility = "Air signs are most compatible with Fire signs (Aries, Leo, Sagittarius) as they share enthusiasm and love for freedom. Air fans the flames of Fire's passion. Air signs also work well with other Air signs, creating intellectually stimulating relationships. Earth signs may seem too slow, while Water signs may be too emotional for Air's rational nature.",
            career = "Air signs excel in careers involving communication, technology, and intellectual pursuits. They thrive in writing, teaching, journalism, technology, social media, law, consulting, and research. They need mentally stimulating environments with variety and social interaction. Routine and isolation drain their energy.",
            health = "Air signs need to watch for nervous system issues, anxiety, and respiratory problems. They benefit from meditation, breathwork, and activities that calm the mind. Common health areas: lungs, nervous system, circulation, arms, hands. They should practice grounding techniques and avoid excessive mental stimulation before sleep."
        ),
        Element(
            name = "Water",
            emoji = "💧",
            color = WaterElement,
            signs = listOf("Cancer", "Scorpio", "Pisces"),
            description = "Water is the element of emotion, intuition, and spiritual depth. It represents feelings, the subconscious mind, and the ability to flow and adapt. Water signs are sensitive, empathetic, and deeply connected to their inner world. They are the healers and mystics of the zodiac, understanding life through emotional and spiritual lenses.",
            characteristics = "• Emotional and sensitive\n• Intuitive and psychic\n• Empathetic and compassionate\n• Creative and imaginative\n• Nurturing and caring\n• Mysterious and private\n• Spiritual and mystical\n• Adaptable like water",
            strengths = "• Empathy\n• Intuition\n• Creativity\n• Compassion\n• Emotional depth\n• Healing abilities\n• Adaptability",
            challenges = "• Over-sensitivity\n• Moodiness\n• Escapism\n• Boundary issues\n• Manipulation\n• Victim mentality",
            compatibility = "Water signs are most compatible with Earth signs (Taurus, Virgo, Capricorn) as earth contains and shapes water. They provide structure to Water's emotions. Water signs also work well with other Water signs, creating deep emotional bonds, though they may become too intense. Fire signs may evaporate Water, while Air signs may seem too detached.",
            career = "Water signs excel in careers involving healing, creativity, and emotional connection. They thrive in counseling, healthcare, arts, music, spirituality, social work, and hospitality. They need emotionally meaningful work that helps others. They are drawn to careers where they can use their intuition and empathy.",
            health = "Water signs need to watch for emotional overwhelm, depression, and fluid retention. They benefit from creative expression, time near water, and emotional release practices. Common health areas: lymphatic system, reproductive organs, feet, emotional wellbeing. They should maintain healthy boundaries and practice self-care regularly."
        ),
        Element(
            name = "Ether (Spirit)",
            emoji = "⚡",
            color = MysticPurple,
            signs = listOf("The Fifth Element", "All Signs"),
            description = "Ether, also known as Spirit or Akasha, is the fifth element that transcends and unifies the other four. It represents consciousness, divine energy, and the space in which all elements exist. Ether is the mystical force that connects everything in the universe, the source of all creation and the ultimate reality beyond physical manifestation.",
            characteristics = "• Transcendent and spiritual\n• Unifying and integrating\n• Consciousness and awareness\n• Divine connection\n• Space and potential\n• Timeless and eternal\n• Pure energy and vibration\n• Source of all elements",
            strengths = "• Spiritual awareness\n• Unity consciousness\n• Divine connection\n• Transcendence\n• Pure potential\n• Universal love\n• Enlightenment",
            challenges = "• Disconnection from physical reality\n• Difficulty grounding\n• Escapism into spirituality\n• Lack of practical action\n• Spiritual bypassing",
            compatibility = "Ether is compatible with all elements as it contains and transcends them all. It represents the spiritual dimension that unifies Fire's passion, Earth's stability, Air's intellect, and Water's emotion. When balanced with Ether, each element finds its highest expression and purpose in the cosmic dance of existence.",
            career = "Ether-aligned individuals excel in spiritual teaching, energy healing, meditation instruction, mysticism, philosophy, and any work that bridges the physical and spiritual realms. They are the shamans, priests, spiritual guides, and consciousness explorers who help others connect with higher dimensions of reality.",
            health = "Ether relates to overall energetic balance and spiritual wellbeing. Practices include meditation, prayer, energy healing, breathwork, and consciousness expansion. It governs the crown chakra and pineal gland. Balance Ether through spiritual practice while staying grounded in the physical body. Integration of spirit and matter is key to health."
        )
    )
}
