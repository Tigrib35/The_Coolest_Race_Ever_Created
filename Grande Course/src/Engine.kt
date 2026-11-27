package racing

import kotlin.math.cos
import kotlin.math.sin

class Engine(
    maxSpeed: Float,
    grip: Float,
    braking: Float,
    trackLength: Float = 5000f
) {
    private val maxSpeed = maxSpeed
    private val grip = grip
    private val braking = braking

    var x = 0f
    var y = 0f
    var speed = 0f
    var angle = 0f
    var finished = false
    var time = 0f
    var dist = 0f

    val trackLen = trackLength

    data class Bot(
        var x: Float,
        var y: Float,
        var speed: Float
    )

    val bots = mutableListOf<Bot>()

    fun init(withBots: Boolean) {
        x = 0f
        y = 0f
        speed = 0f
        angle = 0f
        dist = 0f
        finished = false
        time = 0f

        bots.clear()

        if (withBots) {
            bots.add(Bot(-80f, 0f, 0f))
            bots.add(Bot(0f, 60f, 0f))
            bots.add(Bot(80f, -60f, 0f))
        }
    }

    fun update(
        dt: Float,
        throttle: Float,
        steer: Float
    ) {
        if (finished) return

        time += dt

        when {
            throttle > 0f ->
                speed += 300f * dt

            throttle < 0f ->
                speed -= braking * dt

            else ->
                speed -= 80f * dt
        }

        speed = speed.coerceIn(0f, maxSpeed)

        angle += steer * grip *
                (speed / maxSpeed + 0.2f) *
                2.5f * dt

        x += sin(angle) * speed * dt
        y -= cos(angle) * speed * dt

        dist += cos(angle) * speed * dt

        if (dist >= trackLen) {
            finished = true
        }

        bots.forEach { bot ->
            bot.speed +=
                (maxSpeed * 0.85f - bot.speed) *
                        2f * dt

            bot.y -= bot.speed * dt
        }
    }

    fun rank(): Int {
        var rank = 1

        bots.forEach { bot ->
            if (-bot.y > dist) {
                rank++
            }
        }

        return rank
    }
}