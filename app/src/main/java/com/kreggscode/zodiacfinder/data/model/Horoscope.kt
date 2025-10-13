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
    val cards: List<TarotCardData> = emptyList(),
    val interpretation: String = "",
    val advice: String = ""
)

data class TarotCardData(
    val name: String,
    val position: String,
    val meaning: String
)

data class MoonPhaseData(
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

// Moon Phase Enum
enum class MoonPhase(
    val displayName: String,
    val emoji: String,
    val description: String,
    val energy: String,
    val recommendations: String
) {
    NEW_MOON("New Moon", "🌑", "The New Moon marks new beginnings and fresh starts.", "New beginnings, fresh energy", "Set intentions, start new projects, meditate on goals"),
    WAXING_CRESCENT("Waxing Crescent", "🌒", "The Waxing Crescent is about taking action.", "Growth, momentum, courage", "Take small steps toward goals, stay committed"),
    FIRST_QUARTER("First Quarter", "🌓", "The First Quarter is a time of decision-making.", "Decision-making, perseverance", "Make important decisions, push through challenges"),
    WAXING_GIBBOUS("Waxing Gibbous", "🌔", "The Waxing Gibbous is about refinement.", "Refinement, patience", "Refine your approach, pay attention to details"),
    FULL_MOON("Full Moon", "🌕", "The Full Moon is the peak of the cycle.", "Culmination, celebration", "Celebrate achievements, express gratitude"),
    WANING_GIBBOUS("Waning Gibbous", "🌖", "The Waning Gibbous is about gratitude.", "Gratitude, sharing", "Practice gratitude, share knowledge, help others"),
    LAST_QUARTER("Last Quarter", "🌗", "The Last Quarter is a time of release.", "Release, forgiveness", "Forgive yourself and others, release old patterns"),
    WANING_CRESCENT("Waning Crescent", "🌘", "The Waning Crescent is about rest.", "Rest, reflection, spirituality", "Rest and restore, meditate deeply");
    
    companion object {
        fun current(): MoonPhase {
            val day = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_MONTH)
            val phase = (day % 8)
            return values()[phase]
        }
        fun getUpcoming(): List<MoonPhase> {
            val current = current()
            val currentIndex = current.ordinal
            return (1..3).map { offset -> values()[(currentIndex + offset) % values().size] }
        }
    }
}
