package com.kreggscode.zodiacfinder.data.model

import androidx.compose.ui.graphics.Color

data class TarotCard(
    val name: String,
    val number: String,
    val arcana: TarotArcana,
    val suit: TarotSuit?,
    val emoji: String,
    val primaryColor: Color,
    val uprightMeaning: String,
    val reversedMeaning: String,
    val uprightKeywords: List<String>,
    val reversedKeywords: List<String>,
    val description: String,
    val symbolism: String,
    val inLove: String,
    val inCareer: String,
    val inHealth: String,
    val element: String?,
    val astrology: String?
)

enum class TarotArcana {
    MAJOR,
    MINOR
}

enum class TarotSuit(val displayName: String, val element: String, val color: Color) {
    WANDS("Wands", "Fire", Color(0xFFE74C3C)),
    CUPS("Cups", "Water", Color(0xFF3498DB)),
    SWORDS("Swords", "Air", Color(0xFF95A5A6)),
    PENTACLES("Pentacles", "Earth", Color(0xFF27AE60))
}
