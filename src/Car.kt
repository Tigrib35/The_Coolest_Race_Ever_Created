package racing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue

class Car {
    var engine by mutableIntStateOf(1)
    var tires by mutableIntStateOf(1)
    var brakes by mutableIntStateOf(1)

    fun engineCost() = 150 * engine
    fun tiresCost() = 120 * tires
    fun brakesCost() = 100 * brakes

    fun maxSpeed() = 240f + engine * 40f
    fun grip() = 0.9f + tires * 0.1f
    fun brakeForce() = 400f + brakes * 60f
}