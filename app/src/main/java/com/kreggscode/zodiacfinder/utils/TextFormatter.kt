package com.kreggscode.zodiacfinder.utils

object TextFormatter {
    /**
     * Cleans AI-generated text by removing JSON artifacts, code markers, and formatting issues
     */
    fun cleanAIText(text: String): String {
        return text
            // Remove JSON structure
            .replace(Regex("""[\{\}]"""), "")
            .replace(Regex(""""[\w]+"\s*:\s*"""), "")
            // Remove escaped quotes
            .replace("\\\"", "\"")
            .replace("\\n", "\n")
            // Remove markdown headers that are just ###
            .replace(Regex("""^#{1,6}\s*""", RegexOption.MULTILINE), "")
            // Remove asterisks for bold
            .replace("**", "")
            // Remove code blocks
            .replace("```", "")
            // Clean up extra whitespace
            .replace(Regex("""\n{3,}"""), "\n\n")
            .trim()
    }
    
    /**
     * Parses horoscope JSON response into structured data
     */
    fun parseHoroscopeResponse(response: String): Map<String, String> {
        val result = mutableMapOf<String, String>()
        
        try {
            // Try to extract JSON-like content
            val sections = listOf("overall", "love", "career", "finance", "health", "luckyColor", "luckyNumber")
            
            sections.forEach { section ->
                val pattern = """"$section"\s*:\s*"([^"]*?)"""".toRegex(RegexOption.DOT_MATCHES_ALL)
                val match = pattern.find(response)
                match?.let {
                    val value = it.groupValues[1]
                        .replace("\\n", "\n")
                        .replace("\\\"", "\"")
                        .trim()
                    result[section] = value
                }
            }
            
            // If no JSON structure found, try to parse plain text
            if (result.isEmpty()) {
                val cleanText = cleanAIText(response)
                result["overall"] = cleanText
            }
            
        } catch (e: Exception) {
            result["overall"] = cleanAIText(response)
        }
        
        return result
    }
    
    /**
     * Formats text with proper line breaks and removes excessive spacing
     */
    fun formatWithLineBreaks(text: String): String {
        return text
            .split("\n")
            .filter { it.isNotBlank() }
            .joinToString("\n\n") { it.trim() }
    }
    
    /**
     * Extracts bullet points from text
     */
    fun extractBulletPoints(text: String): List<String> {
        return text
            .split("\n")
            .filter { it.trim().startsWith("•") || it.trim().startsWith("-") || it.trim().startsWith("*") }
            .map { it.trim().removePrefix("•").removePrefix("-").removePrefix("*").trim() }
            .filter { it.isNotEmpty() }
    }
}
