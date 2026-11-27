
package racing

import java.io.File
import java.util.Properties

object Save {
    private val file = File(
        System.getProperty("user.home"),
        ".racing-game/save.properties"
    )

    fun save(p: Player) {
        file.parentFile?.mkdirs()

        val pr = Properties()
        pr["coins"] = p.coins.toString()
        pr["exp"] = p.exp.toString()
        pr["level"] = p.level.toString()
        pr["expNext"] = p.expNext.toString()
        pr["handling"] = p.handling.toString()
        pr["reaction"] = p.reaction.toString()

        pr["selectedCarId"] = p.selectedCarId

        pr["engine"] = p.car.engine.toString()
        pr["tires"] = p.car.tires.toString()
        pr["brakes"] = p.car.brakes.toString()

        file.outputStream().use {
            pr.store(it, null)
        }
    }

    fun load(): Player {
        val p = Player()
        if (!file.exists()) return p

        val pr = Properties()
        file.inputStream().use {
            pr.load(it)
        }

        p.coins = pr.getProperty("coins", "500").toInt()
        p.exp = pr.getProperty("exp", "0").toInt()
        p.level = pr.getProperty("level", "1").toInt()
        p.expNext = pr.getProperty("expNext", "100").toInt()
        p.handling = pr.getProperty("handling", "1").toInt()
        p.reaction = pr.getProperty("reaction", "1").toInt()

        p.selectedCarId = pr.getProperty("selectedCarId", "black")

        p.car.engine = pr.getProperty("engine", "1").toInt()
        p.car.tires = pr.getProperty("tires", "1").toInt()
        p.car.brakes = pr.getProperty("brakes", "1").toInt()

        return p
    }
}
