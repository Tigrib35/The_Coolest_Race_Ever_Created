package racing

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.max


@Composable
fun Race(player: Player, mode: RaceMode, onFinish: (Int, Int) -> Unit) {
    val eng = remember {
        Engine(
            maxSpeed = player.car.maxSpeed() + player.reaction * 6f,
            grip = player.car.grip() + player.handling * 0.05f,
            braking = player.car.brakeForce()
        ).also { it.init(mode == RaceMode.BOTS) }
    }

    val focus = remember { FocusRequester() }
    val keys = remember { mutableStateOf<Set<Key>>(emptySet()) }

    var finished by remember { mutableStateOf(false) }
    var hudTime by remember { mutableFloatStateOf(0f) }
    var hudSpeed by remember { mutableFloatStateOf(0f) }
    var hudDist by remember { mutableFloatStateOf(0f) }
    var hudRank by remember { mutableIntStateOf(1) }

    LaunchedEffect(Unit) { focus.requestFocus() }

    LaunchedEffect(Unit) {
        var last = System.nanoTime()
        while (!eng.finished) {
            val now = System.nanoTime()
            val dt = ((now - last) / 1_000_000_000f).coerceAtMost(0.05f)
            last = now

            val k = keys.value
            val throttle = when {
                Key.W in k || Key.DirectionUp in k -> 1f
                Key.S in k || Key.DirectionDown in k -> -1f
                else -> 0f
            }
            val steer =
                (if (Key.D in k || Key.DirectionRight in k) 1f else 0f) -
                        (if (Key.A in k || Key.DirectionLeft in k) 1f else 0f)

            eng.update(dt, throttle, steer)

            hudTime = eng.time
            hudSpeed = eng.speed
            hudDist = eng.dist
            if (mode == RaceMode.BOTS) hudRank = eng.rank()

            delay(16L)
        }
        finished = true
    }

    Box(
        Modifier.fillMaxSize()
            .focusRequester(focus)
            .focusable()
            .onPreviewKeyEvent { e ->
                keys.value = when (e.type) {
                    KeyEventType.KeyDown -> keys.value + e.key
                    KeyEventType.KeyUp -> keys.value - e.key
                    else -> keys.value
                }
                true
            }
    ) {
        Canvas(Modifier.fillMaxSize()) {
            drawRect(Color(0xFF263238))

            val scroll = hudDist % 120f
            var y = -120f + scroll
            while (y < size.height) {
                drawRect(Color.White, Offset(size.width / 2 - 4f, y), Size(8f, 60f))
                y += 120f
            }

            eng.bots.forEach { b ->
                drawRoundRect(
                    Color(0xFF1E88E5),
                    Offset(size.width / 2 + b.x - 16f, size.height * 0.4f + b.y - 28f),
                    Size(32f, 56f),
                    CornerRadius(8f, 8f)
                )
            }

            drawRoundRect(
                Color(0xFFE53935),
                Offset(size.width / 2 - 16f, size.height * 0.75f - 28f),
                Size(32f, 56f),
                CornerRadius(8f, 8f)
            )
        }

        Column(Modifier.align(Alignment.TopStart).padding(20.dp)) {
            Text("Время: ${"%.2f".format(hudTime)}", color = Color.White)
            if (mode == RaceMode.BOTS) Text("Место: $hudRank/4", color = Color.White)
            Text("Скорость: ${"%.0f".format(hudSpeed)}", color = Color.White)
        }

        Button(
            onClick = { onFinish(0, 0) },
            modifier = Modifier.align(Alignment.TopEnd).padding(20.dp)
        ) { Text("Выход") }

        if (finished) {
            val reward = if (mode == RaceMode.TIME)
                100 + max(0, 200 - hudTime.toInt())
            else
                100 + (4 - hudRank) * 75
            val exp = 40 + (4 - hudRank) * 15

            AlertDialog(
                onDismissRequest = {},
                title = { Text("Финиш") },
                text = {
                    Column {
                        Text("Время: ${"%.2f".format(hudTime)}")
                        if (mode == RaceMode.BOTS) Text("Место: $hudRank/4")
                        Text("Монеты: +$reward")
                        Text("Опыт: +$exp")
                    }
                },
                confirmButton = {
                    Button(onClick = { onFinish(reward, exp) }) { Text("Ок") }
                }
            )
        }
    }
}