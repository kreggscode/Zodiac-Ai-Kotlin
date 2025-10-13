package com.kreggscode.zodiacfinder.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kreggscode.zodiacfinder.data.model.ZodiacSign
import com.kreggscode.zodiacfinder.data.repository.ZodiacRepository
import com.kreggscode.zodiacfinder.ui.components.*
import com.kreggscode.zodiacfinder.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("ZodiacProfile", Context.MODE_PRIVATE) }
    
    var name by remember { mutableStateOf(prefs.getString("name", "") ?: "") }
    var dateOfBirth by remember { mutableStateOf(prefs.getString("dateOfBirth", "") ?: "") }
    var timeOfBirth by remember { mutableStateOf(prefs.getString("timeOfBirth", "") ?: "") }
    var placeOfBirth by remember { mutableStateOf(prefs.getString("placeOfBirth", "") ?: "") }
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedSign by remember { mutableStateOf(determineZodiacFromDate(prefs.getString("dateOfBirth", "") ?: "")) }
    
    // AI Analysis state
    var aiAnalysis by remember { mutableStateOf("") }
    var isLoadingAnalysis by remember { mutableStateOf(false) }
    var showGenerateButton by remember { mutableStateOf(selectedSign != null && aiAnalysis.isEmpty()) }
    
    val repository = remember { ZodiacRepository() }
    val coroutineScope = rememberCoroutineScope()
    
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
                    title = "🌟 My Profile",
                    subtitle = "Your cosmic identity",
                    onBackClick = onNavigateBack
                )
            }
            
            // Profile Avatar Card
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
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(50.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile",
                                modifier = Modifier.size(60.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = if (name.isBlank()) "Your Name" else name,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        
                        if (selectedSign != null) {
                            Text(
                                text = "${selectedSign!!.emoji} ${selectedSign!!.displayName}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = selectedSign!!.dateRange,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        NeumorphicButton(
                            onClick = { showEditDialog = true },
                            modifier = Modifier.fillMaxWidth(0.6f)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit",
                                    modifier = Modifier.size(20.dp)
                                )
                                Text("Edit Profile")
                            }
                        }
                    }
                }
            }
            
            // Birth Details
            if (dateOfBirth.isNotBlank()) {
                item {
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            Text(
                                text = "📅 Birth Details",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                            )
                            
                            InfoRow(label = "Date of Birth", value = dateOfBirth)
                            if (timeOfBirth.isNotBlank()) {
                                InfoRow(label = "Time of Birth", value = timeOfBirth)
                            }
                            if (placeOfBirth.isNotBlank()) {
                                InfoRow(label = "Place of Birth", value = placeOfBirth)
                            }
                        }
                    }
                }
            }
            
            // Lucky Elements
            if (selectedSign != null) {
                item {
                    GradientGlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        gradientColors = listOf(
                            LuckyGold.copy(alpha = 0.3f),
                            LuckyGold.copy(alpha = 0.1f)
                        )
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            Text(
                                text = "✨ Your Lucky Elements",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = LuckyGold
                            )
                            
                            HorizontalDivider(
                                color = LuckyGold.copy(alpha = 0.3f),
                                thickness = 2.dp
                            )
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                LuckyElementCard(
                                    emoji = "🍀",
                                    label = "Lucky Number",
                                    value = getLuckyNumberForSign(selectedSign!!).toString()
                                )
                                
                                LuckyElementCard(
                                    emoji = "🎨",
                                    label = "Lucky Color",
                                    value = getLuckyColorForSign(selectedSign!!)
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                LuckyElementCard(
                                    emoji = "💎",
                                    label = "Gemstone",
                                    value = getGemstoneForSign(selectedSign!!)
                                )
                                
                                LuckyElementCard(
                                    emoji = "🌸",
                                    label = "Element",
                                    value = selectedSign!!.element.displayName
                                )
                            }
                        }
                    }
                }
                
                // Crystals & Gemstones Details
                item {
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text(
                                text = "💎 Your Crystals & Gemstones",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                            )
                            
                            Text(
                                text = getGemstoneDescriptionForSign(selectedSign!!),
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                                lineHeight = 22.sp
                            )
                        }
                    }
                }
            }
            
            // AI Analysis Section
            if (selectedSign != null && showGenerateButton && aiAnalysis.isEmpty()) {
                item {
                    Button(
                        onClick = {
                            isLoadingAnalysis = true
                            coroutineScope.launch {
                                val dateStr = dateOfBirth
                                val timeStr = timeOfBirth.ifBlank { "Time not provided" }
                                val placeStr = placeOfBirth.ifBlank { "Location not provided" }
                                
                                repository.generateBirthChartAnalysis(
                                    dateOfBirth = dateStr,
                                    timeOfBirth = timeStr,
                                    placeOfBirth = placeStr,
                                    sunSign = selectedSign!!
                                ).fold(
                                    onSuccess = { analysis ->
                                        aiAnalysis = analysis
                                        isLoadingAnalysis = false
                                        showGenerateButton = false
                                    },
                                    onFailure = {
                                        aiAnalysis = "Unable to generate analysis. Please check your internet connection."
                                        isLoadingAnalysis = false
                                        showGenerateButton = false
                                    }
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(72.dp),
                        enabled = !isLoadingAnalysis,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = androidx.compose.ui.graphics.Color.Transparent
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
                            if (isLoadingAnalysis) {
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = androidx.compose.ui.graphics.Color.White
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        "Analyzing Your Chart...",
                                        color = androidx.compose.ui.graphics.Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            } else {
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("🔮", fontSize = 28.sp)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "Generate AI Analysis",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = androidx.compose.ui.graphics.Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            // AI Analysis Display
            if (aiAnalysis.isNotEmpty()) {
                item {
                    GradientGlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        gradientColors = listOf(
                            MysticPurple.copy(alpha = 0.4f),
                            CosmicBlue.copy(alpha = 0.3f)
                        )
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🔮", fontSize = 32.sp)
                                Spacer(Modifier.width(12.dp))
                                Text(
                                    text = "Your Personalized Analysis",
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
                                text = aiAnalysis,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                                lineHeight = 24.sp
                            )
                        }
                    }
                }
            }
            
            // Empty State
            if (selectedSign == null) {
                item {
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "✨",
                                fontSize = 64.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Complete your profile",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Add your birth details to unlock personalized insights, lucky elements, and gemstone recommendations",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
        
        // Edit Dialog
        if (showEditDialog) {
            ProfileEditDialog(
                currentName = name,
                currentDate = dateOfBirth,
                currentTime = timeOfBirth,
                currentPlace = placeOfBirth,
                onDismiss = { showEditDialog = false },
                onSave = { newName, newDate, newTime, newPlace ->
                    name = newName
                    dateOfBirth = newDate
                    timeOfBirth = newTime
                    placeOfBirth = newPlace
                    // Calculate zodiac sign from date
                    selectedSign = determineZodiacFromDate(newDate)
                    
                    // Save to SharedPreferences
                    prefs.edit().apply {
                        putString("name", newName)
                        putString("dateOfBirth", newDate)
                        putString("timeOfBirth", newTime)
                        putString("placeOfBirth", newPlace)
                        apply()
                    }
                    
                    showEditDialog = false
                    showGenerateButton = true
                    aiAnalysis = ""
                }
            )
        }
    }
}

@Composable
private fun LuckyElementCard(
    emoji: String,
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            text = emoji,
            fontSize = 36.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileEditDialog(
    currentName: String,
    currentDate: String,
    currentTime: String,
    currentPlace: String,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(currentName) }
    var date by remember { mutableStateOf(currentDate) }
    var time by remember { mutableStateOf(currentTime) }
    var place by remember { mutableStateOf(currentPlace) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )
    
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val localDate = java.time.Instant.ofEpochMilli(millis)
                            .atZone(java.time.ZoneId.systemDefault())
                            .toLocalDate()
                        date = "${localDate.monthValue.toString().padStart(2, '0')}/${localDate.dayOfMonth.toString().padStart(2, '0')}/${localDate.year}"
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
    
    if (showTimePicker) {
        var hourText by remember { mutableStateOf("12") }
        var minuteText by remember { mutableStateOf("00") }
        
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            title = { Text("Select Time of Birth") },
            text = {
                Column {
                    Text("Enter time in HH:MM format (24-hour)")
                    Spacer(androidx.compose.ui.Modifier.height(8.dp))
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = hourText,
                            onValueChange = { if (it.length <= 2) hourText = it },
                            label = { Text("Hour") },
                            modifier = androidx.compose.ui.Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = minuteText,
                            onValueChange = { if (it.length <= 2) minuteText = it },
                            label = { Text("Minute") },
                            modifier = androidx.compose.ui.Modifier.weight(1f),
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
                            time = String.format("%02d:%02d", hour, minute)
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
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Edit Profile",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                OutlinedButton(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(if (date.isBlank()) "Select Date of Birth" else date)
                        Icon(Icons.Default.CalendarToday, "Calendar")
                    }
                }
                
                OutlinedButton(
                    onClick = { showTimePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(if (time.isBlank()) "Select Time of Birth (Optional)" else time)
                        Icon(Icons.Default.Schedule, "Time")
                    }
                }
                
                OutlinedTextField(
                    value = place,
                    onValueChange = { place = it },
                    label = { Text("Place of Birth (Optional)") },
                    placeholder = { Text("City, Country") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(name, date, time, place) }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

// Helper functions
private fun determineZodiacFromDate(dateString: String): ZodiacSign? {
    if (dateString.isBlank()) return null
    
    val parts = dateString.split("/")
    if (parts.size != 3) return null
    
    val month = parts[0].toIntOrNull() ?: return null
    val day = parts[1].toIntOrNull() ?: return null
    
    return when {
        (month == 3 && day >= 21) || (month == 4 && day <= 19) -> ZodiacSign.ARIES
        (month == 4 && day >= 20) || (month == 5 && day <= 20) -> ZodiacSign.TAURUS
        (month == 5 && day >= 21) || (month == 6 && day <= 20) -> ZodiacSign.GEMINI
        (month == 6 && day >= 21) || (month == 7 && day <= 22) -> ZodiacSign.CANCER
        (month == 7 && day >= 23) || (month == 8 && day <= 22) -> ZodiacSign.LEO
        (month == 8 && day >= 23) || (month == 9 && day <= 22) -> ZodiacSign.VIRGO
        (month == 9 && day >= 23) || (month == 10 && day <= 22) -> ZodiacSign.LIBRA
        (month == 10 && day >= 23) || (month == 11 && day <= 21) -> ZodiacSign.SCORPIO
        (month == 11 && day >= 22) || (month == 12 && day <= 21) -> ZodiacSign.SAGITTARIUS
        (month == 12 && day >= 22) || (month == 1 && day <= 19) -> ZodiacSign.CAPRICORN
        (month == 1 && day >= 20) || (month == 2 && day <= 18) -> ZodiacSign.AQUARIUS
        else -> ZodiacSign.PISCES
    }
}

private fun getLuckyNumberForSign(sign: ZodiacSign): Int {
    return when (sign) {
        ZodiacSign.ARIES -> 9
        ZodiacSign.TAURUS -> 6
        ZodiacSign.GEMINI -> 5
        ZodiacSign.CANCER -> 2
        ZodiacSign.LEO -> 1
        ZodiacSign.VIRGO -> 5
        ZodiacSign.LIBRA -> 6
        ZodiacSign.SCORPIO -> 8
        ZodiacSign.SAGITTARIUS -> 3
        ZodiacSign.CAPRICORN -> 8
        ZodiacSign.AQUARIUS -> 4
        ZodiacSign.PISCES -> 7
    }
}

private fun getLuckyColorForSign(sign: ZodiacSign): String {
    return when (sign) {
        ZodiacSign.ARIES -> "Red"
        ZodiacSign.TAURUS -> "Green"
        ZodiacSign.GEMINI -> "Yellow"
        ZodiacSign.CANCER -> "Silver"
        ZodiacSign.LEO -> "Gold"
        ZodiacSign.VIRGO -> "Navy Blue"
        ZodiacSign.LIBRA -> "Pink"
        ZodiacSign.SCORPIO -> "Black"
        ZodiacSign.SAGITTARIUS -> "Purple"
        ZodiacSign.CAPRICORN -> "Brown"
        ZodiacSign.AQUARIUS -> "Turquoise"
        ZodiacSign.PISCES -> "Sea Green"
    }
}

private fun getGemstoneForSign(sign: ZodiacSign): String {
    return when (sign) {
        ZodiacSign.ARIES -> "Diamond"
        ZodiacSign.TAURUS -> "Emerald"
        ZodiacSign.GEMINI -> "Agate"
        ZodiacSign.CANCER -> "Pearl"
        ZodiacSign.LEO -> "Ruby"
        ZodiacSign.VIRGO -> "Sapphire"
        ZodiacSign.LIBRA -> "Opal"
        ZodiacSign.SCORPIO -> "Topaz"
        ZodiacSign.SAGITTARIUS -> "Turquoise"
        ZodiacSign.CAPRICORN -> "Garnet"
        ZodiacSign.AQUARIUS -> "Amethyst"
        ZodiacSign.PISCES -> "Aquamarine"
    }
}

private fun getGemstoneDescriptionForSign(sign: ZodiacSign): String {
    return when (sign) {
        ZodiacSign.ARIES -> "💎 Diamond - Enhances courage and strength. Wear diamond jewelry to amplify your natural leadership qualities and boost confidence."
        ZodiacSign.TAURUS -> "💚 Emerald - Promotes love and prosperity. This stone helps Taurus maintain emotional balance and attracts abundance."
        ZodiacSign.GEMINI -> "🤎 Agate - Improves communication and mental clarity. Perfect for Gemini's intellectual pursuits and social connections."
        ZodiacSign.CANCER -> "🤍 Pearl - Enhances emotional healing and intuition. Pearls help Cancer process emotions and strengthen family bonds."
        ZodiacSign.LEO -> "❤️ Ruby - Amplifies passion and vitality. This fiery stone matches Leo's energy and enhances creativity."
        ZodiacSign.VIRGO -> "💙 Sapphire - Promotes wisdom and mental clarity. Helps Virgo focus their analytical mind and find inner peace."
        ZodiacSign.LIBRA -> "🌈 Opal - Brings harmony and balance. Perfect for Libra's quest for beauty and equilibrium in relationships."
        ZodiacSign.SCORPIO -> "🧡 Topaz - Enhances transformation and protection. Supports Scorpio's intense emotional depth and personal power."
        ZodiacSign.SAGITTARIUS -> "💚 Turquoise - Promotes wisdom and adventure. Protects Sagittarius during travels and spiritual journeys."
        ZodiacSign.CAPRICORN -> "❤️ Garnet - Enhances discipline and success. Supports Capricorn's ambitious goals and provides grounding energy."
        ZodiacSign.AQUARIUS -> "💜 Amethyst - Promotes intuition and innovation. Enhances Aquarius's visionary thinking and spiritual awareness."
        ZodiacSign.PISCES -> "💙 Aquamarine - Enhances intuition and emotional healing. Perfect for Pisces' empathetic and artistic nature."
    }
}
