
package racing

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.input.key.*
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import kotlin.math.abs
import kotlin.math.max

private fun loadExplosionFrames(): List<ImageBitmap> = runCatching {
    val stream = Thread.currentThread()
        .contextClassLoader
        .getResourceAsStream("explosion.gif")
        ?: return@runCatching emptyList()

    stream.use { inputStream ->
        val imageInput = ImageIO.createImageInputStream(inputStream)
            ?: return@runCatching emptyList()

        imageInput.use { imageStream ->
            val readers = ImageIO.getImageReaders(imageStream)

            if (!readers.hasNext()) {
                return@runCatching emptyList()
            }

            val reader = readers.next()

            try {
                reader.input = imageStream

                val count = reader.getNumImages(true)

                (0 until count).map { index ->
                    reader.read(index).toComposeImageBitmap()
                }
            } finally {
                reader.dispose()
            }
        }
    }
}.getOrDefault(emptyList())


@Composable
fun Race(
    player: Player,
    mode: RaceMode,
    track: Track,
    onFinish: (Int, Int) -> Unit,
    onMenu: () -> Unit
) {
    var restartKey by remember { mutableIntStateOf(0) }

    var crashed by remember { mutableStateOf(false) }
    var showCrashScreen by remember { mutableStateOf(false) }
    var explosionFrame by remember { mutableIntStateOf(0) }

    val explosionFrames = remember {
        loadExplosionFrames()
    }

    val eng = remember(track.id, player.selectedCarId, restartKey) {
        Engine(
            maxSpeed = player.car.maxSpeed() + player.reaction * 6f,
            grip = (player.car.grip() + player.handling * 0.05f) *
                    track.gripMultiplier,
            braking = player.car.brakeForce()
        ).also {
            it.init(mode == RaceMode.BOTS)
        }
    }

    val opponents = remember(player.selectedCarId, restartKey) {
        Opponents(player.selectedCarId)
    }

    val carBitmap = remember(player.selectedCarId) {
        runCatching {
            Thread.currentThread()
                .contextClassLoader
                .getResourceAsStream(player.selectedCar.image)
                ?.use { loadImageBitmap(it) }
        }.getOrNull()
    }

    val opponentBitmaps = remember(player.selectedCarId) {
        cars.filter { it.id != player.selectedCarId }
            .mapNotNull { car ->
                runCatching {
                    Thread.currentThread()
                        .contextClassLoader
                        .getResourceAsStream(car.image)
                        ?.use { loadImageBitmap(it) }
                }.getOrNull()?.let {
                    car.id to it
                }
            }.toMap()
    }

    val focus = remember { FocusRequester() }
    val keys = remember { mutableStateOf<Set<Key>>(emptySet()) }

    var finished by remember { mutableStateOf(false) }
    var hudTime by remember { mutableFloatStateOf(0f) }
    var hudSpeed by remember { mutableFloatStateOf(0f) }
    var hudDist by remember { mutableFloatStateOf(0f) }
    var hudRank by remember { mutableIntStateOf(1) }

    LaunchedEffect(Unit) {
        focus.requestFocus()
    }

    // Игровой цикл
    LaunchedEffect(eng) {
        var last = System.nanoTime()

        while (!eng.finished && !eng.crashed) {
            val now = System.nanoTime()

            val dt = ((now - last) / 1_000_000_000f)
                .coerceAtMost(0.05f)

            last = now

            val k = keys.value

            val throttle = when {
                Key.W in k || Key.DirectionUp in k -> 1f
                Key.S in k || Key.DirectionDown in k -> -1f
                else -> 0f
            }

            val steer = when {
                (Key.D in k || Key.DirectionRight in k) &&
                        (Key.A in k || Key.DirectionLeft in k) -> 0f

                Key.D in k || Key.DirectionRight in k -> 1f

                Key.A in k || Key.DirectionLeft in k -> -1f

                else -> 0f
            }

            eng.update(dt, throttle, steer)

            opponents.update(dt)

            // Проверка столкновения с соперниками
            if (mode == RaceMode.BOTS) {
                val collision = opponents.enemies.any { enemy ->
                    val progress = ((enemy.y + 600f) / 1600f)
                        .coerceIn(0f, 1f)

                    val scale = 0.35f + progress * 0.65f

                    val verticalHit =
                        abs(enemy.y - 800f) < 220f

                    val horizontalHit =
                        abs(enemy.x * scale - eng.x * 0.35f) < 55f

                    verticalHit && horizontalHit
                }

                if (collision) {
                    eng.crash()
                }
            }

            hudTime = eng.time
            hudSpeed = eng.speed
            hudDist = eng.dist

            if (mode == RaceMode.BOTS) {
                hudRank = eng.rank()
            }

            delay(16L)
        }

        // Разделяем аварию и обычный финиш
        if (eng.crashed) {
            crashed = true
        } else if (eng.finished) {
            finished = true
        }
    }

    // Проигрывание взрыва после аварии
    LaunchedEffect(crashed, restartKey) {
        if (crashed) {
            showCrashScreen = false
            explosionFrame = 0

            if (explosionFrames.isNotEmpty()) {
                for (i in explosionFrames.indices) {
                    explosionFrame = i
                    delay(70L)
                }
            } else {
                // Если GIF не найден, всё равно показываем экран аварии
                delay(700L)
            }

            showCrashScreen = true
        } else {
            showCrashScreen = false
            explosionFrame = 0
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .focusRequester(focus)
            .focusable()
            .onPreviewKeyEvent { e ->
                keys.value = when (e.type) {
                    KeyEventType.KeyDown ->
                        keys.value + e.key

                    KeyEventType.KeyUp ->
                        keys.value - e.key

                    else -> keys.value
                }

                true
            }
    ) {

        Canvas(Modifier.fillMaxSize()) {

            drawTrack(
                distance = eng.dist,
                playerOffset = eng.x
            )

            // Машины соперников
            opponents.enemies.forEach { enemy ->

                val bitmap = opponentBitmaps[enemy.car.id]

                val progress = ((enemy.y + 600f) / 1600f)
                    .coerceIn(0f, 1f)

                val scale = 0.35f + progress * 0.65f

                val centerX = size.width / 2f + enemy.x * scale
                val screenY = size.height * (0.30f + progress * 0.55f)

                if (bitmap != null) {
                    drawImage(
                        image = bitmap,
                        dstOffset = IntOffset(
                            (centerX - 24f * scale).toInt(),
                            (screenY - 42f * scale).toInt()
                        ),
                        dstSize = IntSize(
                            (48f * scale).toInt().coerceAtLeast(1),
                            (84f * scale).toInt().coerceAtLeast(1)
                        )
                    )
                } else {
                    drawRoundRect(
                        color = Color(0xFF1E88E5),
                        topLeft = Offset(
                            centerX - 16f * scale,
                            screenY - 28f * scale
                        ),
                        size = Size(
                            32f * scale,
                            56f * scale
                        ),
                        cornerRadius = CornerRadius(8f * scale)
                    )
                }
            }

            // Машина игрока
            val carY = size.height * 0.78f
            val playerX = size.width / 2f + eng.x * 0.35f

            val carWidth = 64f
            val carHeight = 112f

            if (!crashed) {

                if (carBitmap != null) {
                    drawImage(
                        image = carBitmap,
                        dstOffset = IntOffset(
                            (playerX - carWidth / 2f).toInt(),
                            (carY - carHeight / 2f).toInt()
                        ),
                        dstSize = IntSize(
                            carWidth.toInt(),
                            carHeight.toInt()
                        )
                    )
                } else {
                    drawRoundRect(
                        color = Color(0xFFE53935),
                        topLeft = Offset(
                            playerX - carWidth / 2f,
                            carY - carHeight / 2f
                        ),
                        size = Size(carWidth, carHeight),
                        cornerRadius = CornerRadius(10f)
                    )
                }

            } else if (!showCrashScreen && explosionFrames.isNotEmpty()) {

                // Анимация взрыва на месте машины
                val frame = explosionFrames[
                    explosionFrame.coerceIn(0, explosionFrames.lastIndex)
                ]

                drawImage(
                    image = frame,
                    dstOffset = IntOffset(
                        (playerX - 90f).toInt(),
                        (carY - 90f).toInt()
                    ),
                    dstSize = IntSize(180, 180)
                )
            }
        }

        // HUD
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(20.dp)
        ) {
            Text(
                text = "Трасса: ${track.name}",
                color = Color.White
            )

            Text(
                text = "Время: ${"%.2f".format(hudTime)}",
                color = Color.White
            )

            if (mode == RaceMode.BOTS) {
                Text(
                    text = "Место: $hudRank/4",
                    color = Color.White
                )
            }

            Text(
                text = "Скорость: ${"%.0f".format(hudSpeed)}",
                color = Color.White
            )

            Text(
                text = "Дистанция: ${"%.0f".format(hudDist)} м",
                color = Color.White
            )
        }

        // Кнопка выхода из гонки
        if (!crashed && !finished) {
            Button(
                onClick = onMenu,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(20.dp)
            ) {
                Text("Выход")
            }
        }

        // Экран аварии
        if (showCrashScreen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.84f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "ДОЕЗДИЛСЯ!",
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFB71C1C)
                    )

                    Spacer(Modifier.height(35.dp))

                    Button(
                        onClick = {
                            crashed = false
                            finished = false
                            showCrashScreen = false
                            explosionFrame = 0

                            hudTime = 0f
                            hudSpeed = 0f
                            hudDist = 0f
                            hudRank = 1

                            keys.value = emptySet()

                            restartKey++
                        },
                        modifier = Modifier.width(190.dp)
                    ) {
                        Text(
                            text = "Сначала",
                            fontSize = 18.sp
                        )
                    }

                    Spacer(Modifier.height(15.dp))

                    Button(
                        onClick = onMenu,
                        modifier = Modifier.width(190.dp)
                    ) {
                        Text(
                            text = "Выход",
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }

        // Окно финиша
        if (finished && !crashed) {

            val baseReward = if (mode == RaceMode.TIME) {
                100 + max(0, 200 - hudTime.toInt())
            } else {
                100 + (4 - hudRank) * 75
            }

            val reward = (
                    baseReward * track.rewardMultiplier
                    ).toInt()

            val exp = 40 + (4 - hudRank) * 15

            AlertDialog(
                onDismissRequest = {},

                title = {
                    Text("Финиш!")
                },

                text = {
                    Column {
                        Text("Трасса: ${track.name}")

                        Text(
                            "Время: ${"%.2f".format(hudTime)}"
                        )

                        if (mode == RaceMode.BOTS) {
                            Text("Место: $hudRank/4")
                        }

                        Text("Монеты: +$reward")
                        Text("Опыт: +$exp")
                    }
                },

                confirmButton = {
                    Button(
                        onClick = {
                            onFinish(reward, exp)
                        }
                    ) {
                        Text("Ок")
                    }
                }
            )
        }
    }
}
