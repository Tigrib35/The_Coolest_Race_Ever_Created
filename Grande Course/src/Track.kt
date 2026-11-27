<<<<<<< HEAD
package racing

import androidx.compose.ui.graphics.Color

data class TrackTheme(
    val asphalt: Color,       // цвет дороги
    val marking: Color,       // цвет разметки
    val accent: Color,        // цвет рамки/акцента (превью, карточки)
    val ambient: Color,       // фон вне дороги
    val hasCurves: Boolean,   // визуальные повороты (для разнообразия)
)

data class Track(
    val id: String,
    val name: String,
    val description: String,
    val length: Float,            // метров до финиша
    val difficulty: Int,          // 1..5 — влияет на скорость ботов
    val gripMultiplier: Float,    // <1 = скользко, >1 = цепко
    val rewardMultiplier: Float,  // множитель монет и опыта
    val requiredLevel: Int,       // с какого уровня доступна
    val theme: TrackTheme,
)

object Tracks {
    val all: List<Track> = listOf(
        Track(
            id = "highway",
            name = "Шоссе",
            description = "Ровная прямая. Идеально для новичков.",
            length = 5000f,
            difficulty = 1,
            gripMultiplier = 1.0f,
            rewardMultiplier = 1.0f,
            requiredLevel = 1,
            theme = TrackTheme(
                asphalt = Color(0xFF263238),
                marking = Color.White,
                accent = Color(0xFF4CAF50),
                ambient = Color(0xFF1A2025),
                hasCurves = false,
            ),
        ),
        Track(
            id = "mountain",
            name = "Горный перевал",
            description = "Серпантин. Держитесь подальше от обрыва.",
            length = 6000f,
            difficulty = 2,
            gripMultiplier = 0.90f,
            rewardMultiplier = 1.3f,
            requiredLevel = 2,
            theme = TrackTheme(
                asphalt = Color(0xFF3E2723),
                marking = Color(0xFFFFEB3B),
                accent = Color(0xFF8D6E63),
                ambient = Color(0xFF1B0F0A),
                hasCurves = true,
            ),
        ),
        Track(
            id = "desert",
            name = "Пустыня",
            description = "Песок и жара. Асфальт скользкий.",
            length = 7000f,
            difficulty = 3,
            gripMultiplier = 0.75f,
            rewardMultiplier = 1.6f,
            requiredLevel = 3,
            theme = TrackTheme(
                asphalt = Color(0xFFD4A574),
                marking = Color(0xFFFFF8E1),
                accent = Color(0xFFFF9800),
                ambient = Color(0xFF8B5A2B),
                hasCurves = false,
            ),
        ),
        Track(
            id = "night_city",
            name = "Ночной город",
            description = "Узкие улицы, неон, высокие скорости.",
            length = 8000f,
            difficulty = 4,
            gripMultiplier = 0.95f,
            rewardMultiplier = 2.0f,
            requiredLevel = 5,
            theme = TrackTheme(
                asphalt = Color(0xFF1A1A2E),
                marking = Color(0xFFE94560),
                accent = Color(0xFF00D9FF),
                ambient = Color(0xFF0F0F1E),
                hasCurves = true,
            ),
        ),
        Track(
            id = "volcano",
            name = "Вулкан",
            description = "Финальное испытание. Лава рядом.",
            length = 10000f,
            difficulty = 5,
            gripMultiplier = 0.85f,
            rewardMultiplier = 3.0f,
            requiredLevel = 7,
            theme = TrackTheme(
                asphalt = Color(0xFF2C1A1A),
                marking = Color(0xFFFFD54F),
                accent = Color(0xFFFF5722),
                ambient = Color(0xFF1A0000),
                hasCurves = true,
            ),
        ),
    )

    val default: Track get() = all.first()
=======
package racing

import androidx.compose.ui.graphics.Color

data class TrackTheme(
    val asphalt: Color,       // цвет дороги
    val marking: Color,       // цвет разметки
    val accent: Color,        // цвет рамки/акцента (превью, карточки)
    val ambient: Color,       // фон вне дороги
    val hasCurves: Boolean,   // визуальные повороты (для разнообразия)
)

data class Track(
    val id: String,
    val name: String,
    val description: String,
    val length: Float,            // метров до финиша
    val difficulty: Int,          // 1..5 — влияет на скорость ботов
    val gripMultiplier: Float,    // <1 = скользко, >1 = цепко
    val rewardMultiplier: Float,  // множитель монет и опыта
    val requiredLevel: Int,       // с какого уровня доступна
    val theme: TrackTheme,
)

object Tracks {
    val all: List<Track> = listOf(
        Track(
            id = "highway",
            name = "Шоссе",
            description = "Ровная прямая. Идеально для новичков.",
            length = 5000f,
            difficulty = 1,
            gripMultiplier = 1.0f,
            rewardMultiplier = 1.0f,
            requiredLevel = 1,
            theme = TrackTheme(
                asphalt = Color(0xFF263238),
                marking = Color.White,
                accent = Color(0xFF4CAF50),
                ambient = Color(0xFF1A2025),
                hasCurves = false,
            ),
        ),
        Track(
            id = "mountain",
            name = "Горный перевал",
            description = "Серпантин. Держитесь подальше от обрыва.",
            length = 6000f,
            difficulty = 2,
            gripMultiplier = 0.90f,
            rewardMultiplier = 1.3f,
            requiredLevel = 2,
            theme = TrackTheme(
                asphalt = Color(0xFF3E2723),
                marking = Color(0xFFFFEB3B),
                accent = Color(0xFF8D6E63),
                ambient = Color(0xFF1B0F0A),
                hasCurves = true,
            ),
        ),
        Track(
            id = "desert",
            name = "Пустыня",
            description = "Песок и жара. Асфальт скользкий.",
            length = 7000f,
            difficulty = 3,
            gripMultiplier = 0.75f,
            rewardMultiplier = 1.6f,
            requiredLevel = 3,
            theme = TrackTheme(
                asphalt = Color(0xFFD4A574),
                marking = Color(0xFFFFF8E1),
                accent = Color(0xFFFF9800),
                ambient = Color(0xFF8B5A2B),
                hasCurves = false,
            ),
        ),
        Track(
            id = "night_city",
            name = "Ночной город",
            description = "Узкие улицы, неон, высокие скорости.",
            length = 8000f,
            difficulty = 4,
            gripMultiplier = 0.95f,
            rewardMultiplier = 2.0f,
            requiredLevel = 5,
            theme = TrackTheme(
                asphalt = Color(0xFF1A1A2E),
                marking = Color(0xFFE94560),
                accent = Color(0xFF00D9FF),
                ambient = Color(0xFF0F0F1E),
                hasCurves = true,
            ),
        ),
        Track(
            id = "volcano",
            name = "Вулкан",
            description = "Финальное испытание. Лава рядом.",
            length = 10000f,
            difficulty = 5,
            gripMultiplier = 0.85f,
            rewardMultiplier = 3.0f,
            requiredLevel = 7,
            theme = TrackTheme(
                asphalt = Color(0xFF2C1A1A),
                marking = Color(0xFFFFD54F),
                accent = Color(0xFFFF5722),
                ambient = Color(0xFF1A0000),
                hasCurves = true,
            ),
        ),
    )

    val default: Track get() = all.first()
>>>>>>> 8b156d369d27faeb82cfbb970c835c7b2634cdad
}