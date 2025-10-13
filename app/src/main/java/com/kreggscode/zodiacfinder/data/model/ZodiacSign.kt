package com.kreggscode.zodiacfinder.data.model

enum class ZodiacSign(
    val displayName: String,
    val symbol: String,
    val element: ZodiacElement,
    val dateRange: String,
    val emoji: String
) {
    ARIES("Aries", "♈", ZodiacElement.FIRE, "Mar 21 - Apr 19", "♈"),
    TAURUS("Taurus", "♉", ZodiacElement.EARTH, "Apr 20 - May 20", "♉"),
    GEMINI("Gemini", "♊", ZodiacElement.AIR, "May 21 - Jun 20", "♊"),
    CANCER("Cancer", "♋", ZodiacElement.WATER, "Jun 21 - Jul 22", "♋"),
    LEO("Leo", "♌", ZodiacElement.FIRE, "Jul 23 - Aug 22", "♌"),
    VIRGO("Virgo", "♍", ZodiacElement.EARTH, "Aug 23 - Sep 22", "♍"),
    LIBRA("Libra", "♎", ZodiacElement.AIR, "Sep 23 - Oct 22", "♎"),
    SCORPIO("Scorpio", "♏", ZodiacElement.WATER, "Oct 23 - Nov 21", "♏"),
    SAGITTARIUS("Sagittarius", "♐", ZodiacElement.FIRE, "Nov 22 - Dec 21", "♐"),
    CAPRICORN("Capricorn", "♑", ZodiacElement.EARTH, "Dec 22 - Jan 19", "♑"),
    AQUARIUS("Aquarius", "♒", ZodiacElement.AIR, "Jan 20 - Feb 18", "♒"),
    PISCES("Pisces", "♓", ZodiacElement.WATER, "Feb 19 - Mar 20", "♓");

    companion object {
        fun fromDate(month: Int, day: Int): ZodiacSign {
            return when {
                (month == 3 && day >= 21) || (month == 4 && day <= 19) -> ARIES
                (month == 4 && day >= 20) || (month == 5 && day <= 20) -> TAURUS
                (month == 5 && day >= 21) || (month == 6 && day <= 20) -> GEMINI
                (month == 6 && day >= 21) || (month == 7 && day <= 22) -> CANCER
                (month == 7 && day >= 23) || (month == 8 && day <= 22) -> LEO
                (month == 8 && day >= 23) || (month == 9 && day <= 22) -> VIRGO
                (month == 9 && day >= 23) || (month == 10 && day <= 22) -> LIBRA
                (month == 10 && day >= 23) || (month == 11 && day <= 21) -> SCORPIO
                (month == 11 && day >= 22) || (month == 12 && day <= 21) -> SAGITTARIUS
                (month == 12 && day >= 22) || (month == 1 && day <= 19) -> CAPRICORN
                (month == 1 && day >= 20) || (month == 2 && day <= 18) -> AQUARIUS
                else -> PISCES
            }
        }
    }
}

enum class ZodiacElement(val displayName: String, val emoji: String) {
    FIRE("Fire", "🔥"),
    EARTH("Earth", "🌍"),
    AIR("Air", "💨"),
    WATER("Water", "💧")
}

data class ZodiacProfile(
    val sign: ZodiacSign,
    val traits: List<String> = emptyList(),
    val strengths: List<String> = emptyList(),
    val weaknesses: List<String> = emptyList(),
    val compatibility: List<ZodiacSign> = emptyList(),
    val description: String = ""
)
