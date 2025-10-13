package com.kreggscode.zodiacfinder.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kreggscode.zodiacfinder.data.model.*
import com.kreggscode.zodiacfinder.ui.components.*
import com.kreggscode.zodiacfinder.ui.theme.*

@Composable
fun TarotEncyclopediaScreen(
    onNavigateBack: () -> Unit = {}
) {
    var selectedCard by remember { mutableStateOf<TarotCard?>(null) }
    var filterType by remember { mutableStateOf<TarotFilter>(TarotFilter.ALL) }
    var showFilterMenu by remember { mutableStateOf(false) }
    
    val allCards = remember { getAllTarotCards() }
    val filteredCards = remember(filterType) {
        when (filterType) {
            TarotFilter.ALL -> allCards
            TarotFilter.MAJOR -> allCards.filter { it.arcana == TarotArcana.MAJOR }
            TarotFilter.WANDS -> allCards.filter { it.suit == TarotSuit.WANDS }
            TarotFilter.CUPS -> allCards.filter { it.suit == TarotSuit.CUPS }
            TarotFilter.SWORDS -> allCards.filter { it.suit == TarotSuit.SWORDS }
            TarotFilter.PENTACLES -> allCards.filter { it.suit == TarotSuit.PENTACLES }
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
        if (selectedCard == null) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            TopBarWithBack(
                                title = "🎴 Tarot Encyclopedia",
                                subtitle = "All 78 cards with meanings",
                                onBackClick = onNavigateBack
                            )
                        }
                        IconButton(onClick = { showFilterMenu = true }) {
                            Icon(Icons.Default.FilterList, "Filter", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
                
                item {
                    GradientGlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        gradientColors = listOf(
                            MysticPurple.copy(alpha = 0.3f),
                            CosmicBlue.copy(alpha = 0.2f)
                        )
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "✨ About Tarot",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "The Tarot is a deck of 78 cards, each with unique imagery and symbolism. The 22 Major Arcana represent life's karmic and spiritual lessons. The 56 Minor Arcana reflect daily trials and tribulations.",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
                
                item {
                    Text(
                        text = "${filteredCards.size} Cards",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                
                items(filteredCards) { card ->
                    TarotCardListItem(
                        card = card,
                        onClick = { selectedCard = card }
                    )
                }
            }
            
            if (showFilterMenu) {
                FilterDropdownMenu(
                    currentFilter = filterType,
                    onFilterSelected = { 
                        filterType = it
                        showFilterMenu = false
                    },
                    onDismiss = { showFilterMenu = false }
                )
            }
        } else {
            TarotCardDetailScreen(
                card = selectedCard!!,
                onBack = { selectedCard = null }
            )
        }
    }
}

@Composable
private fun TarotCardListItem(
    card: TarotCard,
    onClick: () -> Unit
) {
    GradientGlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        gradientColors = listOf(
            card.primaryColor.copy(alpha = 0.4f),
            card.primaryColor.copy(alpha = 0.2f)
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = card.emoji,
                fontSize = 48.sp
            )
            
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = card.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (card.suit != null) {
                        Text(
                            text = card.suit.displayName,
                            fontSize = 12.sp,
                            color = card.suit.color,
                            modifier = Modifier
                                .background(
                                    card.suit.color.copy(alpha = 0.2f),
                                    shape = MaterialTheme.shapes.small
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    } else {
                        Text(
                            text = "Major Arcana",
                            fontSize = 12.sp,
                            color = LuckyGold,
                            modifier = Modifier
                                .background(
                                    LuckyGold.copy(alpha = 0.2f),
                                    shape = MaterialTheme.shapes.small
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = card.uprightKeywords.take(3).joinToString(" • "),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
private fun TarotCardDetailScreen(
    card: TarotCard,
    onBack: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            TopBarWithBack(
                title = "${card.emoji} ${card.name}",
                subtitle = card.suit?.displayName ?: "Major Arcana",
                onBackClick = onBack
            )
        }
        
        // Card Overview
        item {
            GradientGlassCard(
                modifier = Modifier.fillMaxWidth(),
                gradientColors = listOf(
                    card.primaryColor.copy(alpha = 0.5f),
                    card.primaryColor.copy(alpha = 0.3f)
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = card.emoji,
                        fontSize = 100.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = card.name,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = card.number,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    if (card.element != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Element: ${card.element}",
                            fontSize = 14.sp,
                            color = card.primaryColor
                        )
                    }
                }
            }
        }
        
        // Description
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "📖 Description",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                    Text(
                        text = card.description,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                        lineHeight = 24.sp
                    )
                }
            }
        }
        
        // Upright Meaning
        item {
            GradientGlassCard(
                modifier = Modifier.fillMaxWidth(),
                gradientColors = listOf(
                    Color(0xFF2ECC71).copy(alpha = 0.3f),
                    Color(0xFF27AE60).copy(alpha = 0.2f)
                )
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "⬆️ Upright Meaning",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF27AE60)
                    )
                    Text(
                        text = card.uprightMeaning,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                        lineHeight = 24.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Keywords:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                    Text(
                        text = card.uprightKeywords.joinToString(" • "),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
        
        // Reversed Meaning
        item {
            GradientGlassCard(
                modifier = Modifier.fillMaxWidth(),
                gradientColors = listOf(
                    Color(0xFFE74C3C).copy(alpha = 0.3f),
                    Color(0xFFC0392B).copy(alpha = 0.2f)
                )
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "⬇️ Reversed Meaning",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFC0392B)
                    )
                    Text(
                        text = card.reversedMeaning,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                        lineHeight = 24.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Keywords:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                    Text(
                        text = card.reversedKeywords.joinToString(" • "),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
        
        // Symbolism
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "🔮 Symbolism",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(0.1f))
                    Text(
                        text = card.symbolism,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                        lineHeight = 24.sp
                    )
                }
            }
        }
        
        // In Different Contexts
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        text = "🎯 In Different Contexts",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(0.1f))
                    
                    // Love
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "💕 In Love",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFE91E63)
                        )
                        Text(
                            text = card.inLove,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            lineHeight = 22.sp
                        )
                    }
                    
                    // Career
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "💼 In Career",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF3498DB)
                        )
                        Text(
                            text = card.inCareer,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            lineHeight = 22.sp
                        )
                    }
                    
                    // Health
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "🏥 In Health",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF2ECC71)
                        )
                        Text(
                            text = card.inHealth,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            lineHeight = 22.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterDropdownMenu(
    currentFilter: TarotFilter,
    onFilterSelected: (TarotFilter) -> Unit,
    onDismiss: () -> Unit
) {
    DropdownMenu(
        expanded = true,
        onDismissRequest = onDismiss
    ) {
        TarotFilter.values().forEach { filter ->
            DropdownMenuItem(
                text = {
                    Text(
                        text = filter.displayName,
                        color = if (filter == currentFilter) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                },
                onClick = { onFilterSelected(filter) }
            )
        }
    }
}

enum class TarotFilter(val displayName: String) {
    ALL("All Cards (78)"),
    MAJOR("Major Arcana (22)"),
    WANDS("Wands - Fire (14)"),
    CUPS("Cups - Water (14)"),
    SWORDS("Swords - Air (14)"),
    PENTACLES("Pentacles - Earth (14)")
}

// Complete Tarot Database - All 78 Cards
private fun getAllTarotCards(): List<TarotCard> {
    return buildList {
        // MAJOR ARCANA (22 cards)
        add(TarotCard(
            name = "The Fool", number = "0", arcana = TarotArcana.MAJOR, suit = null,
            emoji = "🃏", primaryColor = Color(0xFFFFD700),
            uprightMeaning = "New beginnings, innocence, spontaneity, free spirit.",
            reversedMeaning = "Recklessness, taken advantage of, inconsideration.",
            uprightKeywords = listOf("New Beginnings", "Innocence", "Adventure"),
            reversedKeywords = listOf("Recklessness", "Naivety", "Foolishness"),
            description = "The Fool represents new beginnings and unlimited potential.",
            symbolism = "White rose (purity), small dog (loyalty), mountains (challenges).",
            inLove = "New romance, taking a chance on love.",
            inCareer = "New job opportunity, career change.",
            inHealth = "New health routine, staying positive.",
            element = "Air", astrology = "Uranus"
        ))
        
        add(TarotCard(
            name = "The Magician", number = "I", arcana = TarotArcana.MAJOR, suit = null,
            emoji = "🎩", primaryColor = Color(0xFFE74C3C),
            uprightMeaning = "Manifestation, resourcefulness, power, inspired action.",
            reversedMeaning = "Manipulation, poor planning, untapped talents.",
            uprightKeywords = listOf("Manifestation", "Power", "Action"),
            reversedKeywords = listOf("Manipulation", "Illusion", "Unused Potential"),
            description = "The Magician represents the power to manifest your desires.",
            symbolism = "Infinity symbol (unlimited potential), four suit symbols (mastery).",
            inLove = "Taking action in love, manifesting romance.",
            inCareer = "Using skills effectively, new opportunities.",
            inHealth = "Taking control of health, positive action.",
            element = "Air", astrology = "Mercury"
        ))
        
        add(TarotCard(
            name = "The High Priestess", number = "II", arcana = TarotArcana.MAJOR, suit = null,
            emoji = "🌙", primaryColor = Color(0xFF3498DB),
            uprightMeaning = "Intuition, sacred knowledge, divine feminine, subconscious mind.",
            reversedMeaning = "Secrets, disconnected from intuition, withdrawal.",
            uprightKeywords = listOf("Intuition", "Mystery", "Inner Voice"),
            reversedKeywords = listOf("Hidden Agendas", "Silence", "Repressed Feelings"),
            description = "The High Priestess represents intuition and inner wisdom.",
            symbolism = "Moon crown (intuition), Torah scroll (hidden knowledge).",
            inLove = "Trust your intuition, spiritual connection.",
            inCareer = "Listen to inner voice, hidden information.",
            inHealth = "Mind-body connection, trust instincts.",
            element = "Water", astrology = "Moon"
        ))
        
        add(TarotCard(
            name = "The Empress", number = "III", arcana = TarotArcana.MAJOR, suit = null,
            emoji = "👑", primaryColor = Color(0xFF27AE60),
            uprightMeaning = "Femininity, beauty, nature, nurturing, abundance.",
            reversedMeaning = "Creative block, dependence on others, emptiness.",
            uprightKeywords = listOf("Abundance", "Nurturing", "Fertility"),
            reversedKeywords = listOf("Dependence", "Smothering", "Neglect"),
            description = "The Empress represents abundance, creativity, and nurturing.",
            symbolism = "Wheat (abundance), Venus symbol (love and beauty).",
            inLove = "Deep love, nurturing relationship, pregnancy.",
            inCareer = "Creative success, abundance, growth.",
            inHealth = "Fertility, pregnancy, self-care.",
            element = "Earth", astrology = "Venus"
        ))
        
        add(TarotCard(
            name = "The Emperor", number = "IV", arcana = TarotArcana.MAJOR, suit = null,
            emoji = "⚔️", primaryColor = Color(0xFFE67E22),
            uprightMeaning = "Authority, establishment, structure, father figure.",
            reversedMeaning = "Domination, excessive control, lack of discipline.",
            uprightKeywords = listOf("Authority", "Structure", "Control"),
            reversedKeywords = listOf("Tyranny", "Rigidity", "Coldness"),
            description = "The Emperor represents authority, structure, and solid foundation.",
            symbolism = "Throne (authority), ram heads (Aries), armor (protection).",
            inLove = "Commitment, traditional relationship, stability.",
            inCareer = "Leadership role, structure, authority.",
            inHealth = "Discipline in health routine, strength.",
            element = "Fire", astrology = "Aries"
        ))
        
        add(TarotCard(
            name = "The Hierophant", number = "V", arcana = TarotArcana.MAJOR, suit = null,
            emoji = "⛪", primaryColor = Color(0xFF9B59B6),
            uprightMeaning = "Spiritual wisdom, religious beliefs, conformity, tradition.",
            reversedMeaning = "Personal beliefs, freedom, challenging the status quo.",
            uprightKeywords = listOf("Tradition", "Conformity", "Education"),
            reversedKeywords = listOf("Rebellion", "Subversiveness", "New Approaches"),
            description = "The Hierophant represents tradition, conformity, and spiritual guidance.",
            symbolism = "Triple crown (three worlds), crossed keys (heaven).",
            inLove = "Traditional commitment, marriage, shared values.",
            inCareer = "Following rules, traditional path, mentorship.",
            inHealth = "Traditional medicine, seeking expert advice.",
            element = "Earth", astrology = "Taurus"
        ))
        
        add(TarotCard(
            name = "The Lovers", number = "VI", arcana = TarotArcana.MAJOR, suit = null,
            emoji = "💕", primaryColor = Color(0xFFE91E63),
            uprightMeaning = "Love, harmony, relationships, values alignment, choices.",
            reversedMeaning = "Self-love, disharmony, imbalance, misalignment of values.",
            uprightKeywords = listOf("Love", "Union", "Choices"),
            reversedKeywords = listOf("Disharmony", "Imbalance", "Misalignment"),
            description = "The Lovers represents love, harmony, and important choices.",
            symbolism = "Angel (Raphael), tree of knowledge, naked figures (honesty).",
            inLove = "Deep connection, soulmate, important choice.",
            inCareer = "Partnership, collaboration, value alignment.",
            inHealth = "Balance, harmony, holistic health.",
            element = "Air", astrology = "Gemini"
        ))
        
        add(TarotCard(
            name = "The Chariot", number = "VII", arcana = TarotArcana.MAJOR, suit = null,
            emoji = "🏇", primaryColor = Color(0xFF34495E),
            uprightMeaning = "Control, willpower, success, action, determination.",
            reversedMeaning = "Self-discipline, opposition, lack of direction.",
            uprightKeywords = listOf("Willpower", "Victory", "Determination"),
            reversedKeywords = listOf("Lack of Control", "Aggression", "Obstacles"),
            description = "The Chariot represents willpower, determination, and victory.",
            symbolism = "Black and white sphinxes (opposing forces), armor (protection).",
            inLove = "Taking control, moving forward, overcoming obstacles.",
            inCareer = "Success through determination, promotion, victory.",
            inHealth = "Overcoming illness, strength, determination.",
            element = "Water", astrology = "Cancer"
        ))
        
        add(TarotCard(
            name = "Strength", number = "VIII", arcana = TarotArcana.MAJOR, suit = null,
            emoji = "🦁", primaryColor = Color(0xFFF39C12),
            uprightMeaning = "Strength, courage, persuasion, influence, compassion.",
            reversedMeaning = "Inner strength, self-doubt, low energy, raw emotion.",
            uprightKeywords = listOf("Courage", "Compassion", "Inner Strength"),
            reversedKeywords = listOf("Self-Doubt", "Weakness", "Insecurity"),
            description = "Strength represents inner strength, courage, and compassion.",
            symbolism = "Woman taming lion (inner strength), infinity symbol (unlimited potential).",
            inLove = "Patience, compassion, gentle strength.",
            inCareer = "Confidence, persuasion, overcoming challenges.",
            inHealth = "Recovery, inner strength, vitality.",
            element = "Fire", astrology = "Leo"
        ))
        
        add(TarotCard(
            name = "The Hermit", number = "IX", arcana = TarotArcana.MAJOR, suit = null,
            emoji = "🕯️", primaryColor = Color(0xFF95A5A6),
            uprightMeaning = "Soul searching, introspection, being alone, inner guidance.",
            reversedMeaning = "Isolation, loneliness, withdrawal.",
            uprightKeywords = listOf("Introspection", "Solitude", "Guidance"),
            reversedKeywords = listOf("Isolation", "Loneliness", "Withdrawal"),
            description = "The Hermit represents introspection, solitude, and inner guidance.",
            symbolism = "Lantern (inner light), staff (support), mountain (spiritual height).",
            inLove = "Soul searching, taking time alone, self-reflection.",
            inCareer = "Working alone, introspection, seeking guidance.",
            inHealth = "Rest, recovery, listening to body.",
            element = "Earth", astrology = "Virgo"
        ))
        
        add(TarotCard(
            name = "Wheel of Fortune", number = "X", arcana = TarotArcana.MAJOR, suit = null,
            emoji = "🎡", primaryColor = Color(0xFF3498DB),
            uprightMeaning = "Good luck, karma, life cycles, destiny, turning point.",
            reversedMeaning = "Bad luck, resistance to change, breaking cycles.",
            uprightKeywords = listOf("Destiny", "Change", "Cycles"),
            reversedKeywords = listOf("Bad Luck", "Resistance", "Breaking Cycles"),
            description = "Wheel of Fortune represents cycles, destiny, and turning points.",
            symbolism = "Wheel (cycles), sphinx (riddles), snake (descending), Anubis (ascending).",
            inLove = "Destiny, fated meeting, change in relationship.",
            inCareer = "Opportunity, change, good fortune.",
            inHealth = "Cycles, improvement, positive change.",
            element = "Fire", astrology = "Jupiter"
        ))
        
        add(TarotCard(
            name = "Justice", number = "XI", arcana = TarotArcana.MAJOR, suit = null,
            emoji = "⚖️", primaryColor = Color(0xFF16A085),
            uprightMeaning = "Justice, fairness, truth, cause and effect, law.",
            reversedMeaning = "Unfairness, lack of accountability, dishonesty.",
            uprightKeywords = listOf("Justice", "Fairness", "Truth"),
            reversedKeywords = listOf("Unfairness", "Dishonesty", "Unaccountability"),
            description = "Justice represents fairness, truth, and the law of cause and effect.",
            symbolism = "Scales (balance), sword (clarity), purple robe (compassion).",
            inLove = "Fairness, balance, karmic relationship.",
            inCareer = "Legal matters, fairness, contracts.",
            inHealth = "Balance, cause and effect, accountability.",
            element = "Air", astrology = "Libra"
        ))
        
        add(TarotCard(
            name = "The Hanged Man", number = "XII", arcana = TarotArcana.MAJOR, suit = null,
            emoji = "🙃", primaryColor = Color(0xFF3498DB),
            uprightMeaning = "Pause, surrender, letting go, new perspectives.",
            reversedMeaning = "Delays, resistance, stalling, indecision.",
            uprightKeywords = listOf("Surrender", "New Perspective", "Waiting"),
            reversedKeywords = listOf("Stalling", "Resistance", "Indecision"),
            description = "The Hanged Man represents surrender, new perspectives, and waiting.",
            symbolism = "Upside down (new perspective), halo (enlightenment), calm expression (acceptance).",
            inLove = "Letting go, new perspective, patience.",
            inCareer = "Pause, reassessment, sacrifice.",
            inHealth = "Rest, acceptance, new approach.",
            element = "Water", astrology = "Neptune"
        ))
        
        add(TarotCard(
            name = "Death", number = "XIII", arcana = TarotArcana.MAJOR, suit = null,
            emoji = "💀", primaryColor = Color(0xFF2C3E50),
            uprightMeaning = "Endings, change, transformation, transition.",
            reversedMeaning = "Resistance to change, personal transformation, inner purging.",
            uprightKeywords = listOf("Transformation", "Endings", "Change"),
            reversedKeywords = listOf("Resistance", "Stagnation", "Decay"),
            description = "Death represents transformation, endings, and new beginnings.",
            symbolism = "Skeleton (death comes to all), white rose (purity), rising sun (rebirth).",
            inLove = "Transformation, ending of old patterns, rebirth.",
            inCareer = "Career change, transformation, new beginning.",
            inHealth = "Recovery, transformation, letting go.",
            element = "Water", astrology = "Scorpio"
        ))
        
        add(TarotCard(
            name = "Temperance", number = "XIV", arcana = TarotArcana.MAJOR, suit = null,
            emoji = "🧘", primaryColor = Color(0xFF9B59B6),
            uprightMeaning = "Balance, moderation, patience, purpose, meaning.",
            reversedMeaning = "Imbalance, excess, self-healing, re-alignment.",
            uprightKeywords = listOf("Balance", "Moderation", "Patience"),
            reversedKeywords = listOf("Imbalance", "Excess", "Lack of Harmony"),
            description = "Temperance represents balance, moderation, and patience.",
            symbolism = "Angel (divine), mixing water (alchemy), one foot on land/water (balance).",
            inLove = "Balance, patience, harmony.",
            inCareer = "Teamwork, balance, moderation.",
            inHealth = "Balance, moderation, healing.",
            element = "Fire", astrology = "Sagittarius"
        ))
        
        add(TarotCard(
            name = "The Devil", number = "XV", arcana = TarotArcana.MAJOR, suit = null,
            emoji = "😈", primaryColor = Color(0xFF8E44AD),
            uprightMeaning = "Shadow self, attachment, addiction, restriction, sexuality.",
            reversedMeaning = "Releasing limiting beliefs, exploring dark thoughts, detachment.",
            uprightKeywords = listOf("Bondage", "Addiction", "Materialism"),
            reversedKeywords = listOf("Release", "Detachment", "Breaking Free"),
            description = "The Devil represents bondage, materialism, and shadow self.",
            symbolism = "Chains (bondage), inverted pentagram (dark magic), horns (animal nature).",
            inLove = "Obsession, unhealthy attachment, passion.",
            inCareer = "Materialism, feeling trapped, addiction to work.",
            inHealth = "Addiction, unhealthy habits, excess.",
            element = "Earth", astrology = "Capricorn"
        ))
        
        add(TarotCard(
            name = "The Tower", number = "XVI", arcana = TarotArcana.MAJOR, suit = null,
            emoji = "⚡", primaryColor = Color(0xFFE74C3C),
            uprightMeaning = "Sudden change, upheaval, chaos, revelation, awakening.",
            reversedMeaning = "Personal transformation, fear of change, averting disaster.",
            uprightKeywords = listOf("Upheaval", "Chaos", "Revelation"),
            reversedKeywords = listOf("Averted Disaster", "Fear of Change", "Delayed Disaster"),
            description = "The Tower represents sudden change, upheaval, and revelation.",
            symbolism = "Lightning (sudden change), falling figures (ego destruction), crown (false beliefs).",
            inLove = "Sudden change, revelation, breakdown.",
            inCareer = "Sudden change, job loss, revelation.",
            inHealth = "Sudden illness, wake-up call, crisis.",
            element = "Fire", astrology = "Mars"
        ))
        
        add(TarotCard(
            name = "The Star", number = "XVII", arcana = TarotArcana.MAJOR, suit = null,
            emoji = "⭐", primaryColor = Color(0xFF3498DB),
            uprightMeaning = "Hope, faith, purpose, renewal, spirituality.",
            reversedMeaning = "Lack of faith, despair, self-trust, disconnection.",
            uprightKeywords = listOf("Hope", "Faith", "Renewal"),
            reversedKeywords = listOf("Despair", "Faithlessness", "Discouragement"),
            description = "The Star represents hope, faith, and spiritual renewal.",
            symbolism = "Eight stars (chakras), naked woman (truth), water (subconscious).",
            inLove = "Hope, renewal, spiritual connection.",
            inCareer = "Inspiration, hope, new opportunities.",
            inHealth = "Healing, renewal, hope.",
            element = "Air", astrology = "Aquarius"
        ))
        
        add(TarotCard(
            name = "The Moon", number = "XVIII", arcana = TarotArcana.MAJOR, suit = null,
            emoji = "🌙", primaryColor = Color(0xFF9B59B6),
            uprightMeaning = "Illusion, fear, anxiety, subconscious, intuition.",
            reversedMeaning = "Release of fear, repressed emotion, inner confusion.",
            uprightKeywords = listOf("Illusion", "Intuition", "Uncertainty"),
            reversedKeywords = listOf("Release", "Clarity", "Understanding"),
            description = "The Moon represents illusion, intuition, and the subconscious.",
            symbolism = "Moon (subconscious), dog and wolf (tamed/wild), crayfish (early consciousness).",
            inLove = "Uncertainty, illusion, intuition.",
            inCareer = "Confusion, deception, trust intuition.",
            inHealth = "Mental health, anxiety, trust instincts.",
            element = "Water", astrology = "Pisces"
        ))
        
        add(TarotCard(
            name = "The Sun", number = "XIX", arcana = TarotArcana.MAJOR, suit = null,
            emoji = "☀️", primaryColor = Color(0xFFF39C12),
            uprightMeaning = "Positivity, fun, warmth, success, vitality.",
            reversedMeaning = "Inner child, feeling down, overly optimistic.",
            uprightKeywords = listOf("Joy", "Success", "Celebration"),
            reversedKeywords = listOf("Sadness", "Lack of Enthusiasm", "Pessimism"),
            description = "The Sun represents positivity, success, and vitality.",
            symbolism = "Sun (consciousness), child (innocence), sunflowers (life), white horse (purity).",
            inLove = "Joy, success, warmth, happiness.",
            inCareer = "Success, recognition, achievement.",
            inHealth = "Vitality, energy, good health.",
            element = "Fire", astrology = "Sun"
        ))
        
        add(TarotCard(
            name = "Judgement", number = "XX", arcana = TarotArcana.MAJOR, suit = null,
            emoji = "📯", primaryColor = Color(0xFFE74C3C),
            uprightMeaning = "Judgement, rebirth, inner calling, absolution.",
            reversedMeaning = "Self-doubt, inner critic, ignoring the call.",
            uprightKeywords = listOf("Judgement", "Rebirth", "Awakening"),
            reversedKeywords = listOf("Self-Doubt", "Refusal", "Lack of Self-Awareness"),
            description = "Judgement represents rebirth, inner calling, and absolution.",
            symbolism = "Angel (Gabriel), trumpet (call), rising figures (resurrection).",
            inLove = "Renewal, forgiveness, rebirth.",
            inCareer = "Career calling, evaluation, new phase.",
            inHealth = "Recovery, renewal, awakening.",
            element = "Fire", astrology = "Pluto"
        ))
        
        add(TarotCard(
            name = "The World", number = "XXI", arcana = TarotArcana.MAJOR, suit = null,
            emoji = "🌍", primaryColor = Color(0xFF27AE60),
            uprightMeaning = "Completion, integration, accomplishment, travel.",
            reversedMeaning = "Seeking personal closure, short-cuts, delays.",
            uprightKeywords = listOf("Completion", "Accomplishment", "Fulfillment"),
            reversedKeywords = listOf("Incompletion", "Shortcuts", "Delays"),
            description = "The World represents completion, accomplishment, and fulfillment.",
            symbolism = "Wreath (success), four figures (four elements), dancing figure (cosmic consciousness).",
            inLove = "Completion, fulfillment, soulmate.",
            inCareer = "Success, completion, achievement.",
            inHealth = "Wholeness, completion, vitality.",
            element = "Earth", astrology = "Saturn"
        ))
        
        // MINOR ARCANA - WANDS (14 cards)
        addAll(getWandsCards())
        
        // MINOR ARCANA - CUPS (14 cards)
        addAll(getCupsCards())
        
        // MINOR ARCANA - SWORDS (14 cards)
        addAll(getSwordsCards())
        
        // MINOR ARCANA - PENTACLES (14 cards)
        addAll(getPentaclesCards())
    }
}

// WANDS - Fire Element (14 cards)
private fun getWandsCards(): List<TarotCard> = listOf(
    TarotCard(
        name = "Ace of Wands", number = "Ace", arcana = TarotArcana.MINOR, suit = TarotSuit.WANDS,
        emoji = "🔥", primaryColor = Color(0xFFE74C3C),
        uprightMeaning = "Inspiration, new opportunities, growth, potential.",
        reversedMeaning = "Emerging idea, lack of direction, distractions, delays.",
        uprightKeywords = listOf("Inspiration", "New Opportunities", "Growth"),
        reversedKeywords = listOf("Delays", "Lack of Direction", "Distractions"),
        description = "The Ace of Wands represents new creative beginnings and inspiration.",
        symbolism = "Hand emerging from cloud (divine inspiration), sprouting wand (growth).",
        inLove = "New passionate relationship, spark of romance.",
        inCareer = "New job opportunity, creative project, inspiration.",
        inHealth = "Renewed energy, vitality, new health routine.",
        element = "Fire", astrology = null
    ),
    TarotCard(
        name = "Two of Wands", number = "2", arcana = TarotArcana.MINOR, suit = TarotSuit.WANDS,
        emoji = "🌍", primaryColor = Color(0xFFE74C3C),
        uprightMeaning = "Future planning, progress, decisions, discovery.",
        reversedMeaning = "Personal goals, inner alignment, fear of unknown, lack of planning.",
        uprightKeywords = listOf("Planning", "Progress", "Decisions"),
        reversedKeywords = listOf("Fear", "Lack of Planning", "Disorganization"),
        description = "The Two of Wands represents planning and making decisions about the future.",
        symbolism = "Figure holding globe (world is yours), two wands (choices).",
        inLove = "Planning future together, long-distance relationship.",
        inCareer = "Planning career path, expansion, partnerships.",
        inHealth = "Planning health goals, looking ahead.",
        element = "Fire", astrology = "Mars in Aries"
    ),
    TarotCard(
        name = "Three of Wands", number = "3", arcana = TarotArcana.MINOR, suit = TarotSuit.WANDS,
        emoji = "⛵", primaryColor = Color(0xFFE74C3C),
        uprightMeaning = "Progress, expansion, foresight, overseas opportunities.",
        reversedMeaning = "Playing small, lack of foresight, unexpected delays.",
        uprightKeywords = listOf("Expansion", "Foresight", "Opportunities"),
        reversedKeywords = listOf("Delays", "Obstacles", "Lack of Progress"),
        description = "The Three of Wands represents expansion and looking ahead to the future.",
        symbolism = "Figure watching ships (opportunities), three wands (established foundation).",
        inLove = "Long-distance love, expansion in relationship.",
        inCareer = "Business expansion, overseas opportunities, growth.",
        inHealth = "Progress in health, looking ahead positively.",
        element = "Fire", astrology = "Sun in Aries"
    ),
    TarotCard(
        name = "Four of Wands", number = "4", arcana = TarotArcana.MINOR, suit = TarotSuit.WANDS,
        emoji = "🎉", primaryColor = Color(0xFFE74C3C),
        uprightMeaning = "Celebration, joy, harmony, relaxation, homecoming.",
        reversedMeaning = "Personal celebration, inner harmony, conflict with others, transition.",
        uprightKeywords = listOf("Celebration", "Harmony", "Home"),
        reversedKeywords = listOf("Conflict", "Lack of Harmony", "Transition"),
        description = "The Four of Wands represents celebration, harmony, and homecoming.",
        symbolism = "Garland between wands (celebration), couple dancing (joy).",
        inLove = "Engagement, wedding, celebration of love.",
        inCareer = "Work celebration, milestone achieved, teamwork.",
        inHealth = "Celebration of health milestone, harmony.",
        element = "Fire", astrology = "Venus in Aries"
    ),
    TarotCard(
        name = "Five of Wands", number = "5", arcana = TarotArcana.MINOR, suit = TarotSuit.WANDS,
        emoji = "⚔️", primaryColor = Color(0xFFE74C3C),
        uprightMeaning = "Conflict, disagreements, competition, tension, diversity.",
        reversedMeaning = "Inner conflict, conflict avoidance, tension release.",
        uprightKeywords = listOf("Conflict", "Competition", "Tension"),
        reversedKeywords = listOf("Avoidance", "Resolution", "Inner Conflict"),
        description = "The Five of Wands represents conflict, competition, and disagreements.",
        symbolism = "Five figures with wands (conflict), chaotic scene (tension).",
        inLove = "Arguments, competition, tension in relationship.",
        inCareer = "Workplace conflict, competition, differing opinions.",
        inHealth = "Stress, tension, need for balance.",
        element = "Fire", astrology = "Saturn in Leo"
    ),
    TarotCard(
        name = "Six of Wands", number = "6", arcana = TarotArcana.MINOR, suit = TarotSuit.WANDS,
        emoji = "🏆", primaryColor = Color(0xFFE74C3C),
        uprightMeaning = "Success, public recognition, progress, self-confidence.",
        reversedMeaning = "Private achievement, personal definition of success, fall from grace.",
        uprightKeywords = listOf("Victory", "Recognition", "Success"),
        reversedKeywords = listOf("Ego", "Lack of Recognition", "Failure"),
        description = "The Six of Wands represents victory, success, and public recognition.",
        symbolism = "Figure on horse with wreath (victory), crowd (recognition).",
        inLove = "Success in love, public recognition of relationship.",
        inCareer = "Promotion, recognition, achievement, victory.",
        inHealth = "Recovery, success in health goals.",
        element = "Fire", astrology = "Jupiter in Leo"
    ),
    TarotCard(
        name = "Seven of Wands", number = "7", arcana = TarotArcana.MINOR, suit = TarotSuit.WANDS,
        emoji = "🛡️", primaryColor = Color(0xFFE74C3C),
        uprightMeaning = "Challenge, competition, protection, perseverance.",
        reversedMeaning = "Exhaustion, giving up, overwhelmed.",
        uprightKeywords = listOf("Defense", "Perseverance", "Challenge"),
        reversedKeywords = listOf("Giving Up", "Overwhelmed", "Exhaustion"),
        description = "The Seven of Wands represents defending your position and perseverance.",
        symbolism = "Figure on high ground (advantage), defending against six wands (challenges).",
        inLove = "Defending relationship, standing your ground.",
        inCareer = "Defending position, competition, perseverance.",
        inHealth = "Fighting illness, maintaining health.",
        element = "Fire", astrology = "Mars in Leo"
    ),
    TarotCard(
        name = "Eight of Wands", number = "8", arcana = TarotArcana.MINOR, suit = TarotSuit.WANDS,
        emoji = "⚡", primaryColor = Color(0xFFE74C3C),
        uprightMeaning = "Rapid action, movement, quick decisions, air travel.",
        reversedMeaning = "Delays, frustration, resisting change, internal alignment.",
        uprightKeywords = listOf("Speed", "Action", "Movement"),
        reversedKeywords = listOf("Delays", "Frustration", "Slowness"),
        description = "The Eight of Wands represents rapid action and swift movement.",
        symbolism = "Eight wands flying through air (speed), clear sky (clarity).",
        inLove = "Fast-moving relationship, exciting developments.",
        inCareer = "Fast-paced work, quick results, travel.",
        inHealth = "Quick recovery, rapid improvement.",
        element = "Fire", astrology = "Mercury in Sagittarius"
    ),
    TarotCard(
        name = "Nine of Wands", number = "9", arcana = TarotArcana.MINOR, suit = TarotSuit.WANDS,
        emoji = "💪", primaryColor = Color(0xFFE74C3C),
        uprightMeaning = "Resilience, courage, persistence, test of faith, boundaries.",
        reversedMeaning = "Inner resources, struggle, overwhelm, defensive, paranoia.",
        uprightKeywords = listOf("Resilience", "Persistence", "Boundaries"),
        reversedKeywords = listOf("Paranoia", "Defensiveness", "Weakness"),
        description = "The Nine of Wands represents resilience and standing strong despite challenges.",
        symbolism = "Wounded figure with eight wands behind (past battles), one wand held (readiness).",
        inLove = "Guarded heart, resilience in relationship.",
        inCareer = "Nearly there, persistence needed, boundaries.",
        inHealth = "Recovery, resilience, maintaining strength.",
        element = "Fire", astrology = "Moon in Sagittarius"
    ),
    TarotCard(
        name = "Ten of Wands", number = "10", arcana = TarotArcana.MINOR, suit = TarotSuit.WANDS,
        emoji = "😰", primaryColor = Color(0xFFE74C3C),
        uprightMeaning = "Burden, extra responsibility, hard work, completion.",
        reversedMeaning = "Doing it all, carrying the burden, delegation, release.",
        uprightKeywords = listOf("Burden", "Responsibility", "Hard Work"),
        reversedKeywords = listOf("Delegation", "Release", "Lightening Load"),
        description = "The Ten of Wands represents burdens, responsibility, and hard work.",
        symbolism = "Figure carrying ten wands (burden), bent over (weight), home ahead (goal).",
        inLove = "Relationship burden, too much responsibility.",
        inCareer = "Overworked, too much responsibility, burnout.",
        inHealth = "Stress, exhaustion, need to lighten load.",
        element = "Fire", astrology = "Saturn in Sagittarius"
    ),
    TarotCard(
        name = "Page of Wands", number = "Page", arcana = TarotArcana.MINOR, suit = TarotSuit.WANDS,
        emoji = "🎭", primaryColor = Color(0xFFE74C3C),
        uprightMeaning = "Inspiration, ideas, discovery, limitless potential, free spirit.",
        reversedMeaning = "Newly-formed ideas, redirecting energy, self-limiting beliefs, procrastination.",
        uprightKeywords = listOf("Enthusiasm", "Exploration", "Discovery"),
        reversedKeywords = listOf("Procrastination", "Lack of Direction", "Setbacks"),
        description = "The Page of Wands represents enthusiasm, exploration, and new ideas.",
        symbolism = "Young figure with wand (potential), desert (adventure), salamanders (transformation).",
        inLove = "Exciting new romance, playful energy.",
        inCareer = "New project, creative ideas, enthusiasm.",
        inHealth = "New health routine, enthusiasm for fitness.",
        element = "Fire", astrology = null
    ),
    TarotCard(
        name = "Knight of Wands", number = "Knight", arcana = TarotArcana.MINOR, suit = TarotSuit.WANDS,
        emoji = "🏇", primaryColor = Color(0xFFE74C3C),
        uprightMeaning = "Energy, passion, inspired action, adventure, impulsiveness.",
        reversedMeaning = "Passion project, haste, scattered energy, delays, frustration.",
        uprightKeywords = listOf("Action", "Adventure", "Passion"),
        reversedKeywords = listOf("Recklessness", "Impatience", "Delays"),
        description = "The Knight of Wands represents action, adventure, and passionate pursuit.",
        symbolism = "Knight on rearing horse (action), armor with salamanders (passion).",
        inLove = "Passionate romance, adventure, excitement.",
        inCareer = "Taking action, pursuing goals passionately.",
        inHealth = "High energy, active lifestyle.",
        element = "Fire", astrology = null
    ),
    TarotCard(
        name = "Queen of Wands", number = "Queen", arcana = TarotArcana.MINOR, suit = TarotSuit.WANDS,
        emoji = "👸", primaryColor = Color(0xFFE74C3C),
        uprightMeaning = "Courage, confidence, independence, social butterfly, determination.",
        reversedMeaning = "Self-respect, self-confidence, introverted, re-establish sense of self.",
        uprightKeywords = listOf("Confidence", "Independence", "Determination"),
        reversedKeywords = listOf("Selfishness", "Jealousy", "Insecurity"),
        description = "The Queen of Wands represents confidence, independence, and determination.",
        symbolism = "Queen with sunflower (joy), black cat (intuition), lions (strength).",
        inLove = "Confident partner, passionate relationship.",
        inCareer = "Leadership, confidence, entrepreneurship.",
        inHealth = "Vitality, confidence in health.",
        element = "Fire", astrology = null
    ),
    TarotCard(
        name = "King of Wands", number = "King", arcana = TarotArcana.MINOR, suit = TarotSuit.WANDS,
        emoji = "🤴", primaryColor = Color(0xFFE74C3C),
        uprightMeaning = "Natural-born leader, vision, entrepreneur, honour.",
        reversedMeaning = "Impulsiveness, haste, ruthless, high expectations.",
        uprightKeywords = listOf("Leadership", "Vision", "Entrepreneur"),
        reversedKeywords = listOf("Ruthlessness", "Impulsiveness", "Arrogance"),
        description = "The King of Wands represents leadership, vision, and entrepreneurship.",
        symbolism = "King with salamander robe (transformation), lions (courage).",
        inLove = "Passionate leader, protective partner.",
        inCareer = "Leadership role, entrepreneurship, vision.",
        inHealth = "Vitality, taking charge of health.",
        element = "Fire", astrology = null
    )
)

// CUPS - Water Element (14 cards)
private fun getCupsCards(): List<TarotCard> = listOf(
    TarotCard(
        name = "Ace of Cups", number = "Ace", arcana = TarotArcana.MINOR, suit = TarotSuit.CUPS,
        emoji = "💧", primaryColor = Color(0xFF3498DB),
        uprightMeaning = "Love, new relationships, compassion, creativity.",
        reversedMeaning = "Self-love, intuition, repressed emotions.",
        uprightKeywords = listOf("Love", "Compassion", "Creativity"),
        reversedKeywords = listOf("Blocked Emotions", "Emptiness", "Repression"),
        description = "The Ace of Cups represents new love, emotional beginnings, and compassion.",
        symbolism = "Hand holding cup (divine gift), overflowing water (abundance), dove (peace).",
        inLove = "New love, emotional connection, romance.",
        inCareer = "Creative opportunities, emotional fulfillment.",
        inHealth = "Emotional healing, self-care.",
        element = "Water", astrology = null
    ),
    TarotCard(
        name = "Two of Cups", number = "2", arcana = TarotArcana.MINOR, suit = TarotSuit.CUPS,
        emoji = "💑", primaryColor = Color(0xFF3498DB),
        uprightMeaning = "Unified love, partnership, mutual attraction.",
        reversedMeaning = "Self-love, break-ups, disharmony, distrust.",
        uprightKeywords = listOf("Partnership", "Unity", "Love"),
        reversedKeywords = listOf("Break-up", "Imbalance", "Tension"),
        description = "The Two of Cups represents partnership, mutual attraction, and unity.",
        symbolism = "Two figures exchanging cups (partnership), caduceus (healing), lion (passion).",
        inLove = "Deep connection, partnership, mutual love.",
        inCareer = "Partnership, collaboration, teamwork.",
        inHealth = "Balance, partnership in health journey.",
        element = "Water", astrology = "Venus in Cancer"
    ),
    TarotCard(
        name = "Three of Cups", number = "3", arcana = TarotArcana.MINOR, suit = TarotSuit.CUPS,
        emoji = "🎊", primaryColor = Color(0xFF3498DB),
        uprightMeaning = "Celebration, friendship, creativity, collaborations.",
        reversedMeaning = "Independence, alone time, hardcore partying, 'three's a crowd'.",
        uprightKeywords = listOf("Celebration", "Friendship", "Community"),
        reversedKeywords = listOf("Overindulgence", "Gossip", "Isolation"),
        description = "The Three of Cups represents celebration, friendship, and community.",
        symbolism = "Three women raising cups (celebration), flowers (abundance), dancing (joy).",
        inLove = "Celebration of love, social connections.",
        inCareer = "Teamwork, collaboration, celebration.",
        inHealth = "Social support, celebration of health.",
        element = "Water", astrology = "Mercury in Cancer"
    ),
    TarotCard(
        name = "Four of Cups", number = "4", arcana = TarotArcana.MINOR, suit = TarotSuit.CUPS,
        emoji = "😐", primaryColor = Color(0xFF3498DB),
        uprightMeaning = "Meditation, contemplation, apathy, reevaluation.",
        reversedMeaning = "Retreat, withdrawal, checking in for alignment.",
        uprightKeywords = listOf("Apathy", "Contemplation", "Reevaluation"),
        reversedKeywords = listOf("Awareness", "Acceptance", "Moving Forward"),
        description = "The Four of Cups represents apathy, contemplation, and reevaluation.",
        symbolism = "Figure under tree (meditation), three cups (dissatisfaction), fourth cup offered (opportunity).",
        inLove = "Apathy in relationship, taking for granted.",
        inCareer = "Boredom, contemplation, missed opportunities.",
        inHealth = "Apathy, need for motivation.",
        element = "Water", astrology = "Moon in Cancer"
    ),
    TarotCard(
        name = "Five of Cups", number = "5", arcana = TarotArcana.MINOR, suit = TarotSuit.CUPS,
        emoji = "😢", primaryColor = Color(0xFF3498DB),
        uprightMeaning = "Regret, failure, disappointment, pessimism.",
        reversedMeaning = "Personal setbacks, self-forgiveness, moving on.",
        uprightKeywords = listOf("Loss", "Regret", "Disappointment"),
        reversedKeywords = listOf("Acceptance", "Moving On", "Forgiveness"),
        description = "The Five of Cups represents loss, regret, and disappointment.",
        symbolism = "Figure mourning spilled cups (loss), two standing cups (hope), bridge (moving forward).",
        inLove = "Heartbreak, disappointment, loss.",
        inCareer = "Failure, disappointment, setback.",
        inHealth = "Grief, disappointment in health.",
        element = "Water", astrology = "Mars in Scorpio"
    ),
    TarotCard(
        name = "Six of Cups", number = "6", arcana = TarotArcana.MINOR, suit = TarotSuit.CUPS,
        emoji = "🌸", primaryColor = Color(0xFF3498DB),
        uprightMeaning = "Revisiting the past, childhood memories, innocence, joy.",
        reversedMeaning = "Living in past, forgiveness, lacking playfulness.",
        uprightKeywords = listOf("Nostalgia", "Memories", "Innocence"),
        reversedKeywords = listOf("Stuck in Past", "Naivety", "Unrealistic"),
        description = "The Six of Cups represents nostalgia, childhood memories, and innocence.",
        symbolism = "Children with cups of flowers (innocence), old town (past).",
        inLove = "Reunion, childhood sweetheart, nostalgia.",
        inCareer = "Past connections, nostalgia, simpler times.",
        inHealth = "Healing from past, inner child work.",
        element = "Water", astrology = "Sun in Scorpio"
    ),
    TarotCard(
        name = "Seven of Cups", number = "7", arcana = TarotArcana.MINOR, suit = TarotSuit.CUPS,
        emoji = "☁️", primaryColor = Color(0xFF3498DB),
        uprightMeaning = "Opportunities, choices, wishful thinking, illusion.",
        reversedMeaning = "Alignment, personal values, overwhelmed by choices.",
        uprightKeywords = listOf("Choices", "Illusion", "Fantasy"),
        reversedKeywords = listOf("Clarity", "Values", "Focus"),
        description = "The Seven of Cups represents choices, illusions, and wishful thinking.",
        symbolism = "Seven cups with different visions (choices), clouds (illusion).",
        inLove = "Many options, illusions, unrealistic expectations.",
        inCareer = "Many opportunities, confusion, daydreaming.",
        inHealth = "Confusion, unrealistic goals.",
        element = "Water", astrology = "Venus in Scorpio"
    ),
    TarotCard(
        name = "Eight of Cups", number = "8", arcana = TarotArcana.MINOR, suit = TarotSuit.CUPS,
        emoji = "🚶", primaryColor = Color(0xFF3498DB),
        uprightMeaning = "Disappointment, abandonment, withdrawal, escapism.",
        reversedMeaning = "Trying one more time, indecision, aimless drifting, walking away.",
        uprightKeywords = listOf("Walking Away", "Withdrawal", "Escapism"),
        reversedKeywords = listOf("Stagnation", "Avoidance", "Fear of Change"),
        description = "The Eight of Cups represents walking away and seeking deeper meaning.",
        symbolism = "Figure leaving cups (abandonment), mountains (spiritual journey), moon (intuition).",
        inLove = "Leaving relationship, seeking more.",
        inCareer = "Leaving job, seeking fulfillment.",
        inHealth = "Leaving unhealthy habits, seeking better.",
        element = "Water", astrology = "Saturn in Pisces"
    ),
    TarotCard(
        name = "Nine of Cups", number = "9", arcana = TarotArcana.MINOR, suit = TarotSuit.CUPS,
        emoji = "😊", primaryColor = Color(0xFF3498DB),
        uprightMeaning = "Contentment, satisfaction, gratitude, wish come true.",
        reversedMeaning = "Inner happiness, materialism, dissatisfaction, indulgence.",
        uprightKeywords = listOf("Satisfaction", "Wishes", "Contentment"),
        reversedKeywords = listOf("Greed", "Dissatisfaction", "Materialism"),
        description = "The Nine of Cups represents contentment, satisfaction, and wishes fulfilled.",
        symbolism = "Figure with arms crossed (satisfaction), nine cups displayed (abundance).",
        inLove = "Emotional fulfillment, happiness in love.",
        inCareer = "Success, satisfaction, achievement.",
        inHealth = "Good health, satisfaction.",
        element = "Water", astrology = "Jupiter in Pisces"
    ),
    TarotCard(
        name = "Ten of Cups", number = "10", arcana = TarotArcana.MINOR, suit = TarotSuit.CUPS,
        emoji = "👨‍👩‍👧‍👦", primaryColor = Color(0xFF3498DB),
        uprightMeaning = "Divine love, blissful relationships, harmony, alignment.",
        reversedMeaning = "Disconnection, misaligned values, struggling relationships.",
        uprightKeywords = listOf("Harmony", "Family", "Happiness"),
        reversedKeywords = listOf("Disharmony", "Broken Family", "Values Clash"),
        description = "The Ten of Cups represents harmony, happiness, and family bliss.",
        symbolism = "Family with rainbow (blessings), ten cups (emotional fulfillment), home (stability).",
        inLove = "Perfect relationship, family happiness.",
        inCareer = "Work-life balance, harmonious team.",
        inHealth = "Emotional wellbeing, family support.",
        element = "Water", astrology = "Mars in Pisces"
    ),
    TarotCard(
        name = "Page of Cups", number = "Page", arcana = TarotArcana.MINOR, suit = TarotSuit.CUPS,
        emoji = "🐠", primaryColor = Color(0xFF3498DB),
        uprightMeaning = "Creative opportunities, intuitive messages, curiosity, possibility.",
        reversedMeaning = "New ideas, doubting intuition, creative blocks, emotional immaturity.",
        uprightKeywords = listOf("Creativity", "Intuition", "Curiosity"),
        reversedKeywords = listOf("Emotional Immaturity", "Insecurity", "Blocked Creativity"),
        description = "The Page of Cups represents creativity, intuition, and new emotional experiences.",
        symbolism = "Young figure with cup (emotional openness), fish emerging (surprise), fancy clothes (creativity).",
        inLove = "New romance, emotional messages, sweet gestures.",
        inCareer = "Creative opportunities, intuitive insights.",
        inHealth = "Emotional healing, intuitive health insights.",
        element = "Water", astrology = null
    ),
    TarotCard(
        name = "Knight of Cups", number = "Knight", arcana = TarotArcana.MINOR, suit = TarotSuit.CUPS,
        emoji = "🎨", primaryColor = Color(0xFF3498DB),
        uprightMeaning = "Creativity, romance, charm, imagination, beauty.",
        reversedMeaning = "Overactive imagination, unrealistic, jealous, moody.",
        uprightKeywords = listOf("Romance", "Charm", "Imagination"),
        reversedKeywords = listOf("Moodiness", "Jealousy", "Unrealistic"),
        description = "The Knight of Cups represents romance, charm, and creative pursuit.",
        symbolism = "Knight with cup (romantic gesture), winged helmet (imagination), calm horse (emotions).",
        inLove = "Romantic gestures, charming partner, proposal.",
        inCareer = "Creative projects, following passion.",
        inHealth = "Emotional balance, creative healing.",
        element = "Water", astrology = null
    ),
    TarotCard(
        name = "Queen of Cups", number = "Queen", arcana = TarotArcana.MINOR, suit = TarotSuit.CUPS,
        emoji = "🧜‍♀️", primaryColor = Color(0xFF3498DB),
        uprightMeaning = "Compassionate, caring, emotionally stable, intuitive, in flow.",
        reversedMeaning = "Inner feelings, self-care, self-love, co-dependency.",
        uprightKeywords = listOf("Compassion", "Intuition", "Emotional Stability"),
        reversedKeywords = listOf("Insecurity", "Giving Too Much", "Martyrdom"),
        description = "The Queen of Cups represents compassion, intuition, and emotional maturity.",
        symbolism = "Queen with ornate cup (emotions), throne by water (subconscious), calm sea (emotional stability).",
        inLove = "Nurturing partner, emotional depth, intuitive connection.",
        inCareer = "Counseling, healing professions, intuitive work.",
        inHealth = "Emotional wellbeing, intuitive healing.",
        element = "Water", astrology = null
    ),
    TarotCard(
        name = "King of Cups", number = "King", arcana = TarotArcana.MINOR, suit = TarotSuit.CUPS,
        emoji = "🌊", primaryColor = Color(0xFF3498DB),
        uprightMeaning = "Emotionally balanced, compassionate, diplomatic.",
        reversedMeaning = "Self-compassion, inner feelings, moodiness, emotionally manipulative.",
        uprightKeywords = listOf("Emotional Balance", "Compassion", "Diplomacy"),
        reversedKeywords = listOf("Manipulation", "Moodiness", "Volatility"),
        description = "The King of Cups represents emotional balance, compassion, and diplomacy.",
        symbolism = "King with cup (emotional mastery), turbulent sea (controlled emotions), fish (subconscious).",
        inLove = "Emotionally mature partner, balanced relationship.",
        inCareer = "Leadership with compassion, emotional intelligence.",
        inHealth = "Emotional balance, mental health.",
        element = "Water", astrology = null
    )
)

// SWORDS - Air Element (14 cards)
private fun getSwordsCards(): List<TarotCard> = listOf(
    TarotCard(
        name = "Ace of Swords", number = "Ace", arcana = TarotArcana.MINOR, suit = TarotSuit.SWORDS,
        emoji = "⚔️", primaryColor = Color(0xFF95A5A6),
        uprightMeaning = "Breakthroughs, new ideas, mental clarity, success.",
        reversedMeaning = "Inner clarity, re-thinking an idea, clouded judgement.",
        uprightKeywords = listOf("Clarity", "Breakthrough", "New Ideas"),
        reversedKeywords = listOf("Confusion", "Chaos", "Lack of Clarity"),
        description = "The Ace of Swords represents mental clarity, breakthroughs, and new ideas.",
        symbolism = "Hand holding sword (mental power), crown (victory), mountains (challenges).",
        inLove = "Mental clarity in relationship, truth revealed.",
        inCareer = "New ideas, breakthrough, mental clarity.",
        inHealth = "Mental clarity, clear diagnosis.",
        element = "Air", astrology = null
    ),
    TarotCard(
        name = "Two of Swords", number = "2", arcana = TarotArcana.MINOR, suit = TarotSuit.SWORDS,
        emoji = "🙈", primaryColor = Color(0xFF95A5A6),
        uprightMeaning = "Difficult decisions, weighing up options, an impasse, avoidance.",
        reversedMeaning = "Indecision, confusion, information overload, stalemate.",
        uprightKeywords = listOf("Difficult Choices", "Stalemate", "Avoidance"),
        reversedKeywords = listOf("Confusion", "Overload", "No Right Choice"),
        description = "The Two of Swords represents difficult decisions and stalemate.",
        symbolism = "Blindfolded figure (avoidance), crossed swords (impasse), water (emotions).",
        inLove = "Difficult decision, avoiding truth, stalemate.",
        inCareer = "Difficult choice, weighing options, impasse.",
        inHealth = "Avoiding health issues, difficult decision.",
        element = "Air", astrology = "Moon in Libra"
    ),
    TarotCard(
        name = "Three of Swords", number = "3", arcana = TarotArcana.MINOR, suit = TarotSuit.SWORDS,
        emoji = "💔", primaryColor = Color(0xFF95A5A6),
        uprightMeaning = "Heartbreak, emotional pain, sorrow, grief, hurt.",
        reversedMeaning = "Negative self-talk, releasing pain, optimism, forgiveness.",
        uprightKeywords = listOf("Heartbreak", "Sorrow", "Pain"),
        reversedKeywords = listOf("Recovery", "Forgiveness", "Moving On"),
        description = "The Three of Swords represents heartbreak, sorrow, and emotional pain.",
        symbolism = "Three swords piercing heart (pain), rain (tears), clouds (sorrow).",
        inLove = "Heartbreak, betrayal, separation, pain.",
        inCareer = "Conflict, betrayal, disappointment.",
        inHealth = "Emotional pain affecting health, grief.",
        element = "Air", astrology = "Saturn in Libra"
    ),
    TarotCard(
        name = "Four of Swords", number = "4", arcana = TarotArcana.MINOR, suit = TarotSuit.SWORDS,
        emoji = "🛌", primaryColor = Color(0xFF95A5A6),
        uprightMeaning = "Rest, relaxation, meditation, contemplation, recuperation.",
        reversedMeaning = "Exhaustion, burn-out, deep contemplation, stagnation.",
        uprightKeywords = listOf("Rest", "Recuperation", "Contemplation"),
        reversedKeywords = listOf("Burnout", "Exhaustion", "Restlessness"),
        description = "The Four of Swords represents rest, recuperation, and contemplation.",
        symbolism = "Figure lying down (rest), three swords on wall (past battles), one below (protection).",
        inLove = "Taking break from relationship, rest needed.",
        inCareer = "Rest needed, recuperation, stepping back.",
        inHealth = "Rest, recovery, healing time needed.",
        element = "Air", astrology = "Jupiter in Libra"
    ),
    TarotCard(
        name = "Five of Swords", number = "5", arcana = TarotArcana.MINOR, suit = TarotSuit.SWORDS,
        emoji = "😏", primaryColor = Color(0xFF95A5A6),
        uprightMeaning = "Conflict, disagreements, competition, defeat, winning at all costs.",
        reversedMeaning = "Reconciliation, making amends, past resentment.",
        uprightKeywords = listOf("Conflict", "Defeat", "Win at All Costs"),
        reversedKeywords = listOf("Reconciliation", "Making Amends", "Past Resentment"),
        description = "The Five of Swords represents conflict, defeat, and winning at all costs.",
        symbolism = "Figure holding swords (victory), defeated figures (losers), stormy sky (conflict).",
        inLove = "Arguments, conflict, hurtful words.",
        inCareer = "Workplace conflict, competition, defeat.",
        inHealth = "Stress from conflict, mental strain.",
        element = "Air", astrology = "Venus in Aquarius"
    ),
    TarotCard(
        name = "Six of Swords", number = "6", arcana = TarotArcana.MINOR, suit = TarotSuit.SWORDS,
        emoji = "⛵", primaryColor = Color(0xFF95A5A6),
        uprightMeaning = "Transition, change, rite of passage, releasing baggage.",
        reversedMeaning = "Personal transition, resistance to change, unfinished business.",
        uprightKeywords = listOf("Transition", "Moving On", "Travel"),
        reversedKeywords = listOf("Resistance", "Unfinished Business", "Stuck"),
        description = "The Six of Swords represents transition, moving on, and releasing baggage.",
        symbolism = "Boat moving to calmer waters (transition), six swords (baggage), figures (moving forward).",
        inLove = "Moving on, transition in relationship, travel.",
        inCareer = "Career transition, moving forward, relocation.",
        inHealth = "Recovery, moving towards better health.",
        element = "Air", astrology = "Mercury in Aquarius"
    ),
    TarotCard(
        name = "Seven of Swords", number = "7", arcana = TarotArcana.MINOR, suit = TarotSuit.SWORDS,
        emoji = "🥷", primaryColor = Color(0xFF95A5A6),
        uprightMeaning = "Betrayal, deception, getting away with something, acting strategically.",
        reversedMeaning = "Imposter syndrome, self-deceit, keeping secrets.",
        uprightKeywords = listOf("Deception", "Betrayal", "Strategy"),
        reversedKeywords = listOf("Imposter Syndrome", "Self-Deceit", "Confession"),
        description = "The Seven of Swords represents deception, betrayal, and strategic action.",
        symbolism = "Figure sneaking away with swords (theft), camp (betrayal), tiptoeing (stealth).",
        inLove = "Deception, betrayal, dishonesty.",
        inCareer = "Office politics, deception, strategy.",
        inHealth = "Avoiding health issues, self-deception.",
        element = "Air", astrology = "Moon in Aquarius"
    ),
    TarotCard(
        name = "Eight of Swords", number = "8", arcana = TarotArcana.MINOR, suit = TarotSuit.SWORDS,
        emoji = "🔒", primaryColor = Color(0xFF95A5A6),
        uprightMeaning = "Negative thoughts, self-imposed restriction, imprisonment, victim mentality.",
        reversedMeaning = "Self-limiting beliefs, inner critic, releasing negative thoughts, open to new perspectives.",
        uprightKeywords = listOf("Restriction", "Trapped", "Victim Mentality"),
        reversedKeywords = listOf("Release", "Freedom", "New Perspective"),
        description = "The Eight of Swords represents feeling trapped and self-imposed restrictions.",
        symbolism = "Blindfolded bound figure (trapped), eight swords (barriers), castle (escape possible).",
        inLove = "Feeling trapped, self-imposed barriers.",
        inCareer = "Feeling stuck, limited options, victim mentality.",
        inHealth = "Mental restrictions, feeling trapped.",
        element = "Air", astrology = "Jupiter in Gemini"
    ),
    TarotCard(
        name = "Nine of Swords", number = "9", arcana = TarotArcana.MINOR, suit = TarotSuit.SWORDS,
        emoji = "😰", primaryColor = Color(0xFF95A5A6),
        uprightMeaning = "Anxiety, worry, fear, depression, nightmares.",
        reversedMeaning = "Inner turmoil, deep-seated fears, secrets, releasing worry.",
        uprightKeywords = listOf("Anxiety", "Worry", "Nightmares"),
        reversedKeywords = listOf("Hope", "Reaching Out", "Despair"),
        description = "The Nine of Swords represents anxiety, worry, and nightmares.",
        symbolism = "Figure in bed (nightmares), nine swords on wall (worries), head in hands (despair).",
        inLove = "Anxiety about relationship, worry, fear.",
        inCareer = "Work anxiety, stress, worry about job.",
        inHealth = "Anxiety, mental health issues, insomnia.",
        element = "Air", astrology = "Mars in Gemini"
    ),
    TarotCard(
        name = "Ten of Swords", number = "10", arcana = TarotArcana.MINOR, suit = TarotSuit.SWORDS,
        emoji = "🗡️", primaryColor = Color(0xFF95A5A6),
        uprightMeaning = "Painful endings, deep wounds, betrayal, loss, crisis.",
        reversedMeaning = "Recovery, regeneration, resisting an inevitable end.",
        uprightKeywords = listOf("Rock Bottom", "Painful Ending", "Betrayal"),
        reversedKeywords = listOf("Recovery", "Regeneration", "Resisting End"),
        description = "The Ten of Swords represents painful endings and hitting rock bottom.",
        symbolism = "Figure with ten swords in back (betrayal), dark sky (despair), sunrise (hope).",
        inLove = "Painful ending, betrayal, rock bottom.",
        inCareer = "Job loss, failure, painful ending.",
        inHealth = "Rock bottom, crisis, but recovery ahead.",
        element = "Air", astrology = "Sun in Gemini"
    ),
    TarotCard(
        name = "Page of Swords", number = "Page", arcana = TarotArcana.MINOR, suit = TarotSuit.SWORDS,
        emoji = "🔍", primaryColor = Color(0xFF95A5A6),
        uprightMeaning = "New ideas, curiosity, thirst for knowledge, new ways of communicating.",
        reversedMeaning = "Self-expression, all talk and no action, haphazard action, haste.",
        uprightKeywords = listOf("Curiosity", "New Ideas", "Communication"),
        reversedKeywords = listOf("Gossip", "All Talk", "Haste"),
        description = "The Page of Swords represents curiosity, new ideas, and communication.",
        symbolism = "Young figure with sword (mental agility), wind (air element), alert stance (vigilance).",
        inLove = "Curious about partner, communication, new perspective.",
        inCareer = "New ideas, learning, communication.",
        inHealth = "Mental agility, learning about health.",
        element = "Air", astrology = null
    ),
    TarotCard(
        name = "Knight of Swords", number = "Knight", arcana = TarotArcana.MINOR, suit = TarotSuit.SWORDS,
        emoji = "⚡", primaryColor = Color(0xFF95A5A6),
        uprightMeaning = "Ambitious, action-oriented, driven to succeed, fast-thinking.",
        reversedMeaning = "Restless, unfocused, impulsive, burn-out.",
        uprightKeywords = listOf("Action", "Ambition", "Fast Thinking"),
        reversedKeywords = listOf("Impulsive", "Reckless", "Unfocused"),
        description = "The Knight of Swords represents action, ambition, and fast thinking.",
        symbolism = "Knight charging forward (action), raised sword (determination), stormy sky (intensity).",
        inLove = "Fast-moving relationship, direct communication.",
        inCareer = "Ambitious pursuit, fast action, determination.",
        inHealth = "High energy, fast recovery, intensity.",
        element = "Air", astrology = null
    ),
    TarotCard(
        name = "Queen of Swords", number = "Queen", arcana = TarotArcana.MINOR, suit = TarotSuit.SWORDS,
        emoji = "👩‍⚖️", primaryColor = Color(0xFF95A5A6),
        uprightMeaning = "Independent, unbiased judgement, clear boundaries, direct communication.",
        reversedMeaning = "Overly-emotional, easily influenced, bitchy, cold-hearted.",
        uprightKeywords = listOf("Independence", "Clear Thinking", "Direct"),
        reversedKeywords = listOf("Coldness", "Cruelty", "Bitterness"),
        description = "The Queen of Swords represents independence, clear thinking, and direct communication.",
        symbolism = "Queen with raised sword (clarity), butterflies (transformation), clouds (air element).",
        inLove = "Independent partner, clear communication, boundaries.",
        inCareer = "Clear thinking, direct communication, leadership.",
        inHealth = "Mental clarity, direct approach to health.",
        element = "Air", astrology = null
    ),
    TarotCard(
        name = "King of Swords", number = "King", arcana = TarotArcana.MINOR, suit = TarotSuit.SWORDS,
        emoji = "⚖️", primaryColor = Color(0xFF95A5A6),
        uprightMeaning = "Mental clarity, intellectual power, authority, truth.",
        reversedMeaning = "Quiet power, inner truth, misuse of power, manipulation.",
        uprightKeywords = listOf("Authority", "Truth", "Clarity"),
        reversedKeywords = listOf("Manipulation", "Cruelty", "Tyranny"),
        description = "The King of Swords represents mental clarity, authority, and truth.",
        symbolism = "King with sword (authority), butterflies (transformation), throne (power).",
        inLove = "Intellectual connection, clear communication, authority.",
        inCareer = "Leadership, authority, clear thinking, truth.",
        inHealth = "Mental clarity, authoritative approach.",
        element = "Air", astrology = null
    )
)

// PENTACLES - Earth Element (14 cards)
private fun getPentaclesCards(): List<TarotCard> = listOf(
    TarotCard(
        name = "Ace of Pentacles", number = "Ace", arcana = TarotArcana.MINOR, suit = TarotSuit.PENTACLES,
        emoji = "💰", primaryColor = Color(0xFF27AE60),
        uprightMeaning = "A new financial or career opportunity, manifestation, abundance.",
        reversedMeaning = "Lost opportunity, lack of planning and foresight.",
        uprightKeywords = listOf("Opportunity", "Prosperity", "New Venture"),
        reversedKeywords = listOf("Lost Opportunity", "Lack of Planning", "Greed"),
        description = "The Ace of Pentacles represents new financial opportunities and abundance.",
        symbolism = "Hand holding pentacle (material gift), garden (growth), mountains (goals).",
        inLove = "New relationship with potential, material security.",
        inCareer = "New job, promotion, financial opportunity.",
        inHealth = "Good health, vitality, new health routine.",
        element = "Earth", astrology = null
    ),
    TarotCard(
        name = "Two of Pentacles", number = "2", arcana = TarotArcana.MINOR, suit = TarotSuit.PENTACLES,
        emoji = "🤹", primaryColor = Color(0xFF27AE60),
        uprightMeaning = "Multiple priorities, time management, prioritisation, adaptability.",
        reversedMeaning = "Over-committed, disorganisation, reprioritisation.",
        uprightKeywords = listOf("Balance", "Adaptability", "Time Management"),
        reversedKeywords = listOf("Imbalance", "Disorganization", "Overwhelmed"),
        description = "The Two of Pentacles represents balance, adaptability, and juggling priorities.",
        symbolism = "Figure juggling pentacles (balance), infinity symbol (continuous flow), waves (ups and downs).",
        inLove = "Balancing relationship with other priorities.",
        inCareer = "Juggling multiple projects, time management.",
        inHealth = "Balance needed, managing multiple health concerns.",
        element = "Earth", astrology = "Jupiter in Capricorn"
    ),
    TarotCard(
        name = "Three of Pentacles", number = "3", arcana = TarotArcana.MINOR, suit = TarotSuit.PENTACLES,
        emoji = "👷", primaryColor = Color(0xFF27AE60),
        uprightMeaning = "Teamwork, collaboration, learning, implementation.",
        reversedMeaning = "Disharmony, misalignment, working alone.",
        uprightKeywords = listOf("Teamwork", "Collaboration", "Building"),
        reversedKeywords = listOf("Lack of Teamwork", "Disorganization", "Conflict"),
        description = "The Three of Pentacles represents teamwork, collaboration, and skilled work.",
        symbolism = "Craftsman with two others (collaboration), church (building), pentacles (work).",
        inLove = "Building relationship together, teamwork.",
        inCareer = "Teamwork, collaboration, recognition for skills.",
        inHealth = "Working with health professionals, teamwork.",
        element = "Earth", astrology = "Mars in Capricorn"
    ),
    TarotCard(
        name = "Four of Pentacles", number = "4", arcana = TarotArcana.MINOR, suit = TarotSuit.PENTACLES,
        emoji = "💎", primaryColor = Color(0xFF27AE60),
        uprightMeaning = "Saving money, security, conservatism, scarcity, control.",
        reversedMeaning = "Over-spending, greed, self-protection.",
        uprightKeywords = listOf("Control", "Security", "Conservation"),
        reversedKeywords = listOf("Greed", "Materialism", "Self-Protection"),
        description = "The Four of Pentacles represents control, security, and conservatism.",
        symbolism = "Figure holding pentacle (control), pentacles on head/feet (grounding), city (material world).",
        inLove = "Holding back emotionally, fear of loss.",
        inCareer = "Job security, holding onto position, control.",
        inHealth = "Holding onto stress, need to let go.",
        element = "Earth", astrology = "Sun in Capricorn"
    ),
    TarotCard(
        name = "Five of Pentacles", number = "5", arcana = TarotArcana.MINOR, suit = TarotSuit.PENTACLES,
        emoji = "🥶", primaryColor = Color(0xFF27AE60),
        uprightMeaning = "Financial loss, poverty, lack mindset, isolation, worry.",
        reversedMeaning = "Recovery from financial loss, spiritual poverty.",
        uprightKeywords = listOf("Financial Loss", "Poverty", "Isolation"),
        reversedKeywords = listOf("Recovery", "Charity", "Improvement"),
        description = "The Five of Pentacles represents financial loss, poverty, and hardship.",
        symbolism = "Two figures in snow (hardship), church window (help available), pentacles (material loss).",
        inLove = "Feeling isolated, lack of support.",
        inCareer = "Job loss, financial hardship, isolation.",
        inHealth = "Health issues, feeling isolated, need support.",
        element = "Earth", astrology = "Mercury in Taurus"
    ),
    TarotCard(
        name = "Six of Pentacles", number = "6", arcana = TarotArcana.MINOR, suit = TarotSuit.PENTACLES,
        emoji = "🤝", primaryColor = Color(0xFF27AE60),
        uprightMeaning = "Giving, receiving, sharing wealth, generosity, charity.",
        reversedMeaning = "Self-care, unpaid debts, one-sided charity.",
        uprightKeywords = listOf("Generosity", "Charity", "Sharing"),
        reversedKeywords = listOf("Debt", "Strings Attached", "Inequality"),
        description = "The Six of Pentacles represents generosity, charity, and sharing wealth.",
        symbolism = "Figure giving coins (generosity), scales (balance), beggars (receiving).",
        inLove = "Giving and receiving equally, generosity.",
        inCareer = "Generosity, mentorship, fair pay.",
        inHealth = "Receiving help, giving support to others.",
        element = "Earth", astrology = "Moon in Taurus"
    ),
    TarotCard(
        name = "Seven of Pentacles", number = "7", arcana = TarotArcana.MINOR, suit = TarotSuit.PENTACLES,
        emoji = "🌱", primaryColor = Color(0xFF27AE60),
        uprightMeaning = "Long-term view, sustainable results, perseverance, investment.",
        reversedMeaning = "Lack of long-term vision, limited success or reward.",
        uprightKeywords = listOf("Patience", "Investment", "Long-term View"),
        reversedKeywords = listOf("Impatience", "Lack of Reward", "Procrastination"),
        description = "The Seven of Pentacles represents patience, investment, and long-term planning.",
        symbolism = "Figure resting on hoe (pause), seven pentacles on bush (investment), contemplation (assessment).",
        inLove = "Assessing relationship, long-term view.",
        inCareer = "Assessing progress, long-term investment, patience.",
        inHealth = "Long-term health plan, patience with results.",
        element = "Earth", astrology = "Saturn in Taurus"
    ),
    TarotCard(
        name = "Eight of Pentacles", number = "8", arcana = TarotArcana.MINOR, suit = TarotSuit.PENTACLES,
        emoji = "🔨", primaryColor = Color(0xFF27AE60),
        uprightMeaning = "Apprenticeship, repetitive tasks, mastery, skill development.",
        reversedMeaning = "Self-development, perfectionism, misdirected activity.",
        uprightKeywords = listOf("Mastery", "Skill", "Dedication"),
        reversedKeywords = listOf("Perfectionism", "Lack of Focus", "Mediocrity"),
        description = "The Eight of Pentacles represents mastery, skill development, and dedication.",
        symbolism = "Craftsman working (dedication), eight pentacles (progress), focus (mastery).",
        inLove = "Working on relationship, dedication, effort.",
        inCareer = "Skill development, dedication, mastery.",
        inHealth = "Dedicated to health routine, skill building.",
        element = "Earth", astrology = "Sun in Virgo"
    ),
    TarotCard(
        name = "Nine of Pentacles", number = "9", arcana = TarotArcana.MINOR, suit = TarotSuit.PENTACLES,
        emoji = "🏡", primaryColor = Color(0xFF27AE60),
        uprightMeaning = "Abundance, luxury, self-sufficiency, financial independence.",
        reversedMeaning = "Self-worth, over-investment in work, hustling.",
        uprightKeywords = listOf("Abundance", "Luxury", "Independence"),
        reversedKeywords = listOf("Overwork", "Hustling", "Self-Worth"),
        description = "The Nine of Pentacles represents abundance, luxury, and self-sufficiency.",
        symbolism = "Woman in garden (abundance), falcon (discipline), grapes (luxury), nine pentacles (wealth).",
        inLove = "Independence in relationship, self-sufficiency.",
        inCareer = "Financial independence, success, luxury.",
        inHealth = "Good health, self-care, abundance.",
        element = "Earth", astrology = "Venus in Virgo"
    ),
    TarotCard(
        name = "Ten of Pentacles", number = "10", arcana = TarotArcana.MINOR, suit = TarotSuit.PENTACLES,
        emoji = "🏰", primaryColor = Color(0xFF27AE60),
        uprightMeaning = "Wealth, financial security, family, long-term success, contribution.",
        reversedMeaning = "The dark side of wealth, financial failure or loss.",
        uprightKeywords = listOf("Wealth", "Legacy", "Family"),
        reversedKeywords = listOf("Financial Failure", "Loneliness", "Loss"),
        description = "The Ten of Pentacles represents wealth, legacy, and long-term success.",
        symbolism = "Multi-generational family (legacy), ten pentacles (wealth), archway (tradition), dogs (loyalty).",
        inLove = "Long-term commitment, family, legacy.",
        inCareer = "Long-term success, wealth, legacy.",
        inHealth = "Family health, long-term wellbeing.",
        element = "Earth", astrology = "Mercury in Virgo"
    ),
    TarotCard(
        name = "Page of Pentacles", number = "Page", arcana = TarotArcana.MINOR, suit = TarotSuit.PENTACLES,
        emoji = "📚", primaryColor = Color(0xFF27AE60),
        uprightMeaning = "Manifestation, financial opportunity, skill development.",
        reversedMeaning = "Lack of progress, procrastination, learn from failure.",
        uprightKeywords = listOf("Opportunity", "Study", "Manifestation"),
        reversedKeywords = listOf("Procrastination", "Lack of Progress", "Distraction"),
        description = "The Page of Pentacles represents opportunity, study, and new ventures.",
        symbolism = "Young figure holding pentacle (opportunity), green landscape (growth), focused gaze (study).",
        inLove = "New relationship with potential, practical approach.",
        inCareer = "New opportunity, learning, apprenticeship.",
        inHealth = "Learning about health, new routine.",
        element = "Earth", astrology = null
    ),
    TarotCard(
        name = "Knight of Pentacles", number = "Knight", arcana = TarotArcana.MINOR, suit = TarotSuit.PENTACLES,
        emoji = "🐢", primaryColor = Color(0xFF27AE60),
        uprightMeaning = "Hard work, productivity, routine, conservatism.",
        reversedMeaning = "Self-discipline, boredom, feeling 'stuck', perfectionism.",
        uprightKeywords = listOf("Efficiency", "Hard Work", "Responsibility"),
        reversedKeywords = listOf("Laziness", "Obsessiveness", "Workaholic"),
        description = "The Knight of Pentacles represents hard work, efficiency, and responsibility.",
        symbolism = "Knight on stationary horse (patience), pentacle (focus), plowed field (hard work).",
        inLove = "Stable relationship, commitment, reliability.",
        inCareer = "Hard work, dedication, routine, efficiency.",
        inHealth = "Consistent health routine, dedication.",
        element = "Earth", astrology = null
    ),
    TarotCard(
        name = "Queen of Pentacles", number = "Queen", arcana = TarotArcana.MINOR, suit = TarotSuit.PENTACLES,
        emoji = "👩‍🌾", primaryColor = Color(0xFF27AE60),
        uprightMeaning = "Nurturing, practical, providing financially, a working parent.",
        reversedMeaning = "Financial independence, self-care, work-home conflict.",
        uprightKeywords = listOf("Nurturing", "Practical", "Abundance"),
        reversedKeywords = listOf("Self-Care", "Work-Life Balance", "Financial Anxiety"),
        description = "The Queen of Pentacles represents nurturing, practicality, and abundance.",
        symbolism = "Queen with pentacle (abundance), rabbit (fertility), flowers (nurturing), throne (stability).",
        inLove = "Nurturing partner, practical love, stability.",
        inCareer = "Practical approach, nurturing leader, abundance.",
        inHealth = "Nurturing health, practical approach.",
        element = "Earth", astrology = null
    ),
    TarotCard(
        name = "King of Pentacles", number = "King", arcana = TarotArcana.MINOR, suit = TarotSuit.PENTACLES,
        emoji = "🤴", primaryColor = Color(0xFF27AE60),
        uprightMeaning = "Wealth, business, leadership, security, discipline, abundance.",
        reversedMeaning = "Financially inept, obsessed with wealth and status, stubborn.",
        uprightKeywords = listOf("Wealth", "Business", "Leadership"),
        reversedKeywords = listOf("Greed", "Materialism", "Financial Instability"),
        description = "The King of Pentacles represents wealth, business success, and leadership.",
        symbolism = "King with pentacle (wealth), grapes (abundance), bull (Taurus), castle (security).",
        inLove = "Stable provider, committed partner, security.",
        inCareer = "Business success, leadership, wealth, security.",
        inHealth = "Stable health, practical approach, abundance.",
        element = "Earth", astrology = null
    )
)
