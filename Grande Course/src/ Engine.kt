package racing

import kotlin.math.cos
import kotlin.math.sin

class Engine(
    maxSpeed: Float,
    grip: Float,
    braking: Float,
    val track: Track,
) {
    private val maxSpeed = maxSpeed
    private val grip = grip * track.gripMultiplier           // ← модификатор сцепления
    private val braking = braking
    private val trackLength = track.length                   // ← длина из карты

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
            // Сложность карты сдвигает скорость ботов вверх
            val bonus = (track.difficulty - 1) * 0.03f
            fun factor(base: Float) = (base + bonus).coerceIn(0f, 1.05f)
            _bots += Bot(-100f, 0f, 0f, factor(0.80f))
            _bots += Bot(0f, -40f, 0f, factor(0.87f))
            _bots += Bot(100f, -80f, 0f, factor(0.94f))
        }
    }

    fun update(dt: Float, throttle: Float, steer: Float) {
        if (finished) return
        val step = dt.coerceAtMost(0.05f)
        elapsed += step

        speed = when {
            throttle > 0f -> speed + 300f * step
            throttle < 0f -> speed - braking * step
            else -> speed - 80f * step
        }.coerceIn(0f, maxSpeed)

        angle += steer * grip * (speed / maxSpeed + 0.2f) * 2.5f * step

        val forward = cos(angle) * speed * step
        x += sin(angle) * speed * step
        y -= forward
        progress += forward

        if (progress >= trackLength) finished = true

        _bots.forEach { bot ->
            val target = maxSpeed * bot.speedFactor
            bot.speed += (target - bot.speed) * 2f * step
            bot.y -= bot.speed * step
        }
    }

    fun rank(): Int = 1 + _bots.count { -it.y > progress }
}