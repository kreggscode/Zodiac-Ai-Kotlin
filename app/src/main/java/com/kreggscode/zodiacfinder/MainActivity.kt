package com.kreggscode.zodiacfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kreggscode.zodiacfinder.data.model.ZodiacSign
import com.kreggscode.zodiacfinder.navigation.Screen
import com.kreggscode.zodiacfinder.navigation.bottomNavItems
import com.kreggscode.zodiacfinder.ui.components.FloatingBottomBar
import com.kreggscode.zodiacfinder.ui.screens.*
import com.kreggscode.zodiacfinder.ui.theme.ZodiacAIFinderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        // Make system bars transparent
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        window.navigationBarColor = android.graphics.Color.TRANSPARENT
        
        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }
            
            ZodiacAIFinderTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        isDarkTheme = isDarkTheme,
                        onThemeToggle = { isDarkTheme = !isDarkTheme }
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Content with proper insets
        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            NavigationGraph(
                navController = navController,
                isDarkTheme = isDarkTheme,
                onThemeToggle = onThemeToggle
            )
        }
        
        // Floating bottom navigation
        if (currentRoute in bottomNavItems.map { it.route }) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
                FloatingBottomBar(
                    items = bottomNavItems,
                    currentRoute = currentRoute,
                    onItemClick = { route ->
                        if (route != currentRoute) {
                            navController.navigate(route) {
                                popUpTo(Screen.Home.route)
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun NavigationGraph(
    navController: NavHostController,
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToZodiacFinder = {
                    navController.navigate(Screen.ZodiacFinder.route)
                },
                onNavigateToHoroscope = {
                    navController.navigate(Screen.Horoscope.route)
                },
                onNavigateToCompatibility = {
                    navController.navigate(Screen.Compatibility.route)
                },
                onNavigateToReadingHub = {
                    navController.navigate(Screen.ReadingHub.route)
                },
                isDarkTheme = isDarkTheme,
                onThemeToggle = onThemeToggle
            )
        }
        
        composable(Screen.Horoscope.route) {
            HoroscopeScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Compatibility.route) {
            CompatibilityScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Chat.route) {
            ChatScreenNew(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Encyclopedia.route) {
            EncyclopediaScreenNew(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.ZodiacFinder.route) {
            ZodiacFinderScreenNew(
                onNavigateBack = { navController.popBackStack() },
                onSignFound = { sign ->
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.ReadingHub.route) {
            ReadingHubScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
