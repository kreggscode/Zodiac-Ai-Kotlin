package com.kreggscode.zodiacfinder.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kreggscode.zodiacfinder.data.model.ZodiacSign
import com.kreggscode.zodiacfinder.data.repository.ZodiacRepository
import com.kreggscode.zodiacfinder.ui.components.*
import com.kreggscode.zodiacfinder.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun ReadingHubScreen(
    onNavigateBack: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("🎴 Tarot", "🌙 Moon", "🖐️ Palm", "🎲 Lucky")
    
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
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header with Back Button
            item {
                TopBarWithBack(
                    title = "🔮 Reading Hub",
                    subtitle = "Mystical insights and guidance",
                    onBackClick = onNavigateBack
                )
            }
            
            // STUNNING Hero Section
            item {
                GradientGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    gradientColors = listOf(
                        MysticPurple.copy(alpha = 0.4f),
                        CosmicBlue.copy(alpha = 0.3f),
                        TertiaryLight.copy(alpha = 0.2f)
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("✨", fontSize = 72.sp)
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = "Your Mystical Gateway",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Explore Tarot, Moon Phases, Palm Reading & Lucky Numbers",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(0.7f),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
            
            // Tab Selection with Beautiful Cards
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TabCard(
                            title = "Tarot",
                            icon = "🎴",
                            isSelected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            modifier = Modifier.weight(1f)
                        )
                        TabCard(
                            title = "Moon",
                            icon = "🌙",
                            isSelected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TabCard(
                            title = "Palm",
                            icon = "🖐️",
                            isSelected = selectedTab == 2,
                            onClick = { selectedTab = 2 },
                            modifier = Modifier.weight(1f)
                        )
                        TabCard(
                            title = "Lucky",
                            icon = "🎲",
                            isSelected = selectedTab == 3,
                            onClick = { selectedTab = 3 },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            
            // Content based on selected tab
            item {
                when (selectedTab) {
                    0 -> TarotReadingContent()
                    1 -> MoonPhaseContent()
                    2 -> PalmReadingContent()
                    3 -> LuckyNumbersContent()
                }
            }
        }
    }
}

@Composable
private fun TabCard(
    title: String,
    icon: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GradientGlassCard(
        modifier = modifier
            .height(100.dp)
            .clickable(onClick = onClick),
        gradientColors = if (isSelected) {
            listOf(
                MysticPurple.copy(alpha = 0.4f),
                CosmicBlue.copy(alpha = 0.3f)
            )
        } else {
            listOf(
                MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)
            )
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = icon,
                fontSize = 36.sp
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(0.7f)
            )
        }
    }
}

@Composable
private fun TarotReadingContent() {
    var question by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var reading by remember { mutableStateOf<com.kreggscode.zodiacfinder.data.model.TarotReading?>(null) }
    val repository = remember { ZodiacRepository() }
    val coroutineScope = rememberCoroutineScope()
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(
                        text = "Ask the Tarot",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = question,
                        onValueChange = { question = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("What question do you seek guidance on?") },
                        minLines = 3,
                        maxLines = 5
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    NeumorphicButton(
                        onClick = {
                            coroutineScope.launch {
                                isLoading = true
                                repository.getTarotReading(question).fold(
                                    onSuccess = { reading = it },
                                    onFailure = { /* Handle error */ }
                                )
                                isLoading = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = question.isNotBlank() && !isLoading
                    ) {
                        Text(if (isLoading) "Drawing Cards..." else "🃏 Draw Cards")
                    }
                }
        }
        
        if (isLoading) {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CustomLoadingIndicator()
                }
            }
        }
        
        reading?.let { tarotReading ->
            Text(
                text = "Your Reading",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            tarotReading.cards.forEach { card ->
                GradientGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    gradientColors = listOf(
                        MysticPurple.copy(alpha = 0.3f),
                        CosmicBlue.copy(alpha = 0.2f)
                    )
                ) {
                    Column {
                        Text(
                            text = card.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = card.position,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        Text(
                            text = card.meaning,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            lineHeight = 20.sp
                        )
                    }
                }
            }
            
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(
                        text = "💫 Interpretation",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = tarotReading.interpretation,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        lineHeight = 20.sp
                    )
                }
            }
            
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(
                        text = "✨ Advice",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = tarotReading.advice,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun MoonPhaseContent() {
    var isLoading by remember { mutableStateOf(false) }
    var moonPhase by remember { mutableStateOf<com.kreggscode.zodiacfinder.data.model.MoonPhaseData?>(null) }
    val repository = remember { ZodiacRepository() }
    
    LaunchedEffect(Unit) {
        isLoading = true
        repository.getMoonPhaseInsights().fold(
            onSuccess = { moonPhase = it },
            onFailure = { /* Handle error */ }
        )
        isLoading = false
    }
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (isLoading) {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CustomLoadingIndicator()
                }
            }
        }
        
        moonPhase?.let { phase ->
            GradientGlassCard(
                modifier = Modifier.fillMaxWidth(),
                gradientColors = listOf(
                    SecondaryLight.copy(alpha = 0.4f),
                    CosmicBlue.copy(alpha = 0.2f)
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "🌙",
                        fontSize = 64.sp
                    )
                    Text(
                        text = phase.phase,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = phase.date,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${(phase.illumination * 100).toInt()}% Illuminated",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(
                        text = "🌟 Insights",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = phase.insights,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        lineHeight = 20.sp
                    )
                }
            }
            
            if (phase.recommendations.isNotEmpty()) {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        Text(
                            text = "💡 Recommendations",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        phase.recommendations.forEach { recommendation ->
                            Row(modifier = Modifier.padding(vertical = 4.dp)) {
                                Text(text = "• ", fontSize = 14.sp)
                                Text(
                                    text = recommendation,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PalmReadingContent() {
    var description by remember { mutableStateOf("") }
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(
                        text = "🖐️ Palm Reading",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Describe the lines on your palm (life line, heart line, head line, fate line) and I'll provide an interpretation.",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Describe your palm lines...") },
                        minLines = 5,
                        maxLines = 8
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    NeumorphicButton(
                        onClick = { /* Implement palm reading */ },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = description.isNotBlank()
                    ) {
                        Text("🔮 Get Reading")
                    }
                }
            }
        
        GradientGlassCard(
                modifier = Modifier.fillMaxWidth(),
                gradientColors = listOf(
                    MysticPurple.copy(alpha = 0.3f),
                    TertiaryLight.copy(alpha = 0.2f)
                )
            ) {
                Column {
                    Text(
                        text = "ℹ️ Palm Reading Guide",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "• Life Line: Curves around thumb, represents vitality\n" +
                                "• Heart Line: Horizontal line at top, represents emotions\n" +
                                "• Head Line: Middle horizontal line, represents intellect\n" +
                                "• Fate Line: Vertical line in center, represents destiny",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        lineHeight = 18.sp
                    )
                }
            }
    }
}

@Composable
private fun LuckyNumbersContent() {
    var selectedSign by remember { mutableStateOf<ZodiacSign?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var luckyNumbers by remember { mutableStateOf<com.kreggscode.zodiacfinder.data.model.LuckyNumbers?>(null) }
    val repository = remember { ZodiacRepository() }
    val coroutineScope = rememberCoroutineScope()
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
            Text(
                text = "Select Your Sign",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        
        androidx.compose.foundation.lazy.LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(ZodiacSign.values().size) { index ->
                    val sign = ZodiacSign.values()[index]
                    ZodiacSignCard(
                        sign = sign,
                        onClick = {
                            selectedSign = sign
                            coroutineScope.launch {
                                isLoading = true
                                repository.getLuckyNumbers(sign).fold(
                                    onSuccess = { luckyNumbers = it },
                                    onFailure = { /* Handle error */ }
                                )
                                isLoading = false
                            }
                        },
                        modifier = Modifier.width(100.dp).height(120.dp),
                        isSelected = sign == selectedSign
                    )
                }
            }
        
        if (isLoading) {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CustomLoadingIndicator()
                    }
                }
        }
        
        luckyNumbers?.let { numbers ->
            GradientGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    gradientColors = listOf(
                        LuckyGold.copy(alpha = 0.4f),
                        LuckyGold.copy(alpha = 0.2f)
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "🍀",
                            fontSize = 48.sp
                        )
                        Text(
                            text = "Your Lucky Numbers",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.padding(vertical = 12.dp)
                        ) {
                            numbers.numbers.forEach { number ->
                                GlassCard(
                                    modifier = Modifier.size(60.dp)
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = number.toString(),
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                        Text(
                            text = "🔢 Numerological Significance",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = numbers.explanation,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            lineHeight = 20.sp
                        )
                    }
                }
        }
    }
}
