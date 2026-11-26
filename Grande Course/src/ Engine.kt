package racing

import kotlin.math.cos
import kotlin.math.sin

/**
 * Физика заезда. Игрок стартует в (0,0), при angle=0 смотрит «вверх» экрана,
 * прогресс = продвижение по трассе вперёд.
 */
class Engine(
    private val maxSpeed: Float,
    private val grip: Float,
    private val braking: Float,
) {
    companion object {
        const val TRACK_LENGTH = 5000f
        private const val ACCELERATION = 300f
        private const val COAST_DECELERATION = 80f
        private const val MAX_STEP = 0.05f
    }

    var x = 0f
        private set
    var y = 0f
        private set
    var speed = 0f
        private set
    var angle = 0f
        private set
    var progress = 0f
        private set
    var elapsed = 0f
        private set
    var finished = false
        private set

    data class Bot(
        val laneX: Float,
        var y: Float,
        var speed: Float,
        val speedFactor: Float,
    )

    private val _bots = mutableListOf<Bot>()
    val bots: List<Bot> get() = _bots

    fun init(withBots: Boolean) {
        x = 0f; y = 0f; speed = 0f; angle = 0f
        progress = 0f; elapsed = 0f; finished = false
        _bots.clear()
        if (withBots) {
            // Медленный — на старте, средний — чуть впереди, быстрый — дальше всех
            _bots += Bot(-100f, 0f, 0f, 0.80f)
            _bots += Bot(0f, -40f, 0f, 0.87f)
            _bots += Bot(100f, -80f, 0f, 0.94f)
        }
    }

    fun update(dt: Float, throttle: Float, steer: Float) {
        if (finished) return
        val step = dt.coerceAtMost(MAX_STEP)
        elapsed += step

        speed = when {
            throttle > 0f -> speed + ACCELERATION * step
            throttle < 0f -> speed - braking * step
            else -> speed - COAST_DECELERATION * step
        }.coerceIn(0f, maxSpeed)

        angle += steer * grip * (speed / maxSpeed + 0.2f) * 2.5f * step

        val forward = cos(angle) * speed * step
        x += sin(angle) * speed * step
        y -= forward
        progress += forward

        if (progress >= TRACK_LENGTH) finished = true

        _bots.forEach { bot ->
            val target = maxSpeed * bot.speedFactor
            bot.speed += (target - bot.speed) * 2f * step
            bot.y -= bot.speed * step
        }
    }

    fun rank(): Int = 1 + _bots.count { -it.y > progress }
}