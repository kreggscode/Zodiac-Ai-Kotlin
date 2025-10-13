package com.kreggscode.zodiacfinder.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kreggscode.zodiacfinder.data.model.ZodiacSign
import com.kreggscode.zodiacfinder.data.repository.ZodiacRepository
import com.kreggscode.zodiacfinder.ui.components.*
import com.kreggscode.zodiacfinder.ui.theme.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class BirthChartData(
    val name: String,
    val dateOfBirth: LocalDate,
    val timeOfBirth: LocalTime?,
    val placeOfBirth: String?,
    val zodiacSign: ZodiacSign,
    val moonSign: ZodiacSign,
    val risingSign: ZodiacSign,
    val luckyNumbers: List<Int>,
    val moonPhase: String,
    val luckyGemstone: String,
    val luckyColor: String,
    val analysis: String = "",
    val recommendations: List<String> = emptyList(),
    // NEW SECTIONS
    val loveAndRelationships: String = "",
    val careerAndFinance: String = "",
    val healthAndWellness: String = "",
    val compatibilityInsights: String = "",
    val futureGuidance: String = ""
)

// Helper function to calculate Moon Sign (simplified - based on day of month)
fun calculateMoonSign(date: LocalDate): ZodiacSign {
    val dayOfMonth = date.dayOfMonth
    val monthValue = date.monthValue
    // Simplified moon sign calculation (moon changes sign every ~2.5 days)
    val index = ((monthValue + dayOfMonth) % 12)
    return ZodiacSign.values()[index]
}

// Helper function to calculate Rising Sign (simplified - based on birth time)
fun calculateRisingSign(date: LocalDate, time: LocalTime?): ZodiacSign {
    val hour = time?.hour ?: 12
    val monthValue = date.monthValue
    val index = ((monthValue + (hour / 2)) % 12)
    return ZodiacSign.values()[index]
}

// Helper function to calculate Lucky Numbers
fun calculateLuckyNumbers(date: LocalDate, sign: ZodiacSign): List<Int> {
    val dayOfMonth = date.dayOfMonth
    val monthValue = date.monthValue
    val yearLastTwo = date.year % 100
    return listOf(
        dayOfMonth,
        monthValue,
        (dayOfMonth + monthValue) % 50,
        sign.ordinal + 1,
        (yearLastTwo % 50)
    ).sorted()
}

// Helper function to calculate Moon Phase
fun calculateMoonPhase(date: LocalDate): String {
    val dayOfMonth = date.dayOfMonth
    return when (dayOfMonth % 8) {
        0 -> "🌑 New Moon"
        1 -> "🌒 Waxing Crescent"
        2 -> "🌓 First Quarter"
        3 -> "🌔 Waxing Gibbous"
        4 -> "🌕 Full Moon"
        5 -> "🌖 Waning Gibbous"
        6 -> "🌗 Last Quarter"
        else -> "🌘 Waning Crescent"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthChartAnalysisScreen(
    onNavigateBack: () -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedTime by remember { mutableStateOf<LocalTime?>(null) }
    var placeOfBirth by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Not Specified") }
    
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var isGenerating by remember { mutableStateOf(false) }
    var birthChartData by remember { mutableStateOf<BirthChartData?>(null) }
    
    val repository = remember { ZodiacRepository() }
    val coroutineScope = rememberCoroutineScope()
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )
    
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
            // Header
            item {
                TopBarWithBack(
                    title = "🔮 Birth Chart Analysis",
                    subtitle = "Complete astrological profile",
                    onBackClick = onNavigateBack
                )
            }
            
            if (birthChartData == null) {
                // Hero Section
                item {
                    GradientGlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        gradientColors = listOf(
                            MysticPurple.copy(alpha = 0.3f),
                            CosmicBlue.copy(alpha = 0.2f)
                        )
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("✨", fontSize = 64.sp)
                            Spacer(Modifier.height(12.dp))
                            Text(
                                text = "Unlock Your Cosmic Blueprint",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "Get a comprehensive AI-powered analysis of your birth chart with detailed insights into love, career, health, and your future path",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(0.7f),
                                textAlign = TextAlign.Center,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
                
                // Input Form
                item {
                    GradientGlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        gradientColors = listOf(
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
                        )
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("📝", fontSize = 32.sp)
                                Spacer(Modifier.width(12.dp))
                                Text(
                                    text = "Your Birth Details",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            
                            HorizontalDivider(color = MaterialTheme.colorScheme.primary.copy(0.2f))
                            
                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { Text("Full Name *") },
                                modifier = Modifier.fillMaxWidth(),
                                leadingIcon = {
                                    Icon(Icons.Default.Person, "Name")
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(0.3f)
                                )
                            )
                            
                            GradientGlassCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showDatePicker = true },
                                gradientColors = listOf(
                                    MysticPurple.copy(alpha = 0.2f),
                                    MysticPurple.copy(alpha = 0.1f)
                                )
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(56.dp)
                                            .clip(CircleShape)
                                            .background(
                                                brush = Brush.linearGradient(
                                                    colors = listOf(MysticPurple, CosmicBlue)
                                                )
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.CalendarToday,
                                            "Calendar",
                                            tint = Color.White,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                    Spacer(Modifier.width(16.dp))
                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = "Date of Birth",
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = " *",
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.error
                                            )
                                        }
                                        Spacer(Modifier.height(4.dp))
                                        Text(
                                            text = selectedDate.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")),
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                            
                            GradientGlassCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showTimePicker = true },
                                gradientColors = listOf(
                                    CosmicBlue.copy(alpha = 0.2f),
                                    CosmicBlue.copy(alpha = 0.1f)
                                )
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(56.dp)
                                            .clip(CircleShape)
                                            .background(
                                                brush = Brush.linearGradient(
                                                    colors = listOf(CosmicBlue, SecondaryLight)
                                                )
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.Schedule,
                                            "Time",
                                            tint = Color.White,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                    Spacer(Modifier.width(16.dp))
                                    Column {
                                        Text(
                                            text = "Time of Birth (Optional)",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(Modifier.height(4.dp))
                                        Text(
                                            text = selectedTime?.format(DateTimeFormatter.ofPattern("hh:mm a")) ?: "Tap to select for accurate Moon & Rising",
                                            fontSize = 14.sp,
                                            fontWeight = if (selectedTime != null) FontWeight.SemiBold else FontWeight.Normal,
                                            color = if (selectedTime != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(0.6f)
                                        )
                                    }
                                }
                            }
                            
                            OutlinedTextField(
                                value = placeOfBirth,
                                onValueChange = { placeOfBirth = it },
                                label = { Text("Place of Birth (Optional)") },
                                modifier = Modifier.fillMaxWidth(),
                                leadingIcon = {
                                    Icon(Icons.Default.LocationOn, "Location")
                                },
                                placeholder = { Text("City, Country") },
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(0.3f)
                                )
                            )
                            
                            // Gender Selection
                            Column {
                                Text(
                                    text = "Gender (Optional)",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    listOf("Male", "Female", "Other", "Not Specified").forEach { option ->
                                        FilterChip(
                                            selected = gender == option,
                                            onClick = { gender = option },
                                            label = { Text(option, fontSize = 12.sp) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                
                // IMMEDIATE CALCULATIONS - Show before AI button
                if (name.isNotBlank()) {
                    val currentSign = ZodiacSign.fromDate(selectedDate.monthValue, selectedDate.dayOfMonth)
                    val currentMoonSign = calculateMoonSign(selectedDate)
                    val currentRisingSign = calculateRisingSign(selectedDate, selectedTime)
                    val currentLuckyNumbers = calculateLuckyNumbers(selectedDate, currentSign)
                    val currentMoonPhase = calculateMoonPhase(selectedDate)
                    val currentGemstone = when(currentSign) {
                        ZodiacSign.ARIES -> "Diamond"
                        ZodiacSign.TAURUS -> "Emerald"
                        ZodiacSign.GEMINI -> "Pearl"
                        ZodiacSign.CANCER -> "Ruby"
                        ZodiacSign.LEO -> "Peridot"
                        ZodiacSign.VIRGO -> "Sapphire"
                        ZodiacSign.LIBRA -> "Opal"
                        ZodiacSign.SCORPIO -> "Topaz"
                        ZodiacSign.SAGITTARIUS -> "Turquoise"
                        ZodiacSign.CAPRICORN -> "Garnet"
                        ZodiacSign.AQUARIUS -> "Amethyst"
                        ZodiacSign.PISCES -> "Aquamarine"
                    }
                    val currentColor = when(currentSign) {
                        ZodiacSign.ARIES -> "Red"
                        ZodiacSign.TAURUS -> "Green"
                        ZodiacSign.GEMINI -> "Yellow"
                        ZodiacSign.CANCER -> "Silver"
                        ZodiacSign.LEO -> "Gold"
                        ZodiacSign.VIRGO -> "Navy Blue"
                        ZodiacSign.LIBRA -> "Pink"
                        ZodiacSign.SCORPIO -> "Maroon"
                        ZodiacSign.SAGITTARIUS -> "Purple"
                        ZodiacSign.CAPRICORN -> "Brown"
                        ZodiacSign.AQUARIUS -> "Turquoise"
                        ZodiacSign.PISCES -> "Sea Green"
                    }
                    
                    // Sun, Moon, Rising Signs - IMMEDIATE
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            GradientGlassCard(
                                modifier = Modifier.weight(1f),
                                gradientColors = listOf(
                                    LuckyGold.copy(alpha = 0.3f),
                                    LuckyGold.copy(alpha = 0.1f)
                                )
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("☀️", fontSize = 32.sp)
                                    Text("Sun Sign", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(0.7f))
                                    Text(currentSign.displayName, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            
                            GradientGlassCard(
                                modifier = Modifier.weight(1f),
                                gradientColors = listOf(
                                    SecondaryLight.copy(alpha = 0.3f),
                                    SecondaryLight.copy(alpha = 0.1f)
                                )
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("🌙", fontSize = 32.sp)
                                    Text("Moon Sign", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(0.7f))
                                    Text(currentMoonSign.displayName, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            
                            GradientGlassCard(
                                modifier = Modifier.weight(1f),
                                gradientColors = listOf(
                                    CosmicBlue.copy(alpha = 0.3f),
                                    CosmicBlue.copy(alpha = 0.1f)
                                )
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("⬆️", fontSize = 32.sp)
                                    Text("Rising", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(0.7f))
                                    Text(currentRisingSign.displayName, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                    
                    // Lucky Numbers & Moon Phase
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            GradientGlassCard(
                                modifier = Modifier.weight(1f),
                                gradientColors = listOf(
                                    MysticPurple.copy(alpha = 0.3f),
                                    TertiaryLight.copy(alpha = 0.2f)
                                )
                            ) {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("🎲", fontSize = 24.sp)
                                        Spacer(Modifier.width(8.dp))
                                        Text("Lucky Numbers", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Spacer(Modifier.height(8.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        currentLuckyNumbers.forEach { num ->
                                            Box(
                                                modifier = Modifier
                                                    .size(32.dp)
                                                    .background(MysticPurple.copy(0.3f), CircleShape),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(num.toString(), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }
                            
                            GradientGlassCard(
                                modifier = Modifier.weight(1f),
                                gradientColors = listOf(
                                    CosmicBlue.copy(alpha = 0.3f),
                                    SecondaryLight.copy(alpha = 0.2f)
                                )
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Moon Phase", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(0.7f))
                                    Spacer(Modifier.height(4.dp))
                                    Text(currentMoonPhase, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                    
                    // Gems & Colors
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            GradientGlassCard(
                                modifier = Modifier.weight(1f),
                                gradientColors = listOf(
                                    LuckyGold.copy(alpha = 0.3f),
                                    FireElement.copy(alpha = 0.2f)
                                )
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("💎", fontSize = 32.sp)
                                    Spacer(Modifier.width(8.dp))
                                    Column {
                                        Text("Lucky Gem", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(0.7f))
                                        Text(currentGemstone, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                            
                            GradientGlassCard(
                                modifier = Modifier.weight(1f),
                                gradientColors = listOf(
                                    EarthElement.copy(alpha = 0.3f),
                                    WaterElement.copy(alpha = 0.2f)
                                )
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("🎨", fontSize = 32.sp)
                                    Spacer(Modifier.width(8.dp))
                                    Column {
                                        Text("Lucky Color", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(0.7f))
                                        Text(currentColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
                
                // Generate Button - STUNNING
                item {
                    Button(
                        onClick = {
                            if (name.isNotBlank()) {
                                isGenerating = true
                                val sign = ZodiacSign.fromDate(
                                    selectedDate.monthValue,
                                    selectedDate.dayOfMonth
                                )
                                // Generate real AI analysis
                                coroutineScope.launch {
                                    val dateStr = selectedDate.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"))
                                    val timeStr = selectedTime?.format(DateTimeFormatter.ofPattern("hh:mm a")) ?: "Time not provided"
                                    val placeStr = placeOfBirth.ifBlank { "Location not provided" }
                                    
                                    val moonSign = calculateMoonSign(selectedDate)
                                    val risingSign = calculateRisingSign(selectedDate, selectedTime)
                                    val luckyNums = calculateLuckyNumbers(selectedDate, sign)
                                    val moonPh = calculateMoonPhase(selectedDate)
                                    val gemstone = when(sign) {
                                        ZodiacSign.ARIES -> "Diamond"
                                        ZodiacSign.TAURUS -> "Emerald"
                                        ZodiacSign.GEMINI -> "Pearl"
                                        ZodiacSign.CANCER -> "Ruby"
                                        ZodiacSign.LEO -> "Peridot"
                                        ZodiacSign.VIRGO -> "Sapphire"
                                        ZodiacSign.LIBRA -> "Opal"
                                        ZodiacSign.SCORPIO -> "Topaz"
                                        ZodiacSign.SAGITTARIUS -> "Turquoise"
                                        ZodiacSign.CAPRICORN -> "Garnet"
                                        ZodiacSign.AQUARIUS -> "Amethyst"
                                        ZodiacSign.PISCES -> "Aquamarine"
                                    }
                                    val luckyCol = when(sign) {
                                        ZodiacSign.ARIES -> "Red"
                                        ZodiacSign.TAURUS -> "Green"
                                        ZodiacSign.GEMINI -> "Yellow"
                                        ZodiacSign.CANCER -> "Silver"
                                        ZodiacSign.LEO -> "Gold"
                                        ZodiacSign.VIRGO -> "Navy Blue"
                                        ZodiacSign.LIBRA -> "Pink"
                                        ZodiacSign.SCORPIO -> "Maroon"
                                        ZodiacSign.SAGITTARIUS -> "Purple"
                                        ZodiacSign.CAPRICORN -> "Brown"
                                        ZodiacSign.AQUARIUS -> "Turquoise"
                                        ZodiacSign.PISCES -> "Sea Green"
                                    }
                                    
                                    repository.generateBirthChartAnalysis(
                                        dateOfBirth = dateStr,
                                        timeOfBirth = timeStr,
                                        placeOfBirth = placeStr,
                                        sunSign = sign
                                    ).fold(
                                        onSuccess = { aiAnalysis ->
                                            birthChartData = BirthChartData(
                                                name = name,
                                                dateOfBirth = selectedDate,
                                                timeOfBirth = selectedTime,
                                                placeOfBirth = placeOfBirth.ifBlank { null },
                                                zodiacSign = sign,
                                                moonSign = moonSign,
                                                risingSign = risingSign,
                                                luckyNumbers = luckyNums,
                                                moonPhase = moonPh,
                                                luckyGemstone = gemstone,
                                                luckyColor = luckyCol,
                                                analysis = aiAnalysis,
                                                recommendations = listOf(
                                                    "Review the detailed AI analysis below",
                                                    "Consider your birth time for more accuracy",
                                                    "Meditate on your cosmic insights"
                                                )
                                            )
                                            isGenerating = false
                                        },
                                        onFailure = { error ->
                                            val moonSign = calculateMoonSign(selectedDate)
                                            val risingSign = calculateRisingSign(selectedDate, selectedTime)
                                            val luckyNums = calculateLuckyNumbers(selectedDate, sign)
                                            val moonPh = calculateMoonPhase(selectedDate)
                                            val gemstone = when(sign) {
                                                ZodiacSign.ARIES -> "Diamond"
                                                ZodiacSign.TAURUS -> "Emerald"
                                                ZodiacSign.GEMINI -> "Pearl"
                                                ZodiacSign.CANCER -> "Ruby"
                                                ZodiacSign.LEO -> "Peridot"
                                                ZodiacSign.VIRGO -> "Sapphire"
                                                ZodiacSign.LIBRA -> "Opal"
                                                ZodiacSign.SCORPIO -> "Topaz"
                                                ZodiacSign.SAGITTARIUS -> "Turquoise"
                                                ZodiacSign.CAPRICORN -> "Garnet"
                                                ZodiacSign.AQUARIUS -> "Amethyst"
                                                ZodiacSign.PISCES -> "Aquamarine"
                                            }
                                            val luckyCol = when(sign) {
                                                ZodiacSign.ARIES -> "Red"
                                                ZodiacSign.TAURUS -> "Green"
                                                ZodiacSign.GEMINI -> "Yellow"
                                                ZodiacSign.CANCER -> "Silver"
                                                ZodiacSign.LEO -> "Gold"
                                                ZodiacSign.VIRGO -> "Navy Blue"
                                                ZodiacSign.LIBRA -> "Pink"
                                                ZodiacSign.SCORPIO -> "Maroon"
                                                ZodiacSign.SAGITTARIUS -> "Purple"
                                                ZodiacSign.CAPRICORN -> "Brown"
                                                ZodiacSign.AQUARIUS -> "Turquoise"
                                                ZodiacSign.PISCES -> "Sea Green"
                                            }
                                            // Fallback if AI fails
                                            birthChartData = BirthChartData(
                                                name = name,
                                                dateOfBirth = selectedDate,
                                                timeOfBirth = selectedTime,
                                                placeOfBirth = placeOfBirth.ifBlank { null },
                                                zodiacSign = sign,
                                                moonSign = moonSign,
                                                risingSign = risingSign,
                                                luckyNumbers = luckyNums,
                                                moonPhase = moonPh,
                                                luckyGemstone = gemstone,
                                                luckyColor = luckyCol,
                                                analysis = "Error generating analysis: ${error.message}. Please check your internet connection and try again.",
                                                recommendations = listOf(
                                                    "Check your internet connection",
                                                    "Try again in a moment"
                                                )
                                            )
                                            isGenerating = false
                                        }
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(72.dp),
                        enabled = name.isNotBlank() && !isGenerating,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(MysticPurple, CosmicBlue, TertiaryLight)
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isGenerating) {
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        "Generating Your Chart...",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            } else {
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("🌟", fontSize = 28.sp)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "Generate Complete Analysis",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // Display Results
                val data = birthChartData!!
                
                // Basic Info
                item {
                    GradientGlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        gradientColors = listOf(
                            MysticPurple.copy(alpha = 0.4f),
                            CosmicBlue.copy(alpha = 0.2f)
                        )
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = data.name,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${data.zodiacSign.emoji} ${data.zodiacSign.displayName}",
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = data.dateOfBirth.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")),
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
                
                // Sun, Moon, Rising Signs
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        GradientGlassCard(
                            modifier = Modifier.weight(1f),
                            gradientColors = listOf(
                                LuckyGold.copy(alpha = 0.3f),
                                LuckyGold.copy(alpha = 0.1f)
                            )
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("☀️", fontSize = 32.sp)
                                Text("Sun Sign", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(0.7f))
                                Text(data.zodiacSign.displayName, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        
                        GradientGlassCard(
                            modifier = Modifier.weight(1f),
                            gradientColors = listOf(
                                SecondaryLight.copy(alpha = 0.3f),
                                SecondaryLight.copy(alpha = 0.1f)
                            )
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("🌙", fontSize = 32.sp)
                                Text("Moon Sign", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(0.7f))
                                Text(data.moonSign.displayName, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        
                        GradientGlassCard(
                            modifier = Modifier.weight(1f),
                            gradientColors = listOf(
                                CosmicBlue.copy(alpha = 0.3f),
                                CosmicBlue.copy(alpha = 0.1f)
                            )
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("⬆️", fontSize = 32.sp)
                                Text("Rising", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(0.7f))
                                Text(data.risingSign.displayName, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                
                // AI Complete Analysis
                item {
                    GradientGlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        gradientColors = listOf(
                            MysticPurple.copy(alpha = 0.3f),
                            CosmicBlue.copy(alpha = 0.2f)
                        )
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🔮", fontSize = 32.sp)
                                Spacer(Modifier.width(12.dp))
                                Text(
                                    text = "Complete AI Analysis",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            HorizontalDivider(
                                thickness = 2.dp,
                                color = MaterialTheme.colorScheme.primary.copy(0.3f)
                            )
                            Text(
                                text = data.analysis,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                                lineHeight = 24.sp
                            )
                        }
                    }
                }
                
                // LOVE & RELATIONSHIPS Section
                if (data.loveAndRelationships.isNotEmpty()) {
                    item {
                        GradientGlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            gradientColors = listOf(
                                Color(0xFFFF6B9D).copy(alpha = 0.3f),
                                Color(0xFFC06C84).copy(alpha = 0.2f)
                            )
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("💕", fontSize = 32.sp)
                                    Spacer(Modifier.width(12.dp))
                                    Text(
                                        text = "Love & Relationships",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFFF6B9D)
                                    )
                                }
                                HorizontalDivider(color = Color(0xFFFF6B9D).copy(0.3f))
                                Text(
                                    text = data.loveAndRelationships,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(0.9f),
                                    lineHeight = 22.sp
                                )
                            }
                        }
                    }
                }
                
                // CAREER & FINANCE Section
                if (data.careerAndFinance.isNotEmpty()) {
                    item {
                        GradientGlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            gradientColors = listOf(
                                LuckyGold.copy(alpha = 0.3f),
                                FireElement.copy(alpha = 0.2f)
                            )
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("💼", fontSize = 32.sp)
                                    Spacer(Modifier.width(12.dp))
                                    Text(
                                        text = "Career & Finance",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = LuckyGold
                                    )
                                }
                                HorizontalDivider(color = LuckyGold.copy(0.3f))
                                Text(
                                    text = data.careerAndFinance,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(0.9f),
                                    lineHeight = 22.sp
                                )
                            }
                        }
                    }
                }
                
                // HEALTH & WELLNESS Section
                if (data.healthAndWellness.isNotEmpty()) {
                    item {
                        GradientGlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            gradientColors = listOf(
                                EarthElement.copy(alpha = 0.3f),
                                Color(0xFF4CAF50).copy(alpha = 0.2f)
                            )
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("🏥", fontSize = 32.sp)
                                    Spacer(Modifier.width(12.dp))
                                    Text(
                                        text = "Health & Wellness",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF4CAF50)
                                    )
                                }
                                HorizontalDivider(color = Color(0xFF4CAF50).copy(0.3f))
                                Text(
                                    text = data.healthAndWellness,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(0.9f),
                                    lineHeight = 22.sp
                                )
                            }
                        }
                    }
                }
                
                // COMPATIBILITY INSIGHTS Section
                if (data.compatibilityInsights.isNotEmpty()) {
                    item {
                        GradientGlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            gradientColors = listOf(
                                CosmicBlue.copy(alpha = 0.3f),
                                SecondaryLight.copy(alpha = 0.2f)
                            )
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("🤝", fontSize = 32.sp)
                                    Spacer(Modifier.width(12.dp))
                                    Text(
                                        text = "Compatibility Insights",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CosmicBlue
                                    )
                                }
                                HorizontalDivider(color = CosmicBlue.copy(0.3f))
                                Text(
                                    text = data.compatibilityInsights,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(0.9f),
                                    lineHeight = 22.sp
                                )
                            }
                        }
                    }
                }
                
                // FUTURE GUIDANCE Section
                if (data.futureGuidance.isNotEmpty()) {
                    item {
                        GradientGlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            gradientColors = listOf(
                                MysticPurple.copy(alpha = 0.4f),
                                TertiaryLight.copy(alpha = 0.3f)
                            )
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("🔮", fontSize = 32.sp)
                                    Spacer(Modifier.width(12.dp))
                                    Text(
                                        text = "Future Guidance",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MysticPurple
                                    )
                                }
                                HorizontalDivider(color = MysticPurple.copy(0.3f))
                                Text(
                                    text = data.futureGuidance,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(0.9f),
                                    lineHeight = 22.sp
                                )
                            }
                        }
                    }
                }
                
                // Lucky Gemstone
                item {
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text("💎", fontSize = 48.sp)
                            Column {
                                Text(
                                    text = "Your Lucky Gemstone",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(0.7f)
                                )
                                Text(
                                    text = data.luckyGemstone,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
                
                // Recommendations
                item {
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text(
                                text = "✨ Quick Tips",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            data.recommendations.forEach { rec ->
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    Text("• ", fontSize = 14.sp)
                                    Text(
                                        text = rec,
                                        fontSize = 14.sp,
                                        lineHeight = 20.sp,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }
                
                // Reset Button
                item {
                    OutlinedButton(
                        onClick = { birthChartData = null },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Refresh, "Reset")
                        Spacer(Modifier.width(8.dp))
                        Text("Generate New Analysis")
                    }
                }
            }
        }
        
        // Date Picker Dialog
        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            selectedDate = java.time.Instant.ofEpochMilli(millis)
                                .atZone(java.time.ZoneId.systemDefault())
                                .toLocalDate()
                        }
                        showDatePicker = false
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
        
        // Time Picker Dialog
        if (showTimePicker) {
            var hourText by remember { mutableStateOf("12") }
            var minuteText by remember { mutableStateOf("00") }
            
            AlertDialog(
                onDismissRequest = { showTimePicker = false },
                title = { Text("Select Time of Birth") },
                text = {
                    Column {
                        Text("Enter time in HH:MM format (24-hour)")
                        Spacer(Modifier.height(8.dp))
                        
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = hourText,
                                onValueChange = { if (it.length <= 2) hourText = it },
                                label = { Text("Hour") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = minuteText,
                                onValueChange = { if (it.length <= 2) minuteText = it },
                                label = { Text("Minute") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        try {
                            val hour = hourText.toIntOrNull() ?: 12
                            val minute = minuteText.toIntOrNull() ?: 0
                            if (hour in 0..23 && minute in 0..59) {
                                selectedTime = LocalTime.of(hour, minute)
                            }
                        } catch (e: Exception) {
                            // Invalid time
                        }
                        showTimePicker = false
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showTimePicker = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
