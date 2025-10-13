package com.kreggscode.zodiacfinder.data.model

data class DailyHoroscope(
    val sign: ZodiacSign,
    val date: String,
    val love: String = "",
    val career: String = "",
    val finance: String = "",
    val health: String = "",
    val luck: String = "",
    val luckyNumber: Int = 0,
    val luckyColor: String = "",
    val overall: String = ""
)

data class CompatibilityResult(
    val sign1: ZodiacSign,
    val sign2: ZodiacSign,
    val loveScore: Int = 0,
    val marriageScore: Int = 0,
    val careerScore: Int = 0,
    val friendshipScore: Int = 0,
    val overallScore: Int = 0,
    val insights: String = "",
    val suggestions: List<String> = emptyList()
)

data class TarotReading(
    val cards: List<TarotCard> = emptyList(),
    val interpretation: String = "",
    val advice: String = ""
)

data class TarotCard(
    val name: String,
    val position: String,
    val meaning: String
)

data class MoonPhase(
    val phase: String,
    val date: String,
    val illumination: Float,
    val insights: String = "",
    val recommendations: List<String> = emptyList()
)

data class PalmReading(
    val lifeLineAnalysis: String = "",
    val heartLineAnalysis: String = "",
    val headLineAnalysis: String = "",
    val fateLineAnalysis: String = "",
    val overallInterpretation: String = ""
)

data class LuckyNumbers(
    val numbers: List<Int> = emptyList(),
    val explanation: String = ""
)
