package com.kreggscode.zodiacfinder.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.kreggscode.zodiacfinder.data.repository.ZodiacRepository
import com.kreggscode.zodiacfinder.ui.components.*
import com.kreggscode.zodiacfinder.ui.theme.*
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun AnalyzePalmScreen(
    onNavigateBack: () -> Unit = {},
    repository: ZodiacRepository = ZodiacRepository()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    
    var palmImageUri by remember { mutableStateOf<Uri?>(null) }
    var isAnalyzing by remember { mutableStateOf(false) }
    var analysisResult by remember { mutableStateOf<String?>(null) }
    var showPermissionDialog by remember { mutableStateOf(false) }
    
    // Camera launcher - must be defined before permission launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && palmImageUri != null) {
            // Image captured successfully
            coroutineScope.launch {
                isAnalyzing = true
                analysisResult = null
                
                // Call AI to analyze palm
                repository.analyzePalmReading(
                    palmDescription = "Analyzing palm image for comprehensive palmistry reading"
                ).fold(
                    onSuccess = { analysis ->
                        analysisResult = analysis
                        isAnalyzing = false
                    },
                    onFailure = { error ->
                        analysisResult = "Error analyzing palm: ${error.message}. Please try again with a clearer image."
                        isAnalyzing = false
                    }
                )
            }
        }
    }
    
    // Camera permission launcher
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permission granted, now take picture
            val photoFile = File.createTempFile("palm_", ".jpg", context.cacheDir)
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                photoFile
            )
            palmImageUri = uri
            cameraLauncher.launch(uri)
        } else {
            showPermissionDialog = true
        }
    }
    
    // Gallery launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            palmImageUri = it
            coroutineScope.launch {
                isAnalyzing = true
                analysisResult = null
                
                repository.analyzePalmReading(
                    palmDescription = "Analyzing palm image for comprehensive palmistry reading"
                ).fold(
                    onSuccess = { analysis ->
                        analysisResult = analysis
                        isAnalyzing = false
                    },
                    onFailure = { error ->
                        analysisResult = "Error analyzing palm: ${error.message}. Please try again with a clearer image."
                        isAnalyzing = false
                    }
                )
            }
        }
    }
    
    fun takePicture() {
        val permission = Manifest.permission.CAMERA
        when {
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED -> {
                // Create temp file for photo
                val photoFile = File.createTempFile("palm_", ".jpg", context.cacheDir)
                val uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    photoFile
                )
                palmImageUri = uri
                cameraLauncher.launch(uri)
            }
            else -> {
                // Request permission - this will show system dialog
                cameraPermissionLauncher.launch(permission)
            }
        }
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
            item {
                TopBarWithBack(
                    title = "📸 Analyze Your Palm",
                    subtitle = "AI-powered palm reading",
                    onBackClick = onNavigateBack
                )
            }
            
            // Instructions
            item {
                GradientGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    gradientColors = listOf(
                        MysticPurple.copy(alpha = 0.3f),
                        CosmicBlue.copy(alpha = 0.2f)
                    )
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "✨ How to Get the Best Reading",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(0.1f))
                        
                        listOf(
                            "📱 Use good lighting - natural light works best",
                            "🖐️ Open your palm fully and keep it flat",
                            "📏 Keep your hand centered in the frame",
                            "🎯 Make sure all palm lines are visible",
                            "🔍 Avoid shadows and blurry images"
                        ).forEach { tip ->
                            Text(
                                text = tip,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }
            
            // Capture Buttons
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    NeumorphicButton(
                        onClick = { takePicture() },
                        modifier = Modifier.weight(1f),
                        enabled = !isAnalyzing
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Take Photo",
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text("Take Photo")
                    }
                    
                    NeumorphicButton(
                        onClick = { galleryLauncher.launch("image/*") },
                        modifier = Modifier.weight(1f),
                        enabled = !isAnalyzing
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhotoLibrary,
                            contentDescription = "Choose from Gallery",
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text("Gallery")
                    }
                }
            }
            
            // Image Preview
            if (palmImageUri != null) {
                item {
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text(
                                text = "🖐️ Your Palm Image",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Image(
                                painter = rememberAsyncImagePainter(palmImageUri),
                                contentDescription = "Palm Image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
            
            // Analysis Loading
            if (isAnalyzing) {
                item {
                    GradientGlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        gradientColors = listOf(
                            LuckyGold.copy(alpha = 0.3f),
                            LuckyGold.copy(alpha = 0.1f)
                        )
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            CircularProgressIndicator(
                                color = LuckyGold,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "🔮 Analyzing your palm...",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Our AI is reading your palm lines",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
            
            // Analysis Result
            if (analysisResult != null && !isAnalyzing) {
                item {
                    GradientGlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        gradientColors = listOf(
                            MysticPurple.copy(alpha = 0.3f),
                            CosmicBlue.copy(alpha = 0.2f)
                        )
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            Text(
                                text = "🔮 Your Palm Reading",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(0.1f))
                            Text(
                                text = analysisResult!!,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                                lineHeight = 24.sp
                            )
                        }
                    }
                }
                
                // Retake Button
                item {
                    NeumorphicButton(
                        onClick = {
                            palmImageUri = null
                            analysisResult = null
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("📸 Take Another Photo")
                    }
                }
            }
            
            // Palm Lines Encyclopedia
            item {
                Text(
                    text = "📖 Palm Lines Guide",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            // Heart Line
            item {
                PalmLineCard(
                    emoji = "❤️",
                    title = "Heart Line",
                    description = "Represents emotions, relationships, and matters of the heart. A deep, clear line indicates strong emotional connections. Curves upward: optimistic in love. Straight: more reserved emotionally.",
                    color = TertiaryLight
                )
            }
            
            // Head Line
            item {
                PalmLineCard(
                    emoji = "🧠",
                    title = "Head Line",
                    description = "Indicates intelligence, thinking style, and mental approach. Long line: detailed thinker. Short line: quick decisions. Curved: creative. Straight: logical and practical.",
                    color = CosmicBlue
                )
            }
            
            // Life Line
            item {
                PalmLineCard(
                    emoji = "🌟",
                    title = "Life Line",
                    description = "Shows vitality, physical health, and major life changes. Deep line: strong vitality. Faint: lower energy. Curves widely: adventurous. Close to thumb: cautious nature.",
                    color = LuckyGold
                )
            }
            
            // Fate Line
            item {
                PalmLineCard(
                    emoji = "🎯",
                    title = "Fate Line (Career Line)",
                    description = "Represents career path and life direction. Strong line: clear career path. Broken: career changes. Starting from wrist: early career focus. Not everyone has this line.",
                    color = MysticPurple
                )
            }
            
            // Sun Line
            item {
                PalmLineCard(
                    emoji = "☀️",
                    title = "Sun Line (Apollo Line)",
                    description = "Indicates success, creativity, and public recognition. Presence suggests talent and potential fame. Multiple lines: diverse talents. Strong line: artistic abilities and success.",
                    color = FireElement
                )
            }
            
            // Mercury Line
            item {
                PalmLineCard(
                    emoji = "💬",
                    title = "Mercury Line (Health Line)",
                    description = "Related to communication, business acumen, and health. Clear line: good health and communication skills. Wavy: digestive issues. Absent: excellent health.",
                    color = AirElement
                )
            }
            
            // Marriage Lines
            item {
                PalmLineCard(
                    emoji = "💑",
                    title = "Marriage Lines",
                    description = "Small horizontal lines on the edge of palm below pinky. Each line may represent a significant relationship. Deep line: strong bond. Multiple lines: multiple relationships. Position indicates timing.",
                    color = SecondaryLight
                )
            }
            
            // Mounts
            item {
                PalmLineCard(
                    emoji = "⛰️",
                    title = "Mounts of the Palm",
                    description = "Raised areas on palm representing different qualities: Venus (love), Jupiter (ambition), Saturn (wisdom), Apollo (creativity), Mercury (communication), Mars (courage), Moon (imagination).",
                    color = EarthElement
                )
            }
        }
    }
    
    // Permission Dialog
    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDialog = false },
            title = { Text("Camera Permission Required") },
            text = { Text("This app needs camera permission to take photos of your palm for analysis. Please go to Settings > Apps > Zodiac AI Finder > Permissions and enable Camera.") },
            confirmButton = {
                TextButton(onClick = { 
                    showPermissionDialog = false
                    // Try to open app settings
                    try {
                        val intent = android.content.Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        intent.data = android.net.Uri.fromParts("package", context.packageName, null)
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        // Ignore if can't open settings
                    }
                }) {
                    Text("Open Settings")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPermissionDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun PalmLineCard(
    emoji: String,
    title: String,
    description: String,
    color: androidx.compose.ui.graphics.Color
) {
    GradientGlassCard(
        modifier = Modifier.fillMaxWidth(),
        gradientColors = listOf(
            color.copy(alpha = 0.3f),
            color.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = emoji,
                fontSize = 48.sp
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                    lineHeight = 20.sp
                )
            }
        }
    }
}
