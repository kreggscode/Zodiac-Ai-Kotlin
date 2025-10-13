package com.kreggscode.zodiacfinder.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Horoscope : Screen("horoscope")
    object Compatibility : Screen("compatibility")
    object Chat : Screen("chat")
    object Encyclopedia : Screen("encyclopedia")
    object ZodiacFinder : Screen("zodiac_finder")
    object ReadingHub : Screen("reading_hub")
}
