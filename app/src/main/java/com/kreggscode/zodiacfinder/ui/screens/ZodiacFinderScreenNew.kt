package com.kreggscode.zodiacfinder.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kreggscode.zodiacfinder.data.model.ZodiacSign
import com.kreggscode.zodiacfinder.ui.components.*
import com.kreggscode.zodiacfinder.ui.theme.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZodiacFinderScreenNew(
    onNavigateBack: () -> Unit = {},
    onSignFound: (ZodiacSign) -> Unit = {},
    onNavigateToChart: () -> Unit = {}
) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedTime by remember { mutableStateOf<LocalTime?>(null) }
    var selectedLocation by remember { mutableStateOf<String?>(null) }
    var foundSign by remember { mutableStateOf<ZodiacSign?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )
    
    // Date Picker Dialog - at top level
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
    
    // Time Picker Dialog - at top level
    if (showTimePicker) {
        var hourText by remember { mutableStateOf("12") }
        var minuteText by remember { mutableStateOf("00") }
        
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            title = { Text("Select Birth Time") },
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
                    title = "✨ Find Your Sign",
                    subtitle = "Discover your complete astrological profile",
                    onBackClick = onNavigateBack
                )
            }
            
            // Beautiful Hero Section
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
                        Text(
                            text = "✨",
                            fontSize = 64.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Discover Your Cosmic Identity",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Enter your birth details to unlock your complete astrological profile",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
            
            // Date Input (Required) - Beautiful Card
            item {
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
                        // Icon Circle
                        Box(
                            modifier = Modifier
                                .size(64.dp)
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
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Birth Date",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = " *",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = selectedDate.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
            
            // Time Input (Optional) - Beautiful Card
            item {
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
                        // Icon Circle
                        Box(
                            modifier = Modifier
                                .size(64.dp)
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
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Birth Time (Optional)",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = selectedTime?.format(DateTimeFormatter.ofPattern("hh:mm a")) ?: "Tap to select for accurate Moon & Rising signs",
                                fontSize = 14.sp,
                                fontWeight = if (selectedTime != null) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (selectedTime != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(0.6f)
                            )
                        }
                    }
                }
            }
            
            // Location Input (Optional) - Beautiful Card
            item {
                GradientGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    gradientColors = listOf(
                        LuckyGold.copy(alpha = 0.15f),
                        LuckyGold.copy(alpha = 0.05f)
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Icon Circle
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(LuckyGold, FireElement)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.LocationOn,
                                "Location",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Birth Location (Optional)",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = selectedLocation ?: "",
                                onValueChange = { selectedLocation = it },
                                placeholder = { Text("City, Country", fontSize = 14.sp) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }
            }
            
            // Find Sign Button - STUNNING Gradient Button
            item {
                Button(
                    onClick = {
                        foundSign = ZodiacSign.fromDate(
                            selectedDate.monthValue,
                            selectedDate.dayOfMonth
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp),
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
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🔮",
                                fontSize = 28.sp
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Find My Sign",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
            
            // Result Card
            foundSign?.let { sign ->
                item {
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + expandVertically()
                    ) {
                        ZodiacResultCard(
                            sign = sign,
                            hasTime = selectedTime != null,
                            hasLocation = selectedLocation != null,
                            onViewFullChart = onNavigateToChart
                        )
                    }
                }
            }
            
            // Bottom padding
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun PremiumInputCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    value: String,
    onClick: () -> Unit,
    isRequired: Boolean
) {
    GradientGlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        gradientColors = listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
            MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f)
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Content
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (isRequired) {
                        Text(
                            text = " *",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = value,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun ZodiacResultCard(
    sign: ZodiacSign,
    hasTime: Boolean,
    hasLocation: Boolean,
    onViewFullChart: () -> Unit
) {
    val scale = remember { Animatable(0.8f) }
    var showAIAnalysis by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    }
    
    Column(
        modifier = Modifier.scale(scale.value),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Main Result Card
        GradientGlassCard(
            modifier = Modifier.fillMaxWidth(),
            gradientColors = listOf(
                when (sign.element) {
                    com.kreggscode.zodiacfinder.data.model.ZodiacElement.FIRE -> FireElement.copy(alpha = 0.4f)
                    com.kreggscode.zodiacfinder.data.model.ZodiacElement.EARTH -> EarthElement.copy(alpha = 0.4f)
                    com.kreggscode.zodiacfinder.data.model.ZodiacElement.AIR -> AirElement.copy(alpha = 0.4f)
                    com.kreggscode.zodiacfinder.data.model.ZodiacElement.WATER -> WaterElement.copy(alpha = 0.4f)
                },
                MaterialTheme.colorScheme.surface.copy(alpha = 0.2f)
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "🎉", fontSize = 40.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Your Zodiac Sign",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = sign.symbol,
                    fontSize = 80.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Text(
                    text = sign.displayName,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = sign.dateRange,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = sign.element.emoji,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = sign.element.displayName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
        
        // Quick Info Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            GlassCard(modifier = Modifier.weight(1f)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🌙", fontSize = 24.sp)
                    Text(
                        text = "Moon Sign",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(0.6f)
                    )
                    Text(
                        text = if (hasTime) "Calculated" else "Need Time",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (hasTime) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(0.5f)
                    )
                }
            }
            
            GlassCard(modifier = Modifier.weight(1f)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("⬆️", fontSize = 24.sp)
                    Text(
                        text = "Rising Sign",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(0.6f)
                    )
                    Text(
                        text = if (hasTime && hasLocation) "Calculated" else "Need Time+Loc",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (hasTime && hasLocation) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(0.5f)
                    )
                }
            }
        }
        
        // AI Analysis Button
        NeumorphicButton(
            onClick = { showAIAnalysis = !showAIAnalysis },
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (showAIAnalysis) "🤖 Hide AI Analysis" else "🤖 Get AI Analysis",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        // AI Analysis Content
        if (showAIAnalysis) {
            GradientGlassCard(
                modifier = Modifier.fillMaxWidth(),
                gradientColors = listOf(
                    MysticPurple.copy(alpha = 0.3f),
                    CosmicBlue.copy(alpha = 0.2f)
                )
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "🤖 AI Personality Analysis",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(0.1f))
                    Text(
                        text = getAIAnalysisForSign(sign),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                        lineHeight = 22.sp
                    )
                }
            }
        }
        
        // Action Buttons
        if (hasTime && hasLocation) {
            NeumorphicButton(
                onClick = onViewFullChart,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("📊 View Complete Birth Chart")
            }
        } else {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "💡 Pro Tip",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Add your birth time and location for a complete astrological chart with rising sign, moon sign, and planetary positions!",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

private fun getAIAnalysisForSign(sign: ZodiacSign): String {
    return when (sign) {
        ZodiacSign.ARIES -> "As an Aries, you're a natural-born leader with boundless energy and enthusiasm. Your pioneering spirit drives you to take initiative and blaze new trails. You thrive on challenges and competition, often being the first to try something new. Your direct communication style and courage make you excellent at starting projects, though patience with details may not be your strongest suit. Channel your passionate nature into meaningful goals, and remember that collaboration can amplify your already impressive achievements."
        ZodiacSign.TAURUS -> "Taurus individuals like you are grounded, reliable, and deeply connected to the physical world. You appreciate beauty, comfort, and stability in all aspects of life. Your patient and persistent nature means you see projects through to completion, making you incredibly dependable. You have excellent taste and a strong connection to nature and sensory experiences. While your steadfast nature is a strength, staying open to change and new perspectives will help you grow even more."
        ZodiacSign.GEMINI -> "As a Gemini, your quick wit and intellectual curiosity make you endlessly fascinating. You're a natural communicator who can adapt to any social situation with ease. Your mind is always active, seeking new information and connections. You excel at multitasking and can see multiple perspectives on any issue. Your versatility is your superpower, though focusing your abundant mental energy on one thing at a time can help you achieve even greater depth in your pursuits."
        ZodiacSign.CANCER -> "Cancer natives like you possess deep emotional intelligence and nurturing instincts. You're highly intuitive, often sensing others' needs before they're expressed. Your strong connection to home and family provides you with emotional security and purpose. You have an excellent memory, especially for emotional experiences, and you're fiercely protective of those you love. Your sensitivity is a gift that allows you to create deep, meaningful connections, though setting healthy boundaries will help you maintain your emotional wellbeing."
        ZodiacSign.LEO -> "As a Leo, you radiate warmth, confidence, and natural charisma. You're born to shine and inspire others with your generous spirit and creative talents. Your leadership style is characterized by warmth and encouragement rather than dominance. You have a strong sense of dignity and take pride in your accomplishments and relationships. Your dramatic flair and enthusiasm make life more exciting for everyone around you. Remember that true leadership also involves listening and sharing the spotlight with others."
        ZodiacSign.VIRGO -> "Virgo individuals like you possess exceptional analytical abilities and attention to detail. You're practical, methodical, and dedicated to improvement in all areas of life. Your service-oriented nature drives you to help others and make the world more efficient. You have high standards and a discerning eye that catches what others miss. Your organizational skills and problem-solving abilities are unmatched. While striving for excellence is admirable, remember that perfection is an ideal, not a requirement for worthiness."
        ZodiacSign.LIBRA -> "As a Libra, you're the diplomat of the zodiac, naturally skilled at creating harmony and balance. Your refined aesthetic sense and appreciation for beauty influence everything you do. You excel at seeing all sides of a situation and finding fair solutions. Your charm and social grace make you popular in any setting. You thrive in partnerships and value cooperation over competition. While seeking balance is your strength, don't forget that sometimes taking a clear stand is necessary and valuable."
        ZodiacSign.SCORPIO -> "Scorpio natives like you possess intense emotional depth and transformative power. You're passionate, resourceful, and unafraid to explore life's mysteries and hidden truths. Your intuition is razor-sharp, and you can read people and situations with uncanny accuracy. You're fiercely loyal to those you trust and have remarkable resilience in facing life's challenges. Your intensity is your gift, allowing you to experience life fully and facilitate profound change. Learning to trust and be vulnerable can deepen your already powerful connections."
        ZodiacSign.SAGITTARIUS -> "As a Sagittarius, you're an eternal optimist and seeker of truth and wisdom. Your adventurous spirit and philosophical nature drive you to explore both the world and ideas. You're honest, sometimes bluntly so, and you value freedom above all else. Your enthusiasm is contagious, and your broad perspective helps others see beyond their limitations. You're a natural teacher and storyteller. While your love of freedom is essential, remember that meaningful commitments can actually expand rather than limit your horizons."
        ZodiacSign.CAPRICORN -> "Capricorn individuals like you are ambitious, disciplined, and remarkably resilient. You have a mature, responsible approach to life and a strong work ethic. Your practical wisdom and strategic thinking help you build lasting success. You're patient and willing to work hard for long-term goals, understanding that worthwhile achievements take time. Your dry sense of humor and quiet strength inspire respect. While achievement is important, don't forget to celebrate your progress and enjoy the journey, not just the destination."
        ZodiacSign.AQUARIUS -> "As an Aquarius, you're a visionary thinker with a humanitarian heart. Your innovative ideas and unique perspective often put you ahead of your time. You value intellectual freedom and authentic self-expression above social conventions. Your friendly, egalitarian nature makes you a champion for social causes and progressive change. You're excellent at seeing the big picture and connecting diverse ideas and people. While your independence is vital, remember that emotional connection and vulnerability can enhance rather than threaten your individuality."
        ZodiacSign.PISCES -> "Pisces natives like you possess extraordinary empathy and creative imagination. You're deeply intuitive, often picking up on subtle energies and emotions that others miss. Your compassionate nature drives you to help those in need, and your artistic sensibilities allow you to express the ineffable. You have a rich inner world and a spiritual connection that transcends the material realm. Your sensitivity is a profound gift that allows you to create beauty and healing. Setting boundaries and staying grounded will help you maintain your energy while sharing your gifts with the world."
    }
}
