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

    val handlingCost: Int get() = 100 * handling
    val reactionCost: Int get() = 100 * reaction

    fun gainExp(amount: Int) {
        exp += amount
        while (exp >= expNext) {
            exp -= expNext
            level++
            expNext = (expNext * 1.4f).toInt()
            coins += 50 * level
        }
    }

    fun tryUpgradeHandling(): Boolean {
        if (coins < handlingCost) return false
        coins -= handlingCost
        handling++
        return true
    }

    fun tryUpgradeReaction(): Boolean {
        if (coins < reactionCost) return false
        coins -= reactionCost
        reaction++
        return true
    }
}