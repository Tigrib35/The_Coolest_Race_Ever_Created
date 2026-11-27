
package racing

import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

class Engine(maxSpeed: Float, grip: Float, braking: Float) {
    private val maxSpeed = maxSpeed
    private val grip = grip
    private val braking = braking

    var x = 0f
    var y = 0f
    var speed = 0f
    var angle = 0f
    var finished = false
    var crashed = false
    var time = 0f
    var dist = 0f

    val trackLen = 5000f

    data class Bot(var x: Float, var y: Float, var speed: Float)
    val bots = mutableListOf<Bot>()

    fun init(withBots: Boolean) {
        x = 0f
        y = 0f
        speed = 0f
        angle = 0f
        dist = 0f
        finished = false
        crashed = false
        time = 0f
        bots.clear()

        if (withBots) {
            bots.add(Bot(-80f, 0f, 0f))
            bots.add(Bot(0f, 60f, 0f))
            bots.add(Bot(80f, -60f, 0f))
        }
    }

    fun crash() {
        if (!finished) {
            crashed = true
            speed = 0f
        }
    }

    fun update(dt: Float, throttle: Float, steer: Float) {
        if (finished || crashed) return

        time += dt

        when {
            throttle > 0f -> speed += 300f * dt
            throttle < 0f -> speed -= braking * dt
            else -> speed -= 80f * dt
        }

        // Машина плавно поворачивает.
        angle += (steer * grip * 1.8f - angle * 1.5f) * dt

        // Боковое перемещение относительно центра дороги.
        x += sin(angle) * speed * dt * 0.8f

        // Возвращение руля к прямому положению.
        if (steer == 0f) {
            angle *= (1f - 1.5f * dt).coerceAtLeast(0f)
        }

        // Границы дороги.
        val offRoad = abs(x) > 145f

        if (offRoad) {
            speed -= 230f * dt
        }

        // Сильный выезд за трассу — авария у обочины.
        if (abs(x) > 260f) {
            crash()
            return
        }

        speed = speed.coerceIn(0f, maxSpeed)

        dist += speed * dt

        if (dist >= trackLen) {
            dist = trackLen
            finished = true
        }

        bots.forEach { bot ->
            bot.speed += (maxSpeed * 0.85f - bot.speed) * 2f * dt
            bot.y -= bot.speed * dt
        }
    }

    fun rank(): Int {
        var r = 1
        bots.forEach {
            if (-it.y > dist) r++
        }
        return r
    }
}
