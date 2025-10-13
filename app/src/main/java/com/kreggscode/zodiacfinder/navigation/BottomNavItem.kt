package com.kreggscode.zodiacfinder.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

val bottomNavItems = listOf(
    BottomNavItem(
        route = Screen.Home.route,
        icon = Icons.Default.Home,
        label = "Home"
    ),
    BottomNavItem(
        route = Screen.Horoscope.route,
        icon = Icons.Default.Star,
        label = "Horoscope"
    ),
    BottomNavItem(
        route = Screen.Compatibility.route,
        icon = Icons.Default.Favorite,
        label = "Match"
    ),
    BottomNavItem(
        route = Screen.Chat.route,
        icon = Icons.Default.Chat,
        label = "AI Chat"
    ),
    BottomNavItem(
        route = Screen.Encyclopedia.route,
        icon = Icons.Default.Book,
        label = "Info"
    )
)
