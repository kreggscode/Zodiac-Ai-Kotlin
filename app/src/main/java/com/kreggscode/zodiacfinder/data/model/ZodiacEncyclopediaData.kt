package com.kreggscode.zodiacfinder.data.model

data class DetailedZodiacInfo(
    val sign: ZodiacSign,
    val overview: String,
    val personality: String,
    val strengths: List<String>,
    val weaknesses: List<String>,
    val loveCompatibility: String,
    val careerPaths: List<String>,
    val healthTendencies: String,
    val lifePurpose: String,
    val famousPeople: List<String>,
    val mythsAndLegends: String,
    val luckyThings: LuckyThings
)

data class LuckyThings(
    val numbers: List<Int>,
    val colors: List<String>,
    val days: List<String>,
    val gemstones: List<String>
)

object ZodiacEncyclopediaData {
    fun getDetailedInfo(sign: ZodiacSign): DetailedZodiacInfo {
        return when (sign) {
            ZodiacSign.ARIES -> DetailedZodiacInfo(
                sign = ZodiacSign.ARIES,
                overview = "Aries, the first sign of the zodiac, embodies the pure energy of beginning, courage, and pioneering spirit. Ruled by Mars, the warrior planet, Aries individuals are natural-born leaders who fearlessly charge into new territories. They represent the raw, untamed energy of spring, bringing fresh starts and bold initiatives wherever they go.",
                personality = "Aries personalities are characterized by an infectious enthusiasm and boundless energy that can light up any room. They are direct, honest, and refreshingly straightforward in their approach to life. Their competitive nature drives them to excel, but it's never just about winning—it's about the thrill of the challenge itself. Aries individuals possess a childlike wonder and authenticity that makes them magnetic to others. They live in the moment, making spontaneous decisions that others might overthink. Their impatience can sometimes work against them, but it also means they don't waste time on things that don't excite them. Beneath their bold exterior lies a generous heart that will fiercely protect loved ones.",
                strengths = listOf(
                    "Courageous and confident in facing challenges",
                    "Natural leadership abilities and initiative",
                    "Honest and direct communication style",
                    "Passionate and enthusiastic about pursuits",
                    "Independent and self-reliant nature",
                    "Optimistic outlook even in adversity",
                    "Quick thinking and decisive action",
                    "Generous and protective of loved ones"
                ),
                weaknesses = listOf(
                    "Impulsive decision-making without considering consequences",
                    "Short temper and aggressive when frustrated",
                    "Impatient with slow processes or people",
                    "Can be self-centered and insensitive to others",
                    "Difficulty completing projects once initial excitement fades",
                    "Tendency to rush into situations without planning",
                    "Stubborn and resistant to others' advice"
                ),
                loveCompatibility = "Aries thrives in relationships that offer excitement, passion, and intellectual stimulation. They need a partner who can match their energy and won't be intimidated by their strong personality. Best matches include Leo and Sagittarius (fellow fire signs who understand their passion), Gemini and Aquarius (air signs that provide mental stimulation), and surprisingly, Libra (their opposite sign, creating magnetic attraction). Aries shows love through action and grand gestures rather than words. They're fiercely loyal once committed but need space to maintain their independence. The ideal partner appreciates their spontaneity and joins them in adventures rather than trying to tame their wild spirit.",
                careerPaths = listOf(
                    "Entrepreneur and Business Owner",
                    "Military or Law Enforcement Officer",
                    "Professional Athlete or Sports Coach",
                    "Emergency Services (Firefighter, Paramedic)",
                    "Sales Executive or Motivational Speaker",
                    "Surgeon or Emergency Medicine Doctor",
                    "Competitive Professional (Lawyer, Stock Trader)",
                    "Adventure Tourism Guide or Stunt Performer"
                ),
                healthTendencies = "Aries rules the head and face, making them prone to headaches, migraines, and sinus issues. Their high-energy lifestyle can lead to burnout if they don't learn to pace themselves. Stress often manifests as tension headaches or jaw clenching. They're prone to accidents due to their impulsive nature and should be cautious during physical activities. Regular exercise is essential for Aries to channel their abundant energy constructively. High-intensity workouts like boxing, running, or competitive sports suit them well. They should watch their temper as anger can spike blood pressure. Adequate sleep is crucial but often neglected. Prevention focus: stress management techniques, regular health check-ups, and learning to slow down occasionally.",
                lifePurpose = "Aries is here to teach the world about courage, initiative, and the power of beginning. Their purpose is to break new ground, challenge the status quo, and inspire others to take action on their dreams. They're meant to be pioneers in their chosen fields, whether in business, sports, arts, or social causes. Aries teaches us that failure is not the end but part of the journey toward success. Their life lesson is to balance their fierce independence with consideration for others, to channel their warrior energy into causes greater than themselves, and to develop patience alongside their natural spontaneity. When evolved, Aries becomes the hero who fights not just for themselves but for those who cannot fight for themselves.",
                famousPeople = listOf(
                    "Leonardo da Vinci - Renaissance genius embodying Aries creativity",
                    "Lady Gaga - Bold innovator in music and fashion",
                    "Robert Downey Jr. - Charismatic comeback king",
                    "Emma Watson - Fearless advocate for women's rights",
                    "Vincent van Gogh - Passionate artistic pioneer"
                ),
                mythsAndLegends = "Aries is associated with the Golden Ram from Greek mythology, whose fleece became the object of Jason's quest. According to legend, the ram saved two children, Phrixus and Helle, by flying them to safety. In gratitude, Phrixus sacrificed the ram to Zeus, who placed it among the stars as the constellation Aries. The Golden Fleece became a symbol of authority and kingship. In Egyptian mythology, Aries is linked to Amon-Ra, the ram-headed sun god representing creative power. The ram symbol appears across cultures as representing leadership, sacrifice, and new beginnings—perfectly capturing the Aries essence of courageous action for the greater good.",
                luckyThings = LuckyThings(
                    numbers = listOf(1, 9, 19, 55),
                    colors = listOf("Red", "Scarlet", "Crimson", "White"),
                    days = listOf("Tuesday", "Saturday"),
                    gemstones = listOf("Diamond", "Ruby", "Bloodstone", "Red Jasper")
                )
            )
            // Add more signs... (showing pattern for brevity)
            else -> getDefaultInfo(sign)
        }
    }
    
    private fun getDefaultInfo(sign: ZodiacSign): DetailedZodiacInfo {
        return DetailedZodiacInfo(
            sign = sign,
            overview = "Detailed information coming soon for ${sign.displayName}.",
            personality = "Comprehensive personality analysis for ${sign.displayName} is being prepared.",
            strengths = listOf("To be detailed"),
            weaknesses = listOf("To be detailed"),
            loveCompatibility = "Love compatibility details coming soon.",
            careerPaths = listOf("To be detailed"),
            healthTendencies = "Health information coming soon.",
            lifePurpose = "Life purpose details coming soon.",
            famousPeople = listOf(),
            mythsAndLegends = "Mythological connections coming soon.",
            luckyThings = LuckyThings(
                numbers = listOf(1, 2, 3),
                colors = listOf("TBD"),
                days = listOf("TBD"),
                gemstones = listOf("TBD")
            )
        )
    }
}
