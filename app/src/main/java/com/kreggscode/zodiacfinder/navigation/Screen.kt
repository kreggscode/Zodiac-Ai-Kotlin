package com.kreggscode.zodiacfinder.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Horoscope : Screen("horoscope")
    object Compatibility : Screen("compatibility")
    object Chat : Screen("chat")
    object Encyclopedia : Screen("encyclopedia")
    object ZodiacFinder : Screen("zodiac_finder")
    object ReadingHub : Screen("reading_hub")
    object Profile : Screen("profile")
    object Tarot : Screen("tarot")
    object MoonPhase : Screen("moon_phase")
    object GemstoneEncyclopedia : Screen("gemstone_encyclopedia")
    object BirthChart : Screen("birth_chart")
    object ElementsEncyclopedia : Screen("elements_encyclopedia")
    object CrystalsEncyclopedia : Screen("crystals_encyclopedia")
    object TarotEncyclopedia : Screen("tarot_encyclopedia")
    object PalmEncyclopedia : Screen("palm_encyclopedia")
    object AnalyzePalm : Screen("analyze_palm")
}
