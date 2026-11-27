
package racing

import androidx.compose.ui.graphics.Color

data class TrackTheme(
    val sky: Color,
    val ground: Color,
    val groundAlt: Color,
    val asphalt: Color,
    val marking: Color,
    val curb: Color,
    val accent: Color,
    val hasTrees: Boolean,
    val hasMountains: Boolean,
    val hasCactus: Boolean,
    val hasBuildings: Boolean,
    val hasLava: Boolean,
    val curveStrength: Float
)

data class Track(
    val id: String,
    val name: String,
    val description: String,
    val length: Float,
    val difficulty: Int,
    val gripMultiplier: Float,
    val rewardMultiplier: Float,
    val requiredLevel: Int,
    val theme: TrackTheme
)

object Tracks {

    val all = listOf(

        Track(
            id = "highway",
            name = "Зелёное шоссе",
            description = "Классическая трасса среди деревьев",
            length = 5000f,
            difficulty = 1,
            gripMultiplier = 1f,
            rewardMultiplier = 1f,
            requiredLevel = 1,
            theme = TrackTheme(
                sky = Color(0xFF75C9EF),
                ground = Color(0xFF287A35),
                groundAlt = Color(0xFF205D2C),
                asphalt = Color(0xFF34383D),
                marking = Color.White,
                curb = Color(0xFFE53935),
                accent = Color(0xFF4CAF50),
                hasTrees = true,
                hasMountains = false,
                hasCactus = false,
                hasBuildings = false,
                hasLava = false,
                curveStrength = 0.7f
            )
        ),

        Track(
            id = "mountain",
            name = "Горный серпантин",
            description = "Опасные повороты среди гор",
            length = 6000f,
            difficulty = 2,
            gripMultiplier = 0.9f,
            rewardMultiplier = 1.3f,
            requiredLevel = 2,
            theme = TrackTheme(
                sky = Color(0xFF7895B2),
                ground = Color(0xFF455A46),
                groundAlt = Color(0xFF37483A),
                asphalt = Color(0xFF30343A),
                marking = Color(0xFFFFEB3B),
                curb = Color(0xFFFF5722),
                accent = Color(0xFF8D6E63),
                hasTrees = true,
                hasMountains = true,
                hasCactus = false,
                hasBuildings = false,
                hasLava = false,
                curveStrength = 1.8f
            )
        ),

        Track(
            id = "desert",
            name = "Пустыня",
            description = "Жаркая трасса среди песчаных дюн",
            length = 7000f,
            difficulty = 3,
            gripMultiplier = 0.75f,
            rewardMultiplier = 1.6f,
            requiredLevel = 3,
            theme = TrackTheme(
                sky = Color(0xFFFFB56B),
                ground = Color(0xFFD4A05A),
                groundAlt = Color(0xFFC28B45),
                asphalt = Color(0xFF5C5148),
                marking = Color.White,
                curb = Color(0xFFFF9800),
                accent = Color(0xFFFF9800),
                hasTrees = false,
                hasMountains = false,
                hasCactus = true,
                hasBuildings = false,
                hasLava = false,
                curveStrength = 0.9f
            )
        ),

        Track(
            id = "night_city",
            name = "Ночной город",
            description = "Неоновые улицы большого города",
            length = 8000f,
            difficulty = 4,
            gripMultiplier = 0.95f,
            rewardMultiplier = 2f,
            requiredLevel = 5,
            theme = TrackTheme(
                sky = Color(0xFF11112C),
                ground = Color(0xFF202039),
                groundAlt = Color(0xFF17172D),
                asphalt = Color(0xFF252536),
                marking = Color(0xFF00E5FF),
                curb = Color(0xFFE94560),
                accent = Color(0xFF00D9FF),
                hasTrees = false,
                hasMountains = false,
                hasCactus = false,
                hasBuildings = true,
                hasLava = false,
                curveStrength = 1.4f
            )
        ),

        Track(
            id = "volcano",
            name = "Вулкан",
            description = "Опасная трасса рядом с раскалённой лавой",
            length = 10000f,
            difficulty = 5,
            gripMultiplier = 0.85f,
            rewardMultiplier = 3f,
            requiredLevel = 7,
            theme = TrackTheme(
                sky = Color(0xFF351515),
                ground = Color(0xFF292020),
                groundAlt = Color(0xFF181313),
                asphalt = Color(0xFF242020),
                marking = Color(0xFFFFD54F),
                curb = Color(0xFFFF5722),
                accent = Color(0xFFFF5722),
                hasTrees = false,
                hasMountains = true,
                hasCactus = false,
                hasBuildings = false,
                hasLava = true,
                curveStrength = 1.6f
            )
        )
    )

    val default: Track
        get() = all.first()
}
