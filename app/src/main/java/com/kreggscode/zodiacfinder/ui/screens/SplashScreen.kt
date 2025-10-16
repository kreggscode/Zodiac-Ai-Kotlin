package com.kreggscode.zodiacfinder.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kreggscode.zodiacfinder.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }
    
    // Trigger animation on launch
    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(2500) // Show splash for 2.5 seconds
        onSplashFinished()
    }
    
    // Animation values
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
        label = "alpha"
    )
    
    val scaleAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.5f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )
    
    // Rotating animation for stars
    val infiniteTransition = rememberInfiniteTransition(label = "rotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    
    // Pulsing animation for glow
    val pulseAnim by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0f0f23),
                        Color(0xFF1a1a2e),
                        Color(0xFF16213e),
                        Color(0xFF0f0f23)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Animated stars background
        AnimatedStarsBackground(rotation = rotation)
        
        // Main content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .alpha(alphaAnim.value)
                .scale(scaleAnim.value)
        ) {
            // Main icon with glow effect
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(180.dp)
            ) {
                // Outer glow
                Box(
                    modifier = Modifier
                        .size(180.dp * pulseAnim)
                        .alpha(0.3f)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    MysticPurple.copy(alpha = 0.6f),
                                    CosmicBlue.copy(alpha = 0.3f),
                                    Color.Transparent
                                )
                            )
                        )
                )
                
                // Main icon
                Text(
                    text = "🔮",
                    fontSize = 120.sp,
                    modifier = Modifier.scale(pulseAnim * 0.9f)
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // App name with gradient
            Text(
                text = "ZodiacAI Finder",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Tagline with shimmer effect
            Text(
                text = "Your Cosmic Companion",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.7f),
                fontWeight = FontWeight.Light
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Loading indicator
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) { index ->
                    LoadingDot(delay = index * 200)
                }
            }
        }
        
        // Bottom branding
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
                .alpha(alphaAnim.value)
        ) {
            Text(
                text = "✨",
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Discover Your Destiny",
                fontSize = 14.sp,
                color = LuckyGold.copy(alpha = 0.8f),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun AnimatedStarsBackground(rotation: Float) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Create multiple layers of stars
        repeat(30) { index ->
            val xPos = remember { kotlin.random.Random.nextInt(0, 100) }
            val yPos = remember { kotlin.random.Random.nextInt(0, 100) }
            val size = remember { kotlin.random.Random.nextInt(1, 4) }
            val alpha = remember { kotlin.random.Random.nextFloat() * 0.6f + 0.3f }
            
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.TopStart)
                    .offset(
                        x = (xPos.toFloat() / 100f * 400).dp,
                        y = (yPos.toFloat() / 100f * 800).dp
                    )
            ) {
                Text(
                    text = if (index % 3 == 0) "✨" else "⭐",
                    fontSize = (size * 4).sp,
                    modifier = Modifier.alpha(alpha)
                )
            }
        }
    }
}

@Composable
private fun LoadingDot(delay: Int) {
    val infiniteTransition = rememberInfiniteTransition(label = "loading")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = delay, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = delay, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    Box(
        modifier = Modifier
            .size(12.dp)
            .scale(scale)
            .alpha(alpha)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        LuckyGold,
                        LuckyGold.copy(alpha = 0.5f)
                    )
                ),
                shape = androidx.compose.foundation.shape.CircleShape
            )
    )
}
