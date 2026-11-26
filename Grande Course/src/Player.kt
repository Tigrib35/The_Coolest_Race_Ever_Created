package racing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue

class Player {
    var coins by mutableIntStateOf(500)
    var exp by mutableIntStateOf(0)
    var level by mutableIntStateOf(1)
    var expNext by mutableIntStateOf(100)

    var handling by mutableIntStateOf(1)
    var reaction by mutableIntStateOf(1)

    val car = Car()

    fun addExp(n: Int) {
        exp += n
        while (exp >= expNext) {
            exp -= expNext
            level++
            expNext = (expNext * 1.4f).toInt()
            coins += 50 * level
        }
    }

    fun handlingCost() = 100 * handling
    fun reactionCost() = 100 * reaction

    fun upHandling(): Boolean {
        val c = handlingCost()
        if (coins < c) return false
        coins -= c; handling++; return true
    }

    fun upReaction(): Boolean {
        val c = reactionCost()
        if (coins < c) return false
        coins -= c; reaction++; return true
    }
}