package racing

import java.io.File
import java.util.Properties

object Save {
    private val file = File(System.getProperty("user.home"), ".racing-game/save.properties")

    fun save(player: Player) {
        file.parentFile?.mkdirs()
        val props = Properties().apply {
            setProperty("coins", player.coins.toString())
            setProperty("exp", player.exp.toString())
            setProperty("level", player.level.toString())
            setProperty("expNext", player.expNext.toString())
            setProperty("handling", player.handling.toString())
            setProperty("reaction", player.reaction.toString())
            setProperty("car.engine", player.car.engine.toString())
            setProperty("car.tires", player.car.tires.toString())
            setProperty("car.brakes", player.car.brakes.toString())
        }
        file.outputStream().use { props.store(it, null) }
    }

    fun load(): Player {
        val player = Player()
        if (!file.exists()) return player
        val props = Properties().apply { file.inputStream().use { load(it) } }

        player.coins = props.getProperty("coins", "500").toInt()
        player.exp = props.getProperty("exp", "0").toInt()
        player.level = props.getProperty("level", "1").toInt()
        player.expNext = props.getProperty("expNext", "100").toInt()
        player.handling = props.getProperty("handling", "1").toInt()
        player.reaction = props.getProperty("reaction", "1").toInt()
        player.car.engine = props.getProperty("car.engine", "1").toInt()
        player.car.tires = props.getProperty("car.tires", "1").toInt()
        player.car.brakes = props.getProperty("car.brakes", "1").toInt()
        return player
    }
}