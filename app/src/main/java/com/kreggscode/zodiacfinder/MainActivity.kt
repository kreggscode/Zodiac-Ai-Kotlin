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
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                },
                onNavigateToTarot = {
                    navController.navigate(Screen.Tarot.route)
                },
                onNavigateToMoonPhase = {
                    navController.navigate(Screen.MoonPhase.route)
                },
                onNavigateToGemstones = {
                    navController.navigate(Screen.GemstoneEncyclopedia.route)
                },
                onNavigateToBirthChart = {
                    navController.navigate(Screen.BirthChart.route)
                },
                onNavigateToElements = {
                    navController.navigate(Screen.ElementsEncyclopedia.route)
                },
                onNavigateToCrystals = {
                    navController.navigate(Screen.CrystalsEncyclopedia.route)
                },
                onNavigateToPalmReading = {
                    navController.navigate(Screen.PalmEncyclopedia.route)
                },
                onNavigateToAnalyzePalm = {
                    navController.navigate(Screen.AnalyzePalm.route)
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
                onNavigateBack = { navController.popBackStack() },
                onNavigateToTarot = { navController.navigate(Screen.TarotEncyclopedia.route) },
                onNavigateToCrystals = { navController.navigate(Screen.CrystalsEncyclopedia.route) },
                onNavigateToElements = { navController.navigate(Screen.ElementsEncyclopedia.route) },
                onNavigateToGemstones = { navController.navigate(Screen.GemstoneEncyclopedia.route) }
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
        
        composable(Screen.Profile.route) {
            ProfileScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Tarot.route) {
            TarotScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.MoonPhase.route) {
            MoonPhaseScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.GemstoneEncyclopedia.route) {
            GemstoneEncyclopediaScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.BirthChart.route) {
            BirthChartAnalysisScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.ElementsEncyclopedia.route) {
            ElementsEncyclopediaScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.TarotEncyclopedia.route) {
            TarotEncyclopediaScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.CrystalsEncyclopedia.route) {
            CrystalsEncyclopediaScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.PalmEncyclopedia.route) {
            // Placeholder - Palm Reading Encyclopedia coming soon
            ReadingHubScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.AnalyzePalm.route) {
            AnalyzePalmScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
