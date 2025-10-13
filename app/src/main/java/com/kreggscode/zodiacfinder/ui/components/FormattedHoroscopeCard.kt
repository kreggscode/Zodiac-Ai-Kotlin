package com.kreggscode.zodiacfinder.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kreggscode.zodiacfinder.data.model.ZodiacSign
import com.kreggscode.zodiacfinder.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun FormattedHoroscopeCard(
    sign: ZodiacSign?,
    overall: String,
    love: String,
    career: String,
    finance: String,
    health: String,
    luckyColor: String,
    luckyNumber: Int,
    date: String = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault()).format(Date())
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Date Display
            GradientGlassCard(
                modifier = Modifier.fillMaxWidth(),
                gradientColors = listOf(
                    MysticPurple.copy(alpha = 0.2f),
                    CosmicBlue.copy(alpha = 0.1f)
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "📅",
                        fontSize = 24.sp
                    )
                    Text(
                        text = date,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            
            // Sign Header
            sign?.let {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = it.symbol,
                        fontSize = 48.sp
                    )
                    Column {
                        Text(
                            text = it.displayName,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = it.dateRange,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
                
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                )
            }
            
            // Overall Energy
            if (overall.isNotEmpty()) {
                HoroscopeSectionFormatted(
                    emoji = "💫",
                    title = "Overall Energy",
                    content = overall
                )
            }
            
            // Love & Relationships
            if (love.isNotEmpty()) {
                HoroscopeSectionFormatted(
                    emoji = "💕",
                    title = "Love & Relationships",
                    content = love
                )
            }
            
            // Career & Finance
            if (career.isNotEmpty()) {
                HoroscopeSectionFormatted(
                    emoji = "💼",
                    title = "Career & Work",
                    content = career
                )
            }
            
            // Finance
            if (finance.isNotEmpty()) {
                HoroscopeSectionFormatted(
                    emoji = "💰",
                    title = "Finance & Money",
                    content = finance
                )
            }
            
            // Health & Wellness
            if (health.isNotEmpty()) {
                HoroscopeSectionFormatted(
                    emoji = "🏥",
                    title = "Health & Wellness",
                    content = health
                )
            }
            
            // Lucky Elements
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
            )
            
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "✨",
                        fontSize = 20.sp
                    )
                    Text(
                        text = "Lucky Elements",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "🎨 Color",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Text(
                            text = luckyColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    
                    Column {
                        Text(
                            text = "🍀 Number",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Text(
                            text = luckyNumber.toString(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HoroscopeSectionFormatted(
    emoji: String,
    title: String,
    content: String
) {
    val sectionColor = when (title) {
        "Overall Energy" -> MysticPurple
        "Love & Relationships" -> TertiaryLight
        "Career & Work" -> CosmicBlue
        "Finance & Money" -> LuckyGold
        "Health & Wellness" -> SecondaryLight
        else -> MaterialTheme.colorScheme.primary
    }
    
    GradientGlassCard(
        modifier = Modifier.fillMaxWidth(),
        gradientColors = listOf(
            sectionColor.copy(alpha = 0.2f),
            sectionColor.copy(alpha = 0.05f)
        )
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Text(
                    text = emoji,
                    fontSize = 32.sp
                )
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = sectionColor
                )
            }
            
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 4.dp),
                color = sectionColor.copy(alpha = 0.3f),
                thickness = 2.dp
            )
            
            Text(
                text = content,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.95f),
                lineHeight = 24.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}
