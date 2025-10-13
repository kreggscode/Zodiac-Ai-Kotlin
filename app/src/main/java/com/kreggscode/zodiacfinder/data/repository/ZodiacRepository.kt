package com.kreggscode.zodiacfinder.data.repository

import com.kreggscode.zodiacfinder.data.api.MessageRequest
import com.kreggscode.zodiacfinder.data.api.PollinationAIService
import com.kreggscode.zodiacfinder.data.api.PollinationRequest
import com.kreggscode.zodiacfinder.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class ZodiacRepository(
    private val aiService: PollinationAIService = PollinationAIService.create()
) {
    
    // Generate Daily Horoscope using AI
    suspend fun getDailyHoroscope(sign: ZodiacSign): Result<DailyHoroscope> = withContext(Dispatchers.IO) {
        try {
            val currentDate = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date())
            val prompt = """
                Generate a detailed daily horoscope for ${sign.displayName} for $currentDate.
                Include the following sections:
                1. Love & Relationships
                2. Career & Work
                3. Finance & Money
                4. Health & Wellness
                5. Overall Luck
                6. Lucky Number (single digit 1-9)
                7. Lucky Color
                
                Format the response as JSON with keys: love, career, finance, health, luck, luckyNumber, luckyColor, overall
            """.trimIndent()
            
            val request = PollinationRequest(
                model = "openai",
                messages = listOf(
                    MessageRequest("system", "You are an expert astrologer providing daily horoscope readings. Be insightful, positive, and specific."),
                    MessageRequest("user", prompt)
                ),
                temperature = 1.0f
            )
            
            val response = aiService.generateText(request)
            val content = response.choices.firstOrNull()?.message?.content ?: ""
            
            // Parse the AI response (simplified - in production, use proper JSON parsing)
            val horoscope = DailyHoroscope(
                sign = sign,
                date = currentDate,
                overall = content,
                luckyNumber = (1..9).random(),
                luckyColor = listOf("Red", "Blue", "Green", "Purple", "Gold", "Silver").random()
            )
            
            Result.success(horoscope)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Check Compatibility
    suspend fun checkCompatibility(sign1: ZodiacSign, sign2: ZodiacSign): Result<CompatibilityResult> = withContext(Dispatchers.IO) {
        try {
            val prompt = """
                Analyze the zodiac compatibility between ${sign1.displayName} and ${sign2.displayName}.
                Provide scores (0-100) for:
                1. Love & Romance
                2. Marriage & Long-term
                3. Career Partnership
                4. Friendship
                5. Overall Compatibility
                
                Also provide insights and suggestions for making the relationship work.
                Format as JSON with keys: loveScore, marriageScore, careerScore, friendshipScore, overallScore, insights, suggestions (array)
            """.trimIndent()
            
            val request = PollinationRequest(
                model = "openai",
                messages = listOf(
                    MessageRequest("system", "You are an expert astrologer specializing in zodiac compatibility analysis."),
                    MessageRequest("user", prompt)
                ),
                temperature = 1.0f
            )
            
            val response = aiService.generateText(request)
            val content = response.choices.firstOrNull()?.message?.content ?: ""
            
            // Simplified compatibility result
            val result = CompatibilityResult(
                sign1 = sign1,
                sign2 = sign2,
                loveScore = (60..95).random(),
                marriageScore = (55..90).random(),
                careerScore = (50..85).random(),
                friendshipScore = (65..95).random(),
                overallScore = (60..90).random(),
                insights = content,
                suggestions = listOf(
                    "Communicate openly and honestly",
                    "Respect each other's differences",
                    "Find common ground in shared interests"
                )
            )
            
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Generate Tarot Reading
    suspend fun getTarotReading(question: String): Result<TarotReading> = withContext(Dispatchers.IO) {
        try {
            val prompt = """
                Perform a 3-card tarot reading for the question: "$question"
                
                Draw 3 cards representing:
                1. Past
                2. Present
                3. Future
                
                Provide the card names, their meanings, and an overall interpretation with advice.
                Format as JSON with keys: cards (array with name, position, meaning), interpretation, advice
            """.trimIndent()
            
            val request = PollinationRequest(
                model = "openai",
                messages = listOf(
                    MessageRequest("system", "You are an experienced tarot card reader providing insightful readings."),
                    MessageRequest("user", prompt)
                ),
                temperature = 1.0f
            )
            
            val response = aiService.generateText(request)
            val content = response.choices.firstOrNull()?.message?.content ?: ""
            
            val reading = TarotReading(
                cards = listOf(
                    TarotCard("The Fool", "Past", "New beginnings and innocence"),
                    TarotCard("The Magician", "Present", "Manifestation and power"),
                    TarotCard("The High Priestess", "Future", "Intuition and mystery")
                ),
                interpretation = content,
                advice = "Trust your intuition and embrace the journey ahead."
            )
            
            Result.success(reading)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Get Moon Phase Insights
    suspend fun getMoonPhaseInsights(): Result<MoonPhase> = withContext(Dispatchers.IO) {
        try {
            val currentDate = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date())
            val prompt = """
                Provide insights about the current moon phase for $currentDate.
                Include:
                1. Current moon phase name
                2. Astrological significance
                3. Recommendations for activities during this phase
                4. Emotional and spiritual guidance
                
                Format as JSON with keys: phase, insights, recommendations (array)
            """.trimIndent()
            
            val request = PollinationRequest(
                model = "openai",
                messages = listOf(
                    MessageRequest("system", "You are an expert in lunar astrology and moon phase interpretations."),
                    MessageRequest("user", prompt)
                ),
                temperature = 1.0f
            )
            
            val response = aiService.generateText(request)
            val content = response.choices.firstOrNull()?.message?.content ?: ""
            
            val moonPhase = MoonPhase(
                phase = "Waxing Gibbous",
                date = currentDate,
                illumination = 0.75f,
                insights = content,
                recommendations = listOf(
                    "Set intentions for growth",
                    "Focus on building momentum",
                    "Practice gratitude"
                )
            )
            
            Result.success(moonPhase)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Generate Lucky Numbers
    suspend fun getLuckyNumbers(sign: ZodiacSign): Result<LuckyNumbers> = withContext(Dispatchers.IO) {
        try {
            val prompt = """
                Generate 6 lucky numbers for ${sign.displayName} for today.
                Explain the numerological significance of these numbers.
            """.trimIndent()
            
            val request = PollinationRequest(
                model = "openai",
                messages = listOf(
                    MessageRequest("system", "You are a numerology expert specializing in lucky numbers and their meanings."),
                    MessageRequest("user", prompt)
                ),
                temperature = 1.0f
            )
            
            val response = aiService.generateText(request)
            val content = response.choices.firstOrNull()?.message?.content ?: ""
            
            val luckyNumbers = LuckyNumbers(
                numbers = List(6) { (1..99).random() }.distinct().take(6),
                explanation = content
            )
            
            Result.success(luckyNumbers)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // AI Chat
    suspend fun chatWithAI(userMessage: String, conversationHistory: List<MessageRequest> = emptyList()): Result<String> = withContext(Dispatchers.IO) {
        try {
            val messages = mutableListOf<MessageRequest>()
            messages.add(MessageRequest("system", "You are an expert astrologer and spiritual guide. Provide insightful, compassionate, and accurate astrological guidance."))
            messages.addAll(conversationHistory)
            messages.add(MessageRequest("user", userMessage))
            
            val request = PollinationRequest(
                model = "openai",
                messages = messages,
                temperature = 1.0f
            )
            
            val response = aiService.generateText(request)
            val content = response.choices.firstOrNull()?.message?.content ?: "I'm sorry, I couldn't generate a response."
            
            Result.success(content)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
