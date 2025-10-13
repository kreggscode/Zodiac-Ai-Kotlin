package com.kreggscode.zodiacfinder.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kreggscode.zodiacfinder.ui.components.*
import com.kreggscode.zodiacfinder.ui.theme.*

data class Gemstone(
    val name: String,
    val emoji: String,
    val primaryColor: Color,
    val benefits: String,
    val zodiacSigns: List<String>,
    val chakra: String,
    val properties: String,
    val howToUse: String,
    val rarity: String
)

@Composable
fun GemstoneEncyclopediaScreen(
    onNavigateBack: () -> Unit = {}
) {
    val gemstones = remember { getGemstonesList() }
    var selectedGemstone by remember { mutableStateOf<Gemstone?>(null) }
    
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
        if (selectedGemstone == null) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    TopBarWithBack(
                        title = "💎 Gemstone Encyclopedia",
                        subtitle = "Discover the power of crystals",
                        onBackClick = onNavigateBack
                    )
                }
                
                items(gemstones) { gemstone ->
                    GemstoneCard(
                        gemstone = gemstone,
                        onClick = { selectedGemstone = gemstone }
                    )
                }
            }
        } else {
            GemstoneDetailScreen(
                gemstone = selectedGemstone!!,
                onBack = { selectedGemstone = null }
            )
        }
    }
}

@Composable
private fun GemstoneCard(
    gemstone: Gemstone,
    onClick: () -> Unit
) {
    GradientGlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        gradientColors = listOf(
            gemstone.primaryColor.copy(alpha = 0.3f),
            gemstone.primaryColor.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = gemstone.emoji,
                fontSize = 48.sp
            )
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = gemstone.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = gemstone.rarity,
                    fontSize = 13.sp,
                    color = gemstone.primaryColor,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Best for: ${gemstone.zodiacSigns.take(2).joinToString(", ")}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun GemstoneDetailScreen(
    gemstone: Gemstone,
    onBack: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            TopBarWithBack(
                title = "${gemstone.emoji} ${gemstone.name}",
                subtitle = gemstone.rarity,
                onBackClick = onBack
            )
        }
        
        // Overview
        item {
            GradientGlassCard(
                modifier = Modifier.fillMaxWidth(),
                gradientColors = listOf(
                    gemstone.primaryColor.copy(alpha = 0.4f),
                    gemstone.primaryColor.copy(alpha = 0.2f)
                )
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = gemstone.emoji,
                        fontSize = 100.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = gemstone.name,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = gemstone.rarity,
                        fontSize = 16.sp,
                        color = gemstone.primaryColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
        
        // Benefits
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "✨ Benefits & Healing Properties",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                    Text(
                        text = gemstone.benefits,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                        lineHeight = 24.sp
                    )
                }
            }
        }
        
        // Properties
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "🔮 Metaphysical Properties",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                    Text(
                        text = gemstone.properties,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                        lineHeight = 24.sp
                    )
                }
            }
        }
        
        // Chakra
        item {
            GradientGlassCard(
                modifier = Modifier.fillMaxWidth(),
                gradientColors = listOf(
                    MysticPurple.copy(alpha = 0.2f),
                    CosmicBlue.copy(alpha = 0.1f)
                )
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "🧘 Associated Chakra",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MysticPurple
                    )
                    Text(
                        text = gemstone.chakra,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                    )
                }
            }
        }
        
        // How to Use
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "💫 How to Use",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                    Text(
                        text = gemstone.howToUse,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                        lineHeight = 24.sp
                    )
                }
            }
        }
        
        // Best For Zodiac Signs
        item {
            GradientGlassCard(
                modifier = Modifier.fillMaxWidth(),
                gradientColors = listOf(
                    LuckyGold.copy(alpha = 0.2f),
                    LuckyGold.copy(alpha = 0.1f)
                )
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "⭐ Best for Zodiac Signs",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = LuckyGold
                    )
                    Text(
                        text = gemstone.zodiacSigns.joinToString(" • "),
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

private fun getGemstonesList(): List<Gemstone> {
    return listOf(
        Gemstone(
            name = "Diamond",
            emoji = "💎",
            primaryColor = Color(0xFFE8F4F8),
            benefits = "Diamond amplifies energy and brings clarity. Known as the 'king of gems', it enhances courage, invincibility, and fortitude. Diamonds strengthen relationships, promote purity of thought, and bring abundance. They are excellent for clearing emotional and mental pain.",
            zodiacSigns = listOf("Aries", "Leo", "Taurus"),
            chakra = "Crown Chakra - Connects you to divine consciousness and spiritual enlightenment",
            properties = "Diamond carries the highest vibration of all gemstones. It promotes truth, vision, and spiritual ecstasy. Amplifies the energy of other crystals and can be programmed for any purpose. Enhances brain function and strengthens all energy systems.",
            howToUse = "Wear as jewelry close to the skin, especially as a ring or necklace. Meditate with diamond to enhance spiritual awareness. Place on the crown chakra during energy work. Can be used in crystal grids for amplification.",
            rarity = "Precious • Very Rare"
        ),
        Gemstone(
            name = "Ruby",
            emoji = "❤️",
            primaryColor = Color(0xFFE30B5C),
            benefits = "Ruby ignites passion, enhances vitality, and brings prosperity. Known as the 'king of precious stones', it stimulates the heart chakra, encourages joy and spontaneity. Ruby protects against psychic attack and promotes positive dreams.",
            zodiacSigns = listOf("Leo", "Aries", "Scorpio", "Sagittarius"),
            chakra = "Heart Chakra & Root Chakra - Energizes life force and promotes love",
            properties = "Ruby amplifies energy levels and promotes courage. It banishes nightmares and encourages lucid dreaming. Enhances leadership qualities and increases passion in life. Stimulates circulation and detoxifies the body.",
            howToUse = "Wear as a ring on the ring finger or as a pendant near the heart. Meditate with ruby to boost confidence. Place under pillow for prophetic dreams. Use in manifestation rituals for prosperity.",
            rarity = "Precious • Rare"
        ),
        Gemstone(
            name = "Emerald",
            emoji = "💚",
            primaryColor = Color(0xFF50C878),
            benefits = "Emerald is the stone of successful love and domestic bliss. It brings loyalty, enhances unity, promotes friendship and balance. Known for its ability to bring emotional, physical and mental equilibrium. Emerald enhances psychic abilities and opens clairvoyance.",
            zodiacSigns = listOf("Taurus", "Gemini", "Virgo"),
            chakra = "Heart Chakra - Opens the heart to unconditional love and compassion",
            properties = "Emerald has the power to grant healing and vitality. It strengthens memory and imparts clarity of thought. Inspires deep inner knowing and promotes wisdom. Known to cure infections, heart problems, and eye issues.",
            howToUse = "Wear as a ring on the little finger or as a pendant. Meditate holding emerald for emotional healing. Place on the heart during chakra balancing. Keep in your workspace for mental clarity.",
            rarity = "Precious • Rare"
        ),
        Gemstone(
            name = "Blue Sapphire",
            emoji = "💙",
            primaryColor = Color(0xFF0F52BA),
            benefits = "Blue Sapphire brings mental clarity, spiritual insight, and prosperity. Known as the 'stone of wisdom', it releases mental tension, brings joy and peace of mind. Aligns the physical, mental and spiritual planes, restoring balance.",
            zodiacSigns = listOf("Virgo", "Libra", "Sagittarius", "Capricorn", "Aquarius"),
            chakra = "Third Eye & Throat Chakra - Enhances intuition and communication",
            properties = "Sapphire attracts prosperity and gifts of all kinds. Releases unwanted thoughts and brings discipline to the mind. Known for its protective qualities against negative energies. Enhances concentration and speeds up the fulfillment of ambitions.",
            howToUse = "Wear as a ring on the middle finger (recommended after astrological consultation). Meditate with sapphire for wisdom. Place on third eye during spiritual work. Keep in pocket for protection.",
            rarity = "Precious • Moderately Rare"
        ),
        Gemstone(
            name = "Amethyst",
            emoji = "💜",
            primaryColor = Color(0xFF9966CC),
            benefits = "Amethyst is a natural tranquilizer that relieves stress, soothes irritability, and balances mood swings. Activates spiritual awareness, opens intuition, and enhances psychic abilities. Known for overcoming addictions and blocking negative energies.",
            zodiacSigns = listOf("Aquarius", "Pisces", "Virgo", "Capricorn"),
            chakra = "Third Eye & Crown Chakra - Opens spiritual consciousness",
            properties = "Amethyst has strong healing and cleansing powers. Enhances memory and improves motivation. Encourages selflessness and spiritual wisdom. Protects against psychic attack and ill fortune. Promotes emotional centering.",
            howToUse = "Place under pillow for peaceful sleep and prophetic dreams. Wear as jewelry for daily protection. Meditate holding amethyst for spiritual connection. Use in crystal grids for manifestation.",
            rarity = "Semi-Precious • Common"
        ),
        Gemstone(
            name = "Pearl",
            emoji = "🤍",
            primaryColor = Color(0xFFFFFFF0),
            benefits = "Pearl signifies faith, charity, and innocence. Enhances personal integrity and helps focus attention. Known to soothe and heal negative emotions, brings peace and tranquility. Strengthens relationships and promotes marital bliss.",
            zodiacSigns = listOf("Cancer", "Pisces", "Gemini"),
            chakra = "Sacral & Heart Chakra - Balances emotions and promotes feminine energy",
            properties = "Pearl treats digestive disorders and soft organ conditions. Increases fertility and eases childbirth. Alleviates emotional stress and promotes wisdom through experience. Connected to the moon's calming energy.",
            howToUse = "Wear as a ring or pendant touching the skin. Best worn during full moon for maximum benefit. Keep in water overnight to recharge. Use during meditation for emotional balance.",
            rarity = "Precious • Moderately Rare"
        ),
        Gemstone(
            name = "Rose Quartz",
            emoji = "💗",
            primaryColor = Color(0xFFFF66CC),
            benefits = "Rose Quartz is the stone of universal love. Restores trust and harmony in relationships, encourages unconditional love. Purifies and opens the heart at all levels. Promotes self-love, friendship, deep inner healing, and peace.",
            zodiacSigns = listOf("Taurus", "Libra"),
            chakra = "Heart Chakra - Opens heart to all forms of love",
            properties = "Rose Quartz strengthens empathy and sensitivity. Helps accept necessary change. Excellent for trauma and crisis situations. Releases unexpressed emotions and heartache. Encourages self-forgiveness and acceptance.",
            howToUse = "Wear close to the heart as a pendant. Place in bedroom for harmonious relationships. Hold during self-love meditation. Use in bath water for emotional healing.",
            rarity = "Semi-Precious • Common"
        ),
        Gemstone(
            name = "Citrine",
            emoji = "💛",
            primaryColor = Color(0xFFFFD700),
            benefits = "Citrine is known as the 'merchant's stone' for its properties of increasing prosperity. Activates creativity and encourages self-expression. Enhances concentration and revitalizes the mind. Releases negative traits and promotes joy.",
            zodiacSigns = listOf("Gemini", "Leo", "Aries", "Libra"),
            chakra = "Solar Plexus & Sacral Chakra - Activates personal power and creativity",
            properties = "Citrine is a powerful cleanser and regenerator. Carries the power of the sun. Never needs cleansing. Attracts wealth, prosperity and success. Raises self-esteem and self-confidence. Promotes motivation and stimulates digestion.",
            howToUse = "Place in cash box or wallet to attract abundance. Wear as jewelry for confidence. Keep in workspace for productivity. Meditate with citrine for manifestation.",
            rarity = "Semi-Precious • Moderately Common"
        ),
        Gemstone(
            name = "Turquoise",
            emoji = "🩵",
            primaryColor = Color(0xFF40E0D0),
            benefits = "Turquoise is a purification stone. Dispels negative energy, provides protection against environmental pollutants. Balances and aligns all chakras, stabilizing mood swings. Excellent for exhaustion and panic attacks.",
            zodiacSigns = listOf("Sagittarius", "Aquarius", "Pisces", "Scorpio"),
            chakra = "Throat Chakra - Enhances communication and self-expression",
            properties = "Turquoise strengthens overall body and promotes absorption of nutrients. Solves problems, calms nerves. Enhances intuition and meditation. Known to change color to warn of danger or infidelity.",
            howToUse = "Wear as jewelry for protection. Hold during public speaking for confidence. Keep in car for safe travels. Place on throat during chakra work.",
            rarity = "Semi-Precious • Moderately Rare"
        ),
        Gemstone(
            name = "Black Tourmaline",
            emoji = "🖤",
            primaryColor = Color(0xFF28282B),
            benefits = "Black Tourmaline is one of the most powerful protection stones. Forms a shield against negative energies, electromagnetic pollution, and psychic attack. Grounds and balances, promotes clear thinking. Transforms negative thoughts into positive energy.",
            zodiacSigns = listOf("Capricorn", "Scorpio"),
            chakra = "Root Chakra - Grounds and protects",
            properties = "Tourmaline cleanses, purifies and transforms dense energy into lighter vibration. Grounds spiritual energy. Balances right-left hemispheres of brain. Helps understand oneself and others. Attracts inspiration and compassion.",
            howToUse = "Carry in pocket for daily protection. Place at corners of home for energy shield. Wear as jewelry for grounding. Keep near electronics to absorb EMF radiation.",
            rarity = "Semi-Precious • Common"
        ),
        Gemstone(
            name = "Opal",
            emoji = "🌈",
            primaryColor = Color(0xFFFFE5B4),
            benefits = "Opal amplifies emotions and releases inhibitions. Enhances cosmic consciousness and induces psychic and mystical visions. Stimulates originality and dynamic creativity. Brings loyalty, faithfulness and spontaneity.",
            zodiacSigns = listOf("Libra", "Cancer", "Pisces"),
            chakra = "All Chakras - Aligns and balances all energy centers",
            properties = "Opal is an absorbent and reflective stone. It picks up thoughts and feelings, amplifies them, and returns them. Strengthens memory and stimulates creativity. Associated with love, passion, desire and eroticism.",
            howToUse = "Wear as jewelry to enhance creativity. Meditate with opal for spiritual visions. Keep in creative workspace. Best worn by emotionally stable individuals.",
            rarity = "Precious • Rare"
        ),
        Gemstone(
            name = "Garnet",
            emoji = "🔴",
            primaryColor = Color(0xFF8B0000),
            benefits = "Garnet revitalizes, purifies and balances energy. Inspires love and devotion, balances sex drive and alleviates emotional disharmony. Activates and strengthens the survival instinct. Brings courage and hope.",
            zodiacSigns = listOf("Capricorn", "Aquarius", "Leo"),
            chakra = "Root & Heart Chakra - Grounds and energizes",
            properties = "Garnet regenerates the body and stimulates metabolism. Treats spinal and cellular disorders. Purifies blood, heart and lungs. Regenerates DNA. Assists in assimilation of minerals and vitamins.",
            howToUse = "Wear as jewelry for vitality. Place on root chakra during meditation. Keep in bedroom for passion. Use in manifestation rituals for success.",
            rarity = "Semi-Precious • Common"
        ),
        Gemstone(
            name = "Jade",
            emoji = "🟢",
            primaryColor = Color(0xFF00A86B),
            benefits = "Jade is a symbol of purity, serenity and nurturing. Attracts good luck and friendship. Stabilizes personality and promotes self-sufficiency. Releases negative thoughts and soothes the mind.",
            zodiacSigns = listOf("Taurus", "Libra", "Aries"),
            chakra = "Heart Chakra - Promotes love and healing",
            properties = "Jade is a protective stone that keeps the wearer from harm. Brings harmony and attracts good luck. Stabilizes personality and integrates mind with body. Promotes self-sufficiency. Cleanses organs and aids fertility.",
            howToUse = "Wear as jewelry for protection. Place under pillow for insightful dreams. Hold during meditation for peace. Keep in garden for plant growth.",
            rarity = "Semi-Precious • Moderately Rare"
        ),
        Gemstone(
            name = "Lapis Lazuli",
            emoji = "🔵",
            primaryColor = Color(0xFF1E3A8A),
            benefits = "Lapis Lazuli opens the third eye and balances the throat chakra. Stimulates enlightenment and enhances dream work. Quickly releases stress and brings deep peace. Possesses enormous serenity.",
            zodiacSigns = listOf("Sagittarius", "Libra", "Capricorn"),
            chakra = "Third Eye & Throat Chakra - Enhances wisdom and truth",
            properties = "Lapis Lazuli is a powerful thought amplifier. Stimulates higher mental faculties and encourages creativity. Bonds relationships in love and friendship. Reveals inner truth and promotes self-awareness.",
            howToUse = "Wear as pendant near throat for communication. Meditate with lapis for spiritual insight. Place on third eye during energy work. Keep on desk for mental clarity.",
            rarity = "Semi-Precious • Moderately Rare"
        ),
        Gemstone(
            name = "Moonstone",
            emoji = "🌙",
            primaryColor = Color(0xFFF0F8FF),
            benefits = "Moonstone is a stone of new beginnings. Strongly connected to moon and intuition. Makes conscious the unconscious. Promotes intuition and empathy. Encourages lucid dreaming, especially at full moon.",
            zodiacSigns = listOf("Cancer", "Libra", "Scorpio"),
            chakra = "Sacral & Third Eye Chakra - Enhances intuition and feminine energy",
            properties = "Moonstone calms emotional instability and stress. Improves emotional intelligence. Stabilizes emotions and provides calmness. Enhances psychic abilities and clairvoyance. Promotes inspiration and success.",
            howToUse = "Wear during full moon for maximum power. Place under pillow for prophetic dreams. Meditate with moonstone for intuition. Keep in water to recharge.",
            rarity = "Semi-Precious • Common"
        ),
        Gemstone(
            name = "Aquamarine",
            emoji = "💠",
            primaryColor = Color(0xFF7FFFD4),
            benefits = "Aquamarine is a stone of courage. Sharpens intuition and opens clairvoyance. Shields the aura and aligns the chakras. Calms the mind and reduces stress. Clarifies perception and clears confusion.",
            zodiacSigns = listOf("Pisces", "Aries", "Gemini"),
            chakra = "Throat Chakra - Enhances communication and truth",
            properties = "Aquamarine is useful for closure on all levels. Clears blocked communication and promotes self-expression. Soothes fears and increases sensitivity. Sharpens intuition and opens clairvoyance.",
            howToUse = "Wear as jewelry for courage. Hold during public speaking. Meditate with aquamarine for clarity. Place in bath water for emotional healing.",
            rarity = "Semi-Precious • Moderately Rare"
        ),
        Gemstone(
            name = "Peridot",
            emoji = "🟡",
            primaryColor = Color(0xFF9ACD32),
            benefits = "Peridot is a powerful cleanser. Releases and neutralizes toxins on all levels. Alleviates jealousy, resentment, spite and anger. Reduces stress and sharpens the mind. Opens and cleanses the heart.",
            zodiacSigns = listOf("Leo", "Virgo", "Scorpio"),
            chakra = "Heart & Solar Plexus Chakra - Promotes abundance and wellbeing",
            properties = "Peridot strengthens metabolism and benefits skin. Aids the heart, thymus, lungs, gallbladder, spleen and intestinal tract. Treats ulcers and strengthens eyes. Balances bipolar disorders.",
            howToUse = "Wear as jewelry for protection. Place on solar plexus for healing. Meditate with peridot for abundance. Keep in wallet to attract wealth.",
            rarity = "Semi-Precious • Moderately Common"
        ),
        Gemstone(
            name = "Topaz",
            emoji = "🟠",
            primaryColor = Color(0xFFFFD700),
            benefits = "Topaz soothes, heals, stimulates, recharges, remotivates and aligns the meridians. Promotes truth and forgiveness. Brings joy, generosity, abundance and good health. Known as stone of love and good fortune.",
            zodiacSigns = listOf("Sagittarius", "Scorpio", "Leo"),
            chakra = "Solar Plexus & Throat Chakra - Enhances confidence and communication",
            properties = "Topaz aids digestion and combats eating disorders. Fortifies nerves and stimulates metabolism. Releases tension and promotes relaxation. Stabilizes emotions and makes you receptive to love.",
            howToUse = "Wear as jewelry for confidence. Meditate with topaz for manifestation. Place on solar plexus for healing. Keep in workspace for success.",
            rarity = "Semi-Precious • Moderately Rare"
        ),
        Gemstone(
            name = "Onyx",
            emoji = "⚫",
            primaryColor = Color(0xFF0A0A0A),
            benefits = "Onyx gives strength and promotes vigor, steadfastness and stamina. Imparts self-confidence and helps you be at ease in surroundings. Banishes grief and enhances self-control. Encourages happiness and good fortune.",
            zodiacSigns = listOf("Leo", "Capricorn", "Sagittarius"),
            chakra = "Root Chakra - Provides grounding and protection",
            properties = "Onyx treats disorders of bones, bone marrow and blood. Strengthens teeth and supports feet. Beneficial for ailments of legs, ankles and feet. Heals old grief and sorrows.",
            howToUse = "Carry for protection and strength. Wear as jewelry for grounding. Place in home for stability. Meditate with onyx for inner strength.",
            rarity = "Semi-Precious • Common"
        ),
        Gemstone(
            name = "Carnelian",
            emoji = "🧡",
            primaryColor = Color(0xFFFF6347),
            benefits = "Carnelian restores vitality and motivation. Stimulates creativity and gives courage. Promotes positive life choices and dispels apathy. Sharpens concentration and dispels mental lethargy.",
            zodiacSigns = listOf("Virgo", "Taurus", "Cancer"),
            chakra = "Sacral & Root Chakra - Enhances creativity and vitality",
            properties = "Carnelian improves absorption of vitamins and minerals. Ensures good blood supply to organs and tissues. Influences female reproductive organs. Overcomes frigidity and impotence.",
            howToUse = "Wear as jewelry for motivation. Place on sacral chakra for creativity. Keep in workspace for productivity. Meditate with carnelian for courage.",
            rarity = "Semi-Precious • Common"
        ),
        Gemstone(
            name = "Malachite",
            emoji = "🟩",
            primaryColor = Color(0xFF0BDA51),
            benefits = "Malachite is a stone of transformation. Absorbs negative energies and pollutants from atmosphere and body. Guards against radiation. Clears and activates chakras. Encourages risk-taking and change.",
            zodiacSigns = listOf("Scorpio", "Capricorn"),
            chakra = "Heart & Throat Chakra - Promotes emotional healing",
            properties = "Malachite amplifies energies. Grounds spiritual energies onto planet. Believed to protect against evil eye. Treats asthma, arthritis, epilepsy, fractures, swollen joints, tumors.",
            howToUse = "Wear with caution (can be toxic). Place in room for protection. Use in healing layouts. Keep away from water and acids.",
            rarity = "Semi-Precious • Moderately Rare"
        ),
        Gemstone(
            name = "Aventurine",
            emoji = "🍀",
            primaryColor = Color(0xFF4CBB17),
            benefits = "Aventurine is a stone of prosperity. Reinforces leadership qualities and decisiveness. Promotes compassion and empathy. Encourages perseverance. Stabilizes one's state of mind and stimulates perception.",
            zodiacSigns = listOf("Aries", "Leo"),
            chakra = "Heart Chakra - Attracts luck and abundance",
            properties = "Aventurine benefits thymus gland and connective tissue. Stabilizes blood pressure and stimulates metabolism. Has anti-inflammatory effect. Relieves skin eruptions and allergies.",
            howToUse = "Carry for good luck. Wear as jewelry for prosperity. Place in garden for plant growth. Meditate with aventurine for abundance.",
            rarity = "Semi-Precious • Common"
        ),
        Gemstone(
            name = "Labradorite",
            emoji = "✨",
            primaryColor = Color(0xFF4A5568),
            benefits = "Labradorite is a stone of transformation and magic. Raises consciousness and deflects unwanted energies. Strengthens intuition and psychic abilities. Banishes fears and insecurities. Stimulates imagination.",
            zodiacSigns = listOf("Leo", "Scorpio", "Sagittarius"),
            chakra = "Third Eye & Crown Chakra - Enhances mystical abilities",
            properties = "Labradorite treats disorders of eyes and brain. Stimulates mental acuity and relieves anxiety and stress. Regulates metabolism and balances hormones. Lowers blood pressure.",
            howToUse = "Wear for psychic protection. Meditate with labradorite for visions. Place on third eye for awakening. Keep in workspace for creativity.",
            rarity = "Semi-Precious • Moderately Common"
        ),
        Gemstone(
            name = "Obsidian",
            emoji = "🌑",
            primaryColor = Color(0xFF000000),
            benefits = "Obsidian is a highly protective stone. Forms a shield against negativity. Provides grounding and brings clarity to mind. Dissolves emotional blockages and ancient traumas. Promotes qualities of compassion and strength.",
            zodiacSigns = listOf("Scorpio", "Sagittarius"),
            chakra = "Root Chakra - Provides powerful grounding",
            properties = "Obsidian aids digestion and detoxifies. Reduces arthritis pain and joint problems. Warms extremities. Blocks psychic attack and removes negative spiritual influences.",
            howToUse = "Carry for protection. Place at home entrance for shielding. Meditate with obsidian for shadow work. Use in scrying for divination.",
            rarity = "Semi-Precious • Common"
        ),
        Gemstone(
            name = "Hematite",
            emoji = "⚙️",
            primaryColor = Color(0xFF808080),
            benefits = "Hematite is a grounding and protecting stone. Harmonizes mind, body and spirit. Dissolves negativity and prevents negative energies from entering aura. Boosts self-esteem and confidence.",
            zodiacSigns = listOf("Aries", "Aquarius"),
            chakra = "Root Chakra - Grounds and protects",
            properties = "Hematite stimulates concentration and focus. Enhances memory and original thought. Treats blood disorders and supports kidneys. Regenerates tissue and aids iron absorption.",
            howToUse = "Carry for grounding. Wear as jewelry for protection. Place on root chakra during meditation. Keep in workspace for focus.",
            rarity = "Semi-Precious • Common"
        ),
        Gemstone(
            name = "Fluorite",
            emoji = "🌈",
            primaryColor = Color(0xFF9370DB),
            benefits = "Fluorite absorbs and neutralizes negative energy and stress. Increases concentration and self-confidence. Encourages positivity and balances energies. Improves balance and coordination.",
            zodiacSigns = listOf("Pisces", "Capricorn"),
            chakra = "Third Eye & Heart Chakra - Enhances mental clarity",
            properties = "Fluorite strengthens bone tissue and alleviates arthritis and spinal injuries. Improves skin and mucous membranes. Beneficial for colds, flu and sinusitis. Heals ulcers and wounds.",
            howToUse = "Keep on desk for mental clarity. Wear as jewelry for focus. Meditate with fluorite for organization. Place in study area for learning.",
            rarity = "Semi-Precious • Common"
        ),
        Gemstone(
            name = "Tanzanite",
            emoji = "💎",
            primaryColor = Color(0xFF4B0082),
            benefits = "Tanzanite facilitates spiritual awareness and stimulates insight. Promotes compassion and calms overactive mind. Dissolves old patterns and opens way for rebirth. Enhances healing at all levels.",
            zodiacSigns = listOf("Sagittarius", "Gemini", "Libra"),
            chakra = "Third Eye, Throat & Crown Chakra - Enhances spiritual connection",
            properties = "Tanzanite strengthens immune system and detoxifies blood. Regenerates cells and treats heart, spleen, pancreas and lungs. Beneficial for skin and hair.",
            howToUse = "Wear as jewelry for spiritual growth. Meditate with tanzanite for insight. Place on third eye for visions. Keep for meditation practice.",
            rarity = "Precious • Very Rare"
        ),
        Gemstone(
            name = "Amber",
            emoji = "🟡",
            primaryColor = Color(0xFFFFBF00),
            benefits = "Amber is a powerful healer and cleanser. Absorbs negative energies and transmutes them into positive forces. Stimulates intellect and promotes self-confidence. Brings wisdom, balance and patience.",
            zodiacSigns = listOf("Leo", "Aquarius"),
            chakra = "Solar Plexus & Throat Chakra - Cleanses and energizes",
            properties = "Amber treats throat, stomach, spleen, kidneys, bladder, liver and gallbladder. Strengthens mucus membranes. Alleviates joint problems. Beneficial for memory and decision-making.",
            howToUse = "Wear as jewelry for healing. Place on affected area for pain relief. Meditate with amber for wisdom. Keep for babies during teething.",
            rarity = "Organic • Moderately Rare"
        ),
        Gemstone(
            name = "Coral",
            emoji = "🪸",
            primaryColor = Color(0xFFFF7F50),
            benefits = "Coral facilitates intuition, imagination and visualization. Expedites transfer of knowledge. Strengthens circulatory system and bones. Stimulates tissue regeneration and nourishes blood cells.",
            zodiacSigns = listOf("Taurus", "Pisces"),
            chakra = "Root Chakra - Grounds and protects",
            properties = "Coral treats disorders of spinal canal, alimentary canal, nervous system and thalamus. Strengthens skeleton and bone structure. Treats blood and bladder disorders.",
            howToUse = "Wear as jewelry for protection. Keep in home for harmony. Meditate with coral for grounding. Place in water to recharge.",
            rarity = "Organic • Rare"
        ),
        Gemstone(
            name = "Kunzite",
            emoji = "💗",
            primaryColor = Color(0xFFFFB6C1),
            benefits = "Kunzite is a stone of emotion. Connects heart with mind and encourages communication between the two. Induces deep meditative state. Beneficial for those who find hard to enter meditation.",
            zodiacSigns = listOf("Taurus", "Leo", "Scorpio"),
            chakra = "Heart Chakra - Opens heart to love",
            properties = "Kunzite strengthens circulatory system and heart muscle. Beneficial for lungs. Alleviates joint pain. Neutralizes effects of anesthesia and stimulates immune system.",
            howToUse = "Wear as jewelry for emotional healing. Meditate with kunzite for peace. Place on heart chakra for opening. Keep in bedroom for calm sleep.",
            rarity = "Semi-Precious • Moderately Rare"
        ),
        Gemstone(
            name = "Alexandrite",
            emoji = "💎",
            primaryColor = Color(0xFF9370DB),
            benefits = "Alexandrite is a stone of prosperity and longevity. Brings joy and good fortune. Strengthens intuition, creativity and imagination. Encourages romance and enhances sensuality.",
            zodiacSigns = listOf("Gemini", "Scorpio"),
            chakra = "Crown & Heart Chakra - Balances emotions and spirit",
            properties = "Alexandrite regenerates neurological tissue. Treats leukemia and disorders of nervous system. Aids in treatment of swollen lymph nodes and spleen. Beneficial for pancreas.",
            howToUse = "Wear as jewelry for transformation. Meditate with alexandrite for balance. Place on crown chakra for spiritual growth. Keep for good luck.",
            rarity = "Precious • Extremely Rare"
        )
    )
}
