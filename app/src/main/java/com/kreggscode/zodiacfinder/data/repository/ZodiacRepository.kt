package com.kreggscode.zodiacfinder.data.repository

import com.kreggscode.zodiacfinder.data.api.MessageRequest
import com.kreggscode.zodiacfinder.data.api.PollinationAIService
import com.kreggscode.zodiacfinder.data.api.PollinationRequest
import com.kreggscode.zodiacfinder.data.model.*
import com.kreggscode.zodiacfinder.utils.TextFormatter
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
            
            // Parse the AI response and clean formatting
            val parsed = TextFormatter.parseHoroscopeResponse(content)
            
            val horoscope = DailyHoroscope(
                sign = sign,
                date = currentDate,
                overall = TextFormatter.cleanAIText(parsed["overall"] ?: ""),
                love = TextFormatter.cleanAIText(parsed["love"] ?: ""),
                career = TextFormatter.cleanAIText(parsed["career"] ?: ""),
                finance = TextFormatter.cleanAIText(parsed["finance"] ?: ""),
                health = TextFormatter.cleanAIText(parsed["health"] ?: ""),
                luck = TextFormatter.cleanAIText(parsed["luck"] ?: ""),
                luckyNumber = parsed["luckyNumber"]?.toIntOrNull() ?: (1..9).random(),
                luckyColor = parsed["luckyColor"] ?: listOf("Red", "Blue", "Green", "Purple", "Gold", "Silver").random()
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
            
            // Parse JSON response
            val jsonStart = content.indexOf("{")
            val jsonEnd = content.lastIndexOf("}") + 1
            
            if (jsonStart >= 0 && jsonEnd > jsonStart) {
                val jsonString = content.substring(jsonStart, jsonEnd)
                val jsonObject = org.json.JSONObject(jsonString)
                
                val result = CompatibilityResult(
                    sign1 = sign1,
                    sign2 = sign2,
                    loveScore = jsonObject.optInt("loveScore", 70),
                    marriageScore = jsonObject.optInt("marriageScore", 65),
                    careerScore = jsonObject.optInt("careerScore", 75),
                    friendshipScore = jsonObject.optInt("friendshipScore", 70),
                    overallScore = jsonObject.optInt("overallScore", 70),
                    insights = jsonObject.optString("insights", "A unique cosmic connection with potential for growth."),
                    suggestions = try {
                        val suggestionsArray = jsonObject.getJSONArray("suggestions")
                        List(suggestionsArray.length()) { i ->
                            suggestionsArray.getString(i)
                        }
                    } catch (e: Exception) {
                        listOf(
                            "Communicate openly and honestly",
                            "Respect each other's differences",
                            "Find common ground in shared interests"
                        )
                    }
                )
                return@withContext Result.success(result)
            }
            
            // Fallback if JSON parsing fails
            val result = CompatibilityResult(
                sign1 = sign1,
                sign2 = sign2,
                loveScore = (60..95).random(),
                marriageScore = (55..90).random(),
                careerScore = (50..85).random(),
                friendshipScore = (65..95).random(),
                overallScore = (60..90).random(),
                insights = "A unique cosmic connection between ${sign1.displayName} and ${sign2.displayName}. Both signs bring different energies that can complement each other beautifully when balanced.",
                suggestions = listOf(
                    "Communicate openly and honestly about your needs",
                    "Respect each other's unique qualities and differences",
                    "Find common ground in shared interests and goals",
                    "Support each other's personal growth and aspirations"
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
                    TarotCardData("The Fool", "Past", "New beginnings and innocence"),
                    TarotCardData("The Magician", "Present", "Manifestation and power"),
                    TarotCardData("The High Priestess", "Future", "Intuition and mystery")
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
    suspend fun getMoonPhaseInsights(): Result<MoonPhaseData> = withContext(Dispatchers.IO) {
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
            
            val moonPhase = MoonPhaseData(
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
            messages.add(MessageRequest("system", """You are an expert astrologer and spiritual guide with deep knowledge of:
                - Western astrology (zodiac signs, houses, aspects, transits)
                - Vedic astrology and birth chart analysis
                - Tarot card reading and interpretation
                - Numerology and lucky numbers
                - Crystal healing and gemstone properties
                - Palm reading and palmistry
                - Moon phases and lunar astrology
                - Compatibility analysis
                
                Provide insightful, compassionate, detailed, and accurate astrological guidance. When analyzing birth charts, always consider:
                - Sun sign, Moon sign, and Rising sign
                - Planetary positions and aspects
                - House placements and their meanings
                - Current transits and their effects
                - Life path and karmic lessons
                
                Be specific, personalized, and helpful. Avoid generic responses.""".trimIndent()))
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
    
    // Generate Birth Chart Analysis with AI
    suspend fun generateBirthChartAnalysis(
        dateOfBirth: String,
        timeOfBirth: String,
        placeOfBirth: String,
        sunSign: ZodiacSign
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val prompt = """
                Perform a comprehensive birth chart analysis for someone born on:
                Date: $dateOfBirth
                Time: $timeOfBirth
                Place: $placeOfBirth
                Sun Sign: ${sunSign.displayName}
                
                Provide a detailed analysis covering:
                1. **Sun Sign Analysis**: Deep dive into their ${sunSign.displayName} nature, core personality, ego, and life purpose
                2. **Moon Sign Insights**: Emotional nature, inner world, subconscious patterns, and what they need for emotional security
                3. **Rising Sign (Ascendant)**: How they present themselves to the world, first impressions, physical appearance tendencies
                4. **Planetary Positions**: Analyze the likely positions of Mercury (communication), Venus (love), Mars (action), Jupiter (expansion), Saturn (lessons)
                5. **Life Path & Purpose**: Their soul's journey, karmic lessons, and spiritual growth areas
                6. **Career & Talents**: Natural abilities, best career paths, and how to achieve success
                7. **Love & Relationships**: Romantic nature, what they seek in partners, relationship patterns
                8. **Challenges & Growth**: Areas of difficulty and how to overcome them
                9. **Current Life Phase**: Based on their age and planetary cycles
                10. **Practical Advice**: Specific recommendations for personal growth and fulfillment
                
                Be extremely detailed, specific, and insightful. This should be a comprehensive reading that feels personal and accurate.
            """.trimIndent()
            
            val request = PollinationRequest(
                model = "openai",
                messages = listOf(
                    MessageRequest("system", "You are a master astrologer with 30+ years of experience in birth chart analysis. Provide deeply insightful, accurate, and personalized readings."),
                    MessageRequest("user", prompt)
                ),
                temperature = 1.0f
            )
            
            val response = aiService.generateText(request)
            val content = response.choices.firstOrNull()?.message?.content ?: "Unable to generate birth chart analysis."
            
            Result.success(content)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Generate Detailed Sections for Birth Chart
    suspend fun generateDetailedSections(
        dateOfBirth: String,
        sunSign: ZodiacSign
    ): Result<Map<String, String>> = withContext(Dispatchers.IO) {
        try {
            val prompt = """
                Generate detailed astrological insights for someone born on $dateOfBirth (${sunSign.displayName}).
                
                Provide 5 separate detailed sections (each 150-200 words):
                
                1. LOVE_AND_RELATIONSHIPS: Their romantic nature, ideal partner traits, relationship patterns, how they express love, what they need in relationships, dating advice
                
                2. CAREER_AND_FINANCE: Best career paths, natural talents, work style, financial habits, how to achieve success, money management tips
                
                3. HEALTH_AND_WELLNESS: Physical health tendencies, mental health needs, stress management, ideal exercise, diet recommendations, wellness practices
                
                4. COMPATIBILITY_INSIGHTS: Which signs they're most compatible with (romantic and friendship), which signs challenge them, how to navigate different relationships
                
                5. FUTURE_GUIDANCE: Upcoming opportunities, potential challenges in next 6-12 months, areas of growth, what to focus on, cosmic timing advice
                
                Format as:
                LOVE_AND_RELATIONSHIPS: [content]
                CAREER_AND_FINANCE: [content]
                HEALTH_AND_WELLNESS: [content]
                COMPATIBILITY_INSIGHTS: [content]
                FUTURE_GUIDANCE: [content]
                
                Be specific, insightful, and practical.
            """.trimIndent()
            
            val request = PollinationRequest(
                model = "openai",
                messages = listOf(
                    MessageRequest("system", "You are an expert astrologer providing detailed, actionable insights. Be specific and practical."),
                    MessageRequest("user", prompt)
                ),
                temperature = 1.0f
            )
            
            val response = aiService.generateText(request)
            val content = response.choices.firstOrNull()?.message?.content ?: ""
            
            // Parse the sections
            val sections = mutableMapOf<String, String>()
            val lines = content.lines()
            var currentSection = ""
            var currentContent = StringBuilder()
            
            for (line in lines) {
                when {
                    line.startsWith("LOVE_AND_RELATIONSHIPS:") -> {
                        if (currentSection.isNotEmpty()) {
                            sections[currentSection] = currentContent.toString().trim()
                        }
                        currentSection = "love"
                        currentContent = StringBuilder(line.substringAfter(":").trim())
                    }
                    line.startsWith("CAREER_AND_FINANCE:") -> {
                        if (currentSection.isNotEmpty()) {
                            sections[currentSection] = currentContent.toString().trim()
                        }
                        currentSection = "career"
                        currentContent = StringBuilder(line.substringAfter(":").trim())
                    }
                    line.startsWith("HEALTH_AND_WELLNESS:") -> {
                        if (currentSection.isNotEmpty()) {
                            sections[currentSection] = currentContent.toString().trim()
                        }
                        currentSection = "health"
                        currentContent = StringBuilder(line.substringAfter(":").trim())
                    }
                    line.startsWith("COMPATIBILITY_INSIGHTS:") -> {
                        if (currentSection.isNotEmpty()) {
                            sections[currentSection] = currentContent.toString().trim()
                        }
                        currentSection = "compatibility"
                        currentContent = StringBuilder(line.substringAfter(":").trim())
                    }
                    line.startsWith("FUTURE_GUIDANCE:") -> {
                        if (currentSection.isNotEmpty()) {
                            sections[currentSection] = currentContent.toString().trim()
                        }
                        currentSection = "future"
                        currentContent = StringBuilder(line.substringAfter(":").trim())
                    }
                    currentSection.isNotEmpty() -> {
                        currentContent.append(" ").append(line.trim())
                    }
                }
            }
            
            // Add last section
            if (currentSection.isNotEmpty()) {
                sections[currentSection] = currentContent.toString().trim()
            }
            
            Result.success(sections)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Analyze Palm Reading from Image
    suspend fun analyzePalmReading(palmDescription: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val prompt = """
                I am analyzing a palm image for a palmistry reading. Based on typical palm characteristics, provide a comprehensive and personalized palm reading.
                
                Create a detailed palm reading analysis covering:
                
                **🖐️ HAND SHAPE & OVERALL IMPRESSION**
                Analyze the hand type (Earth, Air, Water, or Fire) and what this reveals about the person's fundamental nature, temperament, and approach to life.
                
                **❤️ HEART LINE (Emotions & Relationships)**
                Provide insights about:
                - Emotional nature and how they express feelings
                - Relationship patterns and romantic tendencies
                - Capacity for love and emotional connections
                - Compatibility with others
                
                **🧠 HEAD LINE (Intelligence & Mental Approach)**
                Analyze:
                - Thinking style (analytical, creative, practical)
                - Decision-making approach
                - Communication abilities
                - Learning style and intellectual pursuits
                
                **🌟 LIFE LINE (Vitality & Life Path)**
                Interpret:
                - Overall energy levels and vitality
                - Major life transitions and changes
                - Physical health indicators
                - Life journey and personal growth
                
                **🎯 FATE LINE (Career & Destiny)**
                Reveal:
                - Career path and professional direction
                - Life purpose and calling
                - Success potential and timing
                - Major career milestones
                
                **☀️ SUN LINE (Success & Recognition)**
                Discuss:
                - Potential for fame or recognition
                - Creative talents and artistic abilities
                - Public image and reputation
                - Achievement and fulfillment
                
                **💑 MARRIAGE LINES (Relationships)**
                Provide insights on:
                - Number and timing of significant relationships
                - Depth and quality of partnerships
                - Marriage potential and timing
                
                **⛰️ MOUNTS (Planetary Influences)**
                Analyze the prominent mounts and their meanings:
                - Mount of Venus (love, passion, vitality)
                - Mount of Jupiter (ambition, leadership)
                - Mount of Saturn (wisdom, responsibility)
                - Mount of Apollo (creativity, success)
                - Mount of Mercury (communication, business)
                - Mount of Mars (courage, energy)
                - Mount of Luna (imagination, intuition)
                
                **💪 STRENGTHS & TALENTS**
                Highlight the person's natural gifts and abilities
                
                **⚠️ CHALLENGES & AREAS FOR GROWTH**
                Identify potential obstacles and how to overcome them
                
                **🔮 FUTURE INSIGHTS**
                Provide guidance on:
                - Career opportunities
                - Relationship prospects
                - Health considerations
                - Personal development
                
                **✨ PRACTICAL ADVICE**
                Offer actionable guidance for using these insights for personal growth and success.
                
                Make this reading feel deeply personal, insightful, and empowering. Use specific details and avoid generic statements. The reading should be comprehensive (at least 500 words) and formatted with clear sections using emojis and bold headings.
            """.trimIndent()
            
            val request = PollinationRequest(
                model = "openai",
                messages = listOf(
                    MessageRequest("system", "You are Master Chiromancer Aria, a world-renowned palmist with 30 years of experience. You have an extraordinary gift for reading palms and providing deeply accurate, personalized insights. Your readings are known for being specific, detailed, and life-changing. You never ask for image descriptions - you provide complete readings based on your expertise."),
                    MessageRequest("user", prompt)
                ),
                temperature = 0.9f
            )
            
            val response = aiService.generateText(request)
            val content = response.choices.firstOrNull()?.message?.content ?: "Unable to analyze palm reading."
            
            Result.success(content)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Generate Profile Analysis from Birth Data
    suspend fun generateProfileAnalysis(
        name: String,
        dateOfBirth: String,
        timeOfBirth: String?,
        placeOfBirth: String?,
        zodiacSign: ZodiacSign
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val prompt = """
                Generate a comprehensive astrological profile analysis for:
                Name: $name
                Date of Birth: $dateOfBirth
                Time of Birth: ${timeOfBirth ?: "Not provided"}
                Place of Birth: ${placeOfBirth ?: "Not provided"}
                Sun Sign: ${zodiacSign.displayName}
                
                Provide a detailed analysis covering:
                1. **Core Personality**: Deep dive into their ${zodiacSign.displayName} nature
                2. **Strengths & Talents**: Natural abilities and gifts
                3. **Challenges & Growth Areas**: What they need to work on
                4. **Life Purpose**: Their soul's mission and karmic path
                5. **Career Guidance**: Best career paths and how to succeed
                6. **Relationship Insights**: Love style, compatibility, what they seek
                7. **Health & Wellness**: Physical and mental health tendencies
                8. **Lucky Elements**: Numbers, colors, gemstones, directions
                9. **Current Life Phase**: Based on age and planetary cycles
                10. **Personalized Advice**: Specific recommendations for growth
                
                Be extremely detailed, insightful, and make it feel personal and accurate.
            """.trimIndent()
            
            val request = PollinationRequest(
                model = "openai",
                messages = listOf(
                    MessageRequest("system", "You are a master astrologer providing comprehensive profile analysis. Be deeply insightful, accurate, and personalized."),
                    MessageRequest("user", prompt)
                ),
                temperature = 1.0f
            )
            
            val response = aiService.generateText(request)
            val content = response.choices.firstOrNull()?.message?.content ?: "Unable to generate profile analysis."
            
            Result.success(content)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
