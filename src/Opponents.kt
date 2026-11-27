
package racing

import androidx.compose.runtime.mutableStateListOf

data class Opponent(
    val car: CarInfo,
    var x: Float,
    var y: Float,
    var speed: Float
)

class Opponents(selectedCarId: String) {

    val enemies = mutableStateListOf<Opponent>()

    init {
        val availableCars = cars.filter {
            it.id != selectedCarId
        }

        availableCars.forEachIndexed { index, car ->
            enemies.add(
                Opponent(
                    car = car,
                    x = when (index) {
                        0 -> -65f
                        1 -> 0f
                        else -> 65f
                    },
                    y = -180f - index * 160f,
                    speed = 80f + index * 15f
                )
            )
        }
    }

    fun update(delta: Float) {
        enemies.forEach { enemy ->
            enemy.y += enemy.speed * delta

            // Когда бот уезжает за экран,
            // переносим его далеко за горизонт.
            if (enemy.y > 1000f) {
                enemy.y -= 1600f
            }
        }
    }
}
