
package racing

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.math.floor
import kotlin.math.sin

fun DrawScope.drawTrack(
    distance: Float,
    playerOffset: Float
) {
    val w = size.width
    val h = size.height
    val horizon = h * 0.30f
    val segments = 110
    val visibleDistance = 750f

    val grass = Color(0xFF287A35)
    val darkGrass = Color(0xFF205D2C)
    val asphalt = Color(0xFF34383D)
    val red = Color(0xFFE53935)
    val white = Color.White

    // Небо
    drawRect(
        color = Color(0xFF75C9EF),
        size = Size(w, h)
    )

    drawRect(
        color = Color(0xFFB7E5F5),
        size = Size(w, horizon)
    )

    // Горы
    val mountain = Path().apply {
        moveTo(0f, horizon)

        lineTo(w * 0.12f, horizon * 0.35f)
        lineTo(w * 0.23f, horizon * 0.72f)
        lineTo(w * 0.39f, horizon * 0.20f)
        lineTo(w * 0.55f, horizon * 0.75f)
        lineTo(w * 0.72f, horizon * 0.28f)
        lineTo(w * 0.86f, horizon * 0.68f)
        lineTo(w, horizon * 0.35f)

        lineTo(w, horizon)
        close()
    }

    drawPath(
        path = mountain,
        color = Color(0xFF607D70)
    )

    // Солнце
    drawCircle(
        color = Color(0xFFFFF1A8),
        radius = w * 0.035f,
        center = Offset(w * 0.80f, horizon * 0.30f)
    )

    // Земля
    drawRect(
        color = grass,
        topLeft = Offset(0f, horizon),
        size = Size(w, h - horizon)
    )

    fun screenY(p: Float): Float {
        return horizon + p * (h - horizon)
    }

    // Форма бесконечной трассы.
    // Дорога меняет направление в зависимости от расстояния.
    fun curve(z: Float): Float {
        return sin(z * 0.0022f) * w * 0.16f +
                sin(z * 0.0008f) * w * 0.10f
    }

    // Камера находится около машины.
    // Передняя часть дороги строится относительно текущей позиции.

    fun center(p: Float): Float {
        val z = distance + (1f - p) * visibleDistance

        return w / 2f +
                (curve(z) - curve(distance)) * 0.75f -
                playerOffset * 0.55f
    }


    fun roadWidth(p: Float): Float {
        return w * (0.12f + p * 0.58f)
    }

    fun quad(
        x1: Float, y1: Float,
        x2: Float, y2: Float,
        x3: Float, y3: Float,
        x4: Float, y4: Float,
        color: Color
    ) {
        val path = Path().apply {
            moveTo(x1, y1)
            lineTo(x2, y2)
            lineTo(x3, y3)
            lineTo(x4, y4)
            close()
        }

        drawPath(path, color)
    }

    // Полосы травы двигаются относительно камеры
    for (i in 0 until segments) {
        val p1 = i.toFloat() / segments
        val p2 = (i + 1).toFloat() / segments

        val z = distance + (1f - p1) * visibleDistance
        val stripe = floor(z / 45f).toInt()

        if (stripe % 2 == 0) {
            drawRect(
                color = darkGrass,
                topLeft = Offset(0f, screenY(p1)),
                size = Size(
                    w,
                    screenY(p2) - screenY(p1) + 1f
                )
            )
        }
    }

    // Дорога и бордюры
    for (i in 0 until segments) {
        val p1 = i.toFloat() / segments
        val p2 = (i + 1).toFloat() / segments

        val y1 = screenY(p1)
        val y2 = screenY(p2)

        val c1 = center(p1)
        val c2 = center(p2)

        val rw1 = roadWidth(p1)
        val rw2 = roadWidth(p2)

        val l1 = c1 - rw1 / 2f
        val r1 = c1 + rw1 / 2f
        val l2 = c2 - rw2 / 2f
        val r2 = c2 + rw2 / 2f

        val curb1 = 5f + p1 * 15f
        val curb2 = 5f + p2 * 15f

        // Асфальт
        quad(
            l1, y1,
            r1, y1,
            r2, y2,
            l2, y2,
            asphalt
        )

        // Бордюры
        val curbColor = if (i % 2 == 0) red else white

        quad(
            l1 - curb1, y1,
            l1, y1,
            l2, y2,
            l2 - curb2, y2,
            curbColor
        )

        quad(
            r1, y1,
            r1 + curb1, y1,
            r2 + curb2, y2,
            r2, y2,
            curbColor
        )

        // Центральная разметка
        if (i % 12 < 6) {
            val line1 = 1.5f + p1 * 3f
            val line2 = 1.5f + p2 * 3f

            quad(
                c1 - line1, y1,
                c1 + line1, y1,
                c2 + line2, y2,
                c2 - line2, y2,
                white
            )
        }
    }

    // Деревья появляются впереди и уходят за камеру.
    val firstTree = floor(distance / 35f).toInt()
    val lastTree = floor(
        (distance + visibleDistance) / 35f
    ).toInt()

    for (index in firstTree..lastTree) {
        val z = index * 35f
        val p = 1f - (z - distance) / visibleDistance

        if (p !in 0f..1f) continue

        val y = screenY(p)
        val c = center(p)
        val rw = roadWidth(p)
        val treeSize = 7f + p * 34f

        for (side in listOf(-1f, 1f)) {
            val x = c + side * (
                    rw / 2f + treeSize * 1.5f
                    )

            // Ствол
            drawRect(
                color = Color(0xFF69452B),
                topLeft = Offset(
                    x - treeSize * 0.12f,
                    y - treeSize * 0.30f
                ),
                size = Size(
                    treeSize * 0.24f,
                    treeSize * 0.65f
                )
            )

            // Крона
            drawCircle(
                color = Color(0xFF164D27),
                radius = treeSize * 0.68f,
                center = Offset(
                    x,
                    y - treeSize * 0.60f
                )
            )

            drawCircle(
                color = Color(0xFF318B3D),
                radius = treeSize * 0.43f,
                center = Offset(
                    x - treeSize * 0.12f,
                    y - treeSize * 0.78f
                )
            )
        }
    }

    // Ограждения
    for (side in listOf(-1f, 1f)) {
        for (i in 0 until segments step 2) {
            val p1 = i.toFloat() / segments
            val p2 = (i + 2).toFloat() / segments

            val x1 = center(p1) +
                    side * (roadWidth(p1) / 2f + 15f)

            val x2 = center(p2) +
                    side * (roadWidth(p2) / 2f + 15f)

            drawLine(
                color = Color(0xFFB0BEC5),
                start = Offset(x1, screenY(p1) - 5f),
                end = Offset(x2, screenY(p2) - 5f),
                strokeWidth = 2f + p1 * 4f
            )
        }
    }
}
