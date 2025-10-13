package com.kreggscode.zodiacfinder.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kreggscode.zodiacfinder.data.model.TarotCard
import com.kreggscode.zodiacfinder.data.model.TarotArcana
import com.kreggscode.zodiacfinder.ui.components.*
import com.kreggscode.zodiacfinder.ui.theme.*

// Simple Tarot Card for readings (using old enum-style data)
data class SimpleTarotCard(
    val displayName: String,
    val emoji: String,
    val upright: String,
    val reversed: String,
    val description: String
)

// Helper function to get simple tarot cards for readings
fun getSimpleTarotDeck(): List<SimpleTarotCard> = listOf(
    SimpleTarotCard("The Fool", "🃏", "New beginnings, innocence, spontaneity", "Recklessness, risk-taking", "The Fool represents new beginnings and faith in the future."),
    SimpleTarotCard("The Magician", "🎩", "Manifestation, resourcefulness, power", "Manipulation, poor planning", "The Magician brings ideas into reality."),
    SimpleTarotCard("The High Priestess", "👸", "Intuition, sacred knowledge", "Secrets, disconnected from intuition", "The High Priestess signifies spiritual enlightenment."),
    SimpleTarotCard("The Empress", "👑", "Femininity, beauty, nature, abundance", "Creative block, dependence", "The Empress represents Mother Earth and fertility."),
    SimpleTarotCard("The Emperor", "🤴", "Authority, establishment, structure", "Domination, excessive control", "The Emperor represents masculine power."),
    SimpleTarotCard("The Lovers", "💕", "Love, harmony, relationships", "Self-love, disharmony", "The Lovers represent perfect union and harmony."),
    SimpleTarotCard("The Chariot", "🎯", "Control, willpower, success", "Lack of control, aggression", "The Chariot represents overcoming obstacles."),
    SimpleTarotCard("Strength", "💪", "Courage, bravery, inner strength", "Self-doubt, weakness", "Strength represents inner courage and resilience."),
    SimpleTarotCard("The Hermit", "🕯️", "Soul searching, introspection", "Isolation, loneliness", "The Hermit represents searching for truth."),
    SimpleTarotCard("Wheel of Fortune", "🎡", "Change, cycles, fate", "Bad luck, lack of control", "The Wheel represents life's cycles."),
    SimpleTarotCard("Justice", "⚖️", "Justice, fairness, truth", "Unfairness, dishonesty", "Justice represents karma and consequences."),
    SimpleTarotCard("The Hanged Man", "🙃", "Pause, surrender, new perspectives", "Delays, resistance", "The Hanged Man represents letting go."),
    SimpleTarotCard("Death", "💀", "Endings, transformation", "Resistance to change", "Death represents transformation and new beginnings."),
    SimpleTarotCard("Temperance", "🧘", "Balance, moderation, patience", "Imbalance, excess", "Temperance represents finding balance."),
    SimpleTarotCard("The Devil", "😈", "Shadow self, attachment", "Releasing limiting beliefs", "The Devil represents our shadow side."),
    SimpleTarotCard("The Tower", "🗼", "Sudden change, upheaval", "Personal transformation", "The Tower represents disruptive change."),
    SimpleTarotCard("The Star", "⭐", "Hope, faith, renewal", "Lack of faith, despair", "The Star brings renewed hope."),
    SimpleTarotCard("The Moon", "🌙", "Illusion, intuition", "Release of fear", "The Moon represents the subconscious."),
    SimpleTarotCard("The Sun", "☀️", "Positivity, success, vitality", "Overly optimistic", "The Sun represents radiance and abundance."),
    SimpleTarotCard("Judgement", "📯", "Reflection, awakening", "Self-doubt, inner critic", "Judgement represents reckoning."),
    SimpleTarotCard("The World", "🌍", "Completion, accomplishment", "Seeking closure", "The World represents fulfillment.")
)

@Composable
fun TarotScreen(
    onNavigateBack: () -> Unit = {}
) {
    var drawnCards by remember { mutableStateOf<List<SimpleTarotCard>>(emptyList()) }
    var spreadType by remember { mutableStateOf(1) }
    var isRevealed by remember { mutableStateOf(false) }
    val tarotDeck = remember { getSimpleTarotDeck() }
    
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
                    title = "🃏 Tarot Reading",
                    subtitle = "Discover your cosmic guidance",
                    onBackClick = onNavigateBack
                )
            }
            
            // Spread Selection
            if (drawnCards.isEmpty()) {
                item {
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            Text(
                                text = "✨ Choose Your Spread",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            
                            SpreadOption(
                                title = "Single Card Reading",
                                description = "Quick insight for your day",
                                selected = spreadType == 1,
                                onClick = { spreadType = 1 }
                            )
                            
                            SpreadOption(
                                title = "Three Card Spread",
                                description = "Past, Present, Future",
                                selected = spreadType == 3,
                                onClick = { spreadType = 3 }
                            )
                            
                            SpreadOption(
                                title = "Celtic Cross",
                                description = "Comprehensive life reading",
                                selected = spreadType == 5,
                                onClick = { spreadType = 5 }
                            )
                        }
                    }
                }
                
                // Draw Button
                item {
                    NeumorphicButton(
                        onClick = {
                            drawnCards = tarotDeck.shuffled().take(spreadType)
                            isRevealed = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🔮",
                                fontSize = 24.sp
                            )
                            Text("Draw Cards", fontSize = 16.sp)
                        }
                    }
                }
                
                // Info Card
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
                                text = "🌙 About Tarot",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MysticPurple
                            )
                            Text(
                                text = "Tarot cards offer guidance and insight into your life's journey. Each card carries deep symbolism and meaning. Focus on your question while drawing cards for the most accurate reading.",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }
            
            // Drawn Cards
            if (drawnCards.isNotEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Your Reading",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        
                        if (!isRevealed) {
                            NeumorphicButton(
                                onClick = { isRevealed = true }
                            ) {
                                Text("Reveal", fontSize = 14.sp)
                            }
                        }
                    }
                }
                
                items(drawnCards.withIndex().toList()) { (index, card) ->
                    AnimatedVisibility(
                        visible = isRevealed,
                        enter = fadeIn() + expandVertically()
                    ) {
                        TarotCardDisplay(
                            card = card,
                            position = when (index) {
                                0 -> if (drawnCards.size == 3) "Past" else "Your Card"
                                1 -> "Present"
                                2 -> "Future"
                                else -> "Card ${index + 1}"
                            }
                        )
                    }
                }
                
                // Reset Button
                if (isRevealed) {
                    item {
                        NeumorphicButton(
                            onClick = {
                                drawnCards = emptyList()
                                isRevealed = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("🔄 New Reading")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SpreadOption(
    title: String,
    description: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    GradientGlassCard(
        modifier = Modifier.fillMaxWidth(),
        gradientColors = if (selected) {
            listOf(
                MysticPurple.copy(alpha = 0.3f),
                CosmicBlue.copy(alpha = 0.2f)
            )
        } else {
            listOf(
                MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)
            )
        }
    ) {
        Surface(
            onClick = onClick,
            color = androidx.compose.ui.graphics.Color.Transparent
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (selected) MysticPurple else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = description,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                
                if (selected) {
                    Text(
                        text = "✓",
                        fontSize = 24.sp,
                        color = MysticPurple
                    )
                }
            }
        }
    }
}

@Composable
private fun TarotCardDisplay(
    card: SimpleTarotCard,
    position: String
) {
    GradientGlassCard(
        modifier = Modifier.fillMaxWidth(),
        gradientColors = listOf(
            MysticPurple.copy(alpha = 0.3f),
            CosmicBlue.copy(alpha = 0.2f)
        )
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            // Position & Card Name
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = position,
                        fontSize = 14.sp,
                        color = MysticPurple,
                        fontWeight = FontWeight.SemiBold
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = card.emoji,
                            fontSize = 32.sp
                        )
                        Text(
                            text = card.displayName,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
            
            HorizontalDivider(
                color = MysticPurple.copy(alpha = 0.3f),
                thickness = 2.dp
            )
            
            // Description
            Text(
                text = card.description,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                lineHeight = 20.sp
            )
            
            // Upright Meaning
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "✨ Upright Meaning",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = LuckyGold
                )
                Text(
                    text = card.upright,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                    lineHeight = 20.sp
                )
            }
            
            // Reversed Meaning
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "⚠️ Reversed Meaning",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = SecondaryLight
                )
                Text(
                    text = card.reversed,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                    lineHeight = 20.sp
                )
            }
        }
    }
}
