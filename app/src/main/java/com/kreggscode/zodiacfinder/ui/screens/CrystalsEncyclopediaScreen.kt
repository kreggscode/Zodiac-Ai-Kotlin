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

data class Crystal(
    val name: String,
    val emoji: String,
    val primaryColor: Color,
    val healingProperties: String,
    val energyType: String,
    val chakra: String,
    val emotionalHealing: String,
    val physicalHealing: String,
    val spiritualPurpose: String,
    val howToUse: String,
    val cleansing: String
)

@Composable
fun CrystalsEncyclopediaScreen(
    onNavigateBack: () -> Unit = {}
) {
    val crystals = remember { getCrystalsList() }
    var selectedCrystal by remember { mutableStateOf<Crystal?>(null) }
    
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
        if (selectedCrystal == null) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    TopBarWithBack(
                        title = "💎 Crystals Encyclopedia",
                        subtitle = "30 healing crystals & energy work",
                        onBackClick = onNavigateBack
                    )
                }
                
                item {
                    GradientGlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        gradientColors = listOf(
                            MysticPurple.copy(alpha = 0.3f),
                            CosmicBlue.copy(alpha = 0.2f)
                        )
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "✨ About Healing Crystals",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Crystals are natural formations that carry specific vibrational frequencies. They are used for healing, meditation, energy work, and spiritual growth. Each crystal has unique properties that can help balance your physical, emotional, and spiritual wellbeing.",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
                
                items(crystals) { crystal ->
                    CrystalListCard(
                        crystal = crystal,
                        onClick = { selectedCrystal = crystal }
                    )
                }
            }
        } else {
            CrystalDetailScreen(
                crystal = selectedCrystal!!,
                onBack = { selectedCrystal = null }
            )
        }
    }
}

@Composable
private fun CrystalListCard(
    crystal: Crystal,
    onClick: () -> Unit
) {
    GradientGlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        gradientColors = listOf(
            crystal.primaryColor.copy(alpha = 0.4f),
            crystal.primaryColor.copy(alpha = 0.2f)
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = crystal.emoji,
                fontSize = 56.sp
            )
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = crystal.name,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = crystal.energyType,
                    fontSize = 13.sp,
                    color = crystal.primaryColor,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = crystal.healingProperties.take(80) + "...",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
private fun CrystalDetailScreen(
    crystal: Crystal,
    onBack: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            TopBarWithBack(
                title = "${crystal.emoji} ${crystal.name}",
                subtitle = crystal.energyType,
                onBackClick = onBack
            )
        }
        
        // Overview
        item {
            GradientGlassCard(
                modifier = Modifier.fillMaxWidth(),
                gradientColors = listOf(
                    crystal.primaryColor.copy(alpha = 0.5f),
                    crystal.primaryColor.copy(alpha = 0.3f)
                )
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = crystal.emoji,
                        fontSize = 100.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = crystal.name,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = crystal.energyType,
                        fontSize = 16.sp,
                        color = crystal.primaryColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
        
        // Healing Properties
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "✨ Healing Properties",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                    Text(
                        text = crystal.healingProperties,
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
                    MysticPurple.copy(alpha = 0.3f),
                    CosmicBlue.copy(alpha = 0.2f)
                )
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "🧘 Chakra Connection",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = crystal.chakra,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                        lineHeight = 22.sp
                    )
                }
            }
        }
        
        // Emotional Healing
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "💖 Emotional Healing",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(0.1f))
                    Text(
                        text = crystal.emotionalHealing,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                        lineHeight = 24.sp
                    )
                }
            }
        }
        
        // Physical Healing
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "🏥 Physical Healing",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(0.1f))
                    Text(
                        text = crystal.physicalHealing,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                        lineHeight = 24.sp
                    )
                }
            }
        }
        
        // Spiritual Purpose
        item {
            GradientGlassCard(
                modifier = Modifier.fillMaxWidth(),
                gradientColors = listOf(
                    LuckyGold.copy(alpha = 0.3f),
                    LuckyGold.copy(alpha = 0.1f)
                )
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "🌟 Spiritual Purpose",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = LuckyGold
                    )
                    Text(
                        text = crystal.spiritualPurpose,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                        lineHeight = 24.sp
                    )
                }
            }
        }
        
        // How to Use
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "📖 How to Use",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(0.1f))
                    Text(
                        text = crystal.howToUse,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                        lineHeight = 24.sp
                    )
                }
            }
        }
        
        // Cleansing
        item {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "🌊 Cleansing & Care",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(0.1f))
                    Text(
                        text = crystal.cleansing,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                        lineHeight = 24.sp
                    )
                }
            }
        }
    }
}

private fun getCrystalsList(): List<Crystal> {
    return listOf(
        Crystal(
            name = "Clear Quartz",
            emoji = "💎",
            primaryColor = Color(0xFFE8F4F8),
            healingProperties = "Master Healer and energy amplifier. Absorbs, stores, releases and regulates energy. Draws off negative energy of all kinds and balances and revitalizes the physical, mental, emotional and spiritual planes.",
            energyType = "Amplification • Universal Healing",
            chakra = "All Chakras - Amplifies energy and thought, as well as the effect of other crystals",
            emotionalHealing = "Aids concentration and unlocks memory. Stimulates the immune system and brings the body into balance. Harmonizes all the chakras and aligns the subtle bodies.",
            physicalHealing = "Stimulates the immune and circulatory systems, enhancing energy flow and bringing the body into balance. Excellent for chronic fatigue, arthritis, bone injuries, depression, diabetes, fibromyalgia.",
            spiritualPurpose = "Clear Quartz enhances spiritual growth, spiritual awareness and wisdom. It increases inspiration and creativity. Protects against negativity, attunes to your higher self.",
            howToUse = "Wear as jewelry, carry in pocket, place on altar, use in crystal grids, hold during meditation, place on any chakra, program with intentions.",
            cleansing = "Cleanse under running water, moonlight, sunlight (brief), smudging with sage, sound healing, or place on selenite. Recharge in sunlight or moonlight."
        ),
        Crystal(
            name = "Selenite",
            emoji = "🤍",
            primaryColor = Color(0xFFF5F5F5),
            healingProperties = "Selenite is a calming stone that instills deep peace. It assists judgment and insight. It clears confusion and aids in seeing the deeper picture. Selenite aligns the spinal column and promotes flexibility.",
            energyType = "Cleansing • Angelic Connection",
            chakra = "Crown Chakra - Connects to higher consciousness and angelic realm",
            emotionalHealing = "Brings clarity of mind, opening the crown and higher crown chakras. Accesses angelic consciousness and higher guidance. Instills deep peace and is excellent for meditation or spiritual work.",
            physicalHealing = "Aligns the spine and is good for the skeleton. Promotes flexibility and guards against epileptic seizures. Reverses the effects of free radicals and is excellent for the skin.",
            spiritualPurpose = "Selenite is a link to the light body, helping to anchor it in the earth vibration. It is a powerful disperser and stabilizer for erratic emotions. One of the most powerful crystals for the new vibration on earth.",
            howToUse = "Place in corners of home for protection, use wands for energy clearing, place under pillow for peaceful sleep, hold during meditation, create crystal grids, use for cleansing other crystals.",
            cleansing = "NEVER use water (selenite dissolves). Cleanse with sound, moonlight, or smudging. Selenite is self-cleansing and can cleanse other crystals. Simply place other crystals on selenite overnight."
        ),
        Crystal(
            name = "Black Obsidian",
            emoji = "🖤",
            primaryColor = Color(0xFF000000),
            healingProperties = "Black Obsidian is a very powerful and creative stone. It increases self-control and forces facing up to one's true self. Releases imbalances and negative energies. Obsidian is protective and provides support during change.",
            energyType = "Protection • Grounding • Truth",
            chakra = "Root Chakra - Grounds spiritual energy into the physical plane",
            emotionalHealing = "Brings hidden emotions and traumas to the surface for healing. Promotes qualities of compassion and strength. Helps you to know who you truly are. Dissolves emotional blockages and ancient traumas.",
            physicalHealing = "Aids the digestion and detoxifies. Reduces arthritis pain, joint problems and cramps. Warms the extremities. Beneficial for circulation and may shrink an enlarged prostate.",
            spiritualPurpose = "Obsidian is molten lava that cooled so quickly it had no time to crystallize. It is a stone without boundaries or limitations. Works extremely fast with great power. Nothing can be hidden from Obsidian.",
            howToUse = "Carry for protection, place at home entrance, use in meditation for shadow work, wear as jewelry for grounding, use in scrying for divination, place under pillow for revealing dreams.",
            cleansing = "Cleanse under running water, bury in earth overnight, smudge with sage, place in moonlight. Obsidian absorbs negative energy quickly, so cleanse frequently. Recharge in earth or with clear quartz."
        ),
        Crystal(
            name = "Amethyst",
            emoji = "💜",
            primaryColor = Color(0xFF9966CC),
            healingProperties = "Amethyst is a natural tranquilizer that relieves stress, soothes irritability, and balances mood swings. Activates spiritual awareness, opens intuition, and enhances psychic abilities. Known for overcoming addictions and blocking negative energies.",
            energyType = "Calming • Spiritual • Protection",
            chakra = "Third Eye & Crown Chakra - Opens spiritual consciousness",
            emotionalHealing = "Amethyst has strong healing and cleansing powers. Enhances memory and improves motivation. Encourages selflessness and spiritual wisdom. Protects against psychic attack and ill fortune. Promotes emotional centering.",
            physicalHealing = "Treats insomnia, headaches, immune system disorders, skin conditions, digestive issues, hormone balance. Beneficial for the nervous system and brain.",
            spiritualPurpose = "Activates spiritual awareness, enhances psychic abilities, overcomes addictions, blocks negative energies. Promotes spiritual wisdom and understanding.",
            howToUse = "Place under pillow for peaceful sleep and prophetic dreams. Wear as jewelry for daily protection. Meditate holding amethyst for spiritual connection. Use in crystal grids for manifestation.",
            cleansing = "Moonlight, smudging, sound. Avoid prolonged sunlight (fades). Recharge in moonlight or on selenite."
        ),
        Crystal(
            name = "Rose Quartz",
            emoji = "💗",
            primaryColor = Color(0xFFFF66CC),
            healingProperties = "Rose Quartz is the stone of universal love. Restores trust and harmony in relationships, encourages unconditional love. Purifies and opens the heart at all levels. Promotes self-love, friendship, deep inner healing, and peace.",
            energyType = "Love • Compassion • Healing",
            chakra = "Heart Chakra - Opens heart to all forms of love",
            emotionalHealing = "Rose Quartz strengthens empathy and sensitivity. Helps accept necessary change. Excellent for trauma and crisis situations. Releases unexpressed emotions and heartache. Encourages self-forgiveness and acceptance.",
            physicalHealing = "Strengthens heart and circulatory system, releases impurities from body fluids. Aids chest and lung problems. Heals kidneys and adrenals. Increases fertility.",
            spiritualPurpose = "Promotes self-love, friendship, deep inner healing, peace. Attracts love and maintains relationships. Opens heart to divine love.",
            howToUse = "Wear close to the heart as a pendant. Place in bedroom for harmonious relationships. Hold during self-love meditation. Use in bath water for emotional healing.",
            cleansing = "Running water, moonlight, smudging. Avoid prolonged sunlight. Recharge in moonlight or with other rose quartz."
        ),
        Crystal(
            name = "Citrine",
            emoji = "💛",
            primaryColor = Color(0xFFFFD700),
            healingProperties = "Citrine is known as the 'merchant's stone' for its properties of increasing prosperity. Activates creativity and encourages self-expression. Enhances concentration and revitalizes the mind. Releases negative traits and promotes joy.",
            energyType = "Abundance • Joy • Manifestation",
            chakra = "Solar Plexus & Sacral Chakra - Activates personal power and creativity",
            emotionalHealing = "Citrine raises self-esteem and self-confidence. Removes destructive tendencies. Enhances individuality, improves motivation, activates creativity and encourages self-expression. Releases negative traits, fears and phobias.",
            physicalHealing = "Stimulates digestion, spleen and pancreas. Negates kidney and bladder infections. Helps eye problems, increases blood circulation, detoxifies blood, activates thymus and balances thyroid. Relieves constipation and removes cellulite.",
            spiritualPurpose = "Citrine is one of only two crystals on Earth that never needs to be cleared or cleansed. It absorbs, transmutes, dissipates and grounds negative energy. Extremely protective for the environment. Energizes every level of life.",
            howToUse = "Place in wealth corner of home, carry in wallet, wear as jewelry, place in cash register, use in manifestation grids, meditate with for abundance, keep in workspace.",
            cleansing = "Citrine NEVER needs cleansing - it's self-cleansing! It actually cleanses other crystals. Simply place other stones on citrine to cleanse them. Recharge in sunlight for extra energy."
        ),
        Crystal(
            name = "Black Tourmaline",
            emoji = "⚫",
            primaryColor = Color(0xFF28282B),
            healingProperties = "Black Tourmaline is one of the most powerful protection stones. Forms a shield against negative energies, electromagnetic pollution, and psychic attack. Grounds and balances, promotes clear thinking. Transforms negative thoughts into positive energy.",
            energyType = "Protection • Grounding • Purification",
            chakra = "Root Chakra - Grounds and protects",
            emotionalHealing = "Tourmaline cleanses, purifies and transforms dense energy into lighter vibration. Grounds spiritual energy. Balances right-left hemispheres of brain. Helps understand oneself and others. Attracts inspiration and compassion.",
            physicalHealing = "Strengthens immune system, treats arthritis, provides pain relief, improves circulation. Beneficial for the nervous system and heart.",
            spiritualPurpose = "Forms a protective shield around the body. Grounds spiritual energy into the physical plane. Deflects negative energies from the environment.",
            howToUse = "Carry in pocket for daily protection. Place at corners of home for energy shield. Wear as jewelry for grounding. Keep near electronics to absorb EMF radiation.",
            cleansing = "Running water, bury in earth, smudge, moonlight. Cleanse frequently as it absorbs much negativity. Recharge in earth or sunlight."
        ),
        Crystal(
            name = "Smoky Quartz",
            emoji = "🟤",
            primaryColor = Color(0xFF8B4513),
            healingProperties = "Smoky Quartz is one of the most efficient grounding and anchoring stones. It gently neutralizes negative vibrations and is detoxifying on all levels. Disperses fear, lifts depression and negativity. Brings emotional calmness.",
            energyType = "Grounding • Detoxification • Stress Relief",
            chakra = "Root Chakra - Grounds energy and connects to earth",
            emotionalHealing = "Relieves stress and anxiety, promotes positive thoughts and actions. Dispels nightmares and manifests your dreams. Aids concentration and assists in communication difficulties. Dissolves cramps and strengthens the back.",
            physicalHealing = "Particularly effective for ailments of the abdomen, hips and legs. Relieves pain including headaches. Benefits the reproductive system, muscle and nerve tissue, and the heart. Dissolves cramps.",
            spiritualPurpose = "Smoky Quartz teaches you how to leave behind anything that no longer serves you. It is a superb antidote to stress. Assists in tolerating difficult times with equanimity, fortifying resolve.",
            howToUse = "Carry for grounding, place in corners of home, hold during meditation, wear as jewelry, place on root chakra, use in crystal grids for manifestation, keep in workspace for stress relief.",
            cleansing = "Cleanse under running water, place in earth, moonlight, or sunlight (brief). Smudge with sage or palo santo. Recharge by placing on clear quartz cluster or in sunlight."
        ),
        Crystal(
            name = "Lepidolite",
            emoji = "💜",
            primaryColor = Color(0xFFDDA0DD),
            healingProperties = "Lepidolite is a stone of transition. It assists in the release and reorganization of old behavioral and psychological patterns, gently inducing change. Brings deep emotional healing, soothing and reducing stress and depression.",
            energyType = "Calming • Transition • Emotional Balance",
            chakra = "Third Eye & Crown Chakra - Opens spiritual awareness",
            emotionalHealing = "Lepidolite dissipates negativity and insists on being used for the highest good. Activates the throat, heart, third eye and crown chakras. Clears electromagnetic pollution. Relieves allergies and strengthens the immune system.",
            physicalHealing = "Relieves exhaustion and aids in the relief of tension and related disorders. Helps to stabilize mood swings and bipolar disorder. Excellent for menopause. Treats illnesses caused by 'sick-building syndrome' or computer stress.",
            spiritualPurpose = "Lepidolite contains lithium and is helpful in stabilizing mood swings and bipolar disorder. It is excellent for overcoming emotional or mental dependency and helps treat addictions and all kinds of compulsions.",
            howToUse = "Place under pillow for peaceful sleep, carry for emotional balance, meditate with for stress relief, wear as jewelry, place in workspace, use in bath water for relaxation.",
            cleansing = "Cleanse gently with moonlight, smudging, or sound. Avoid water and sunlight which can fade the color. Recharge on selenite or with sound healing."
        ),
        Crystal(
            name = "Shungite",
            emoji = "⚫",
            primaryColor = Color(0xFF2F4F4F),
            healingProperties = "Shungite is an ancient stone estimated to be around 2 billion years old. It contains fullerenes, a crystalline form of carbon. Shungite is a powerful protection stone, especially against EMFs and radiation. It purifies, protects, normalizes, induces recovery and promotes growth.",
            energyType = "EMF Protection • Purification • Grounding",
            chakra = "Root Chakra - Grounds and protects from electromagnetic frequencies",
            emotionalHealing = "Infuses the aura with light and allows only positive energy to reach you. Grounds spiritual energy into the body. Promotes mental clarity and emotional balance. Shields from negative energies.",
            physicalHealing = "Boosts immune system, balances blood pressure, clears toxins, aids cellular metabolism. Protects from harmful EMFs from phones, computers, WiFi. Purifies water. Beneficial for allergies and skin conditions.",
            spiritualPurpose = "Shungite is one of the only known natural materials known to contain fullerenes, which are powerful antioxidants. The energy embodied within this ancient stone is said to absorb and eliminate anything that is a health hazard to human life.",
            howToUse = "Place near electronics for EMF protection, carry in pocket, place in water for purification (shungite water), wear as pendant, place in corners of home, use in meditation for grounding.",
            cleansing = "Cleanse under running water, place in sunlight, bury in earth. Shungite doesn't need frequent cleansing as it doesn't absorb negative energy but rather transforms it. Recharge in sunlight."
        )
        // Add remaining 20 crystals here following the same pattern
    )
}
