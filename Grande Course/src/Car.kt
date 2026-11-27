<<<<<<< HEAD
package racing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue

class Car {
    var engine by mutableIntStateOf(1)
    var tires by mutableIntStateOf(1)
    var brakes by mutableIntStateOf(1)

    val engineCost: Int get() = 150 * engine
    val tiresCost: Int get() = 120 * tires
    val brakesCost: Int get() = 100 * brakes

    val maxSpeed: Float get() = 240f + engine * 40f
    val grip: Float get() = 0.9f + tires * 0.1f
    val brakeForce: Float get() = 400f + brakes * 60f
=======
package racing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue

class Car {
    var engine by mutableIntStateOf(1)
    var tires by mutableIntStateOf(1)
    var brakes by mutableIntStateOf(1)

    val engineCost: Int get() = 150 * engine
    val tiresCost: Int get() = 120 * tires
    val brakesCost: Int get() = 100 * brakes

    val maxSpeed: Float get() = 240f + engine * 40f
    val grip: Float get() = 0.9f + tires * 0.1f
    val brakeForce: Float get() = 400f + brakes * 60f
>>>>>>> 8b156d369d27faeb82cfbb970c835c7b2634cdad
}