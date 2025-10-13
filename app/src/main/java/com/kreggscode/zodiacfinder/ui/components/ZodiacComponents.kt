package com.kreggscode.zodiacfinder.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kreggscode.zodiacfinder.data.model.ZodiacSign
import com.kreggscode.zodiacfinder.ui.theme.*

@Composable
fun ZodiacSignCard(
    sign: ZodiacSign,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false
) {
    val backgroundColor = when (sign.element) {
        com.kreggscode.zodiacfinder.data.model.ZodiacElement.FIRE -> FireElement.copy(alpha = if (isSelected) 0.6f else 0.3f)
        com.kreggscode.zodiacfinder.data.model.ZodiacElement.EARTH -> EarthElement.copy(alpha = if (isSelected) 0.6f else 0.3f)
        com.kreggscode.zodiacfinder.data.model.ZodiacElement.AIR -> AirElement.copy(alpha = if (isSelected) 0.6f else 0.3f)
        com.kreggscode.zodiacfinder.data.model.ZodiacElement.WATER -> WaterElement.copy(alpha = if (isSelected) 0.6f else 0.3f)
    }
    
    Box(
        modifier = modifier
            .clickable(onClick = onClick)
    ) {
        // Selection border/glow
        if (isSelected) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                LuckyGold,
                                MysticPurple,
                                CosmicBlue
                            )
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
            )
        }
        
        GradientGlassCard(
            modifier = Modifier
                .fillMaxSize()
                .padding(if (isSelected) 3.dp else 0.dp),
            gradientColors = listOf(
                backgroundColor,
                backgroundColor.copy(alpha = if (isSelected) 0.4f else 0.2f)
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Checkmark for selected
                if (isSelected) {
                    Text(
                        text = "✓",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = LuckyGold,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
                
                Text(
                    text = sign.symbol,
                    fontSize = 56.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) LuckyGold else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = sign.displayName,
                    fontSize = 16.sp,
                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = sign.dateRange,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun QuickActionCard(
    title: String,
    icon: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    badge: String? = null
) {
    Box(modifier = modifier) {
        GradientGlassCard(
            modifier = Modifier
                .clickable(onClick = onClick)
                .aspectRatio(1f),
            gradientColors = listOf(
                color.copy(alpha = 0.3f),
                color.copy(alpha = 0.1f)
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = icon,
                    fontSize = 48.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            }
        }
        
        // Badge
        if (badge != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .background(
                        color = LuckyGold,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = badge,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun ScoreIndicator(
    label: String,
    score: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$score%",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun HoroscopeSection(
    title: String,
    content: String,
    icon: String,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Text(
                    text = icon,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = content,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                lineHeight = 20.sp
            )
        }
    }
}
