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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale
import kotlin.math.max
import kotlin.math.sin

@Composable
fun RaceScreen(
    player: Player,
    mode: RaceMode,
    track: Track,
    onFinish: (reward: Int, exp: Int) -> Unit,
) {
    val engine = remember(track.id, mode) {
        Engine(
            maxSpeed = player.car.maxSpeed + player.reaction * 6f,
            grip = player.car.grip + player.handling * 0.05f,
            braking = player.car.brakeForce,
            track = track,
        ).apply { init(withBots = mode == RaceMode.BOTS) }
    }

    val focusRequester = remember { FocusRequester() }
    val pressedKeys = remember { mutableStateOf(emptySet<Key>()) }

    var hudSpeed by remember { mutableFloatStateOf(0f) }
    var hudTime by remember { mutableFloatStateOf(0f) }
    var hudProgress by remember { mutableFloatStateOf(0f) }
    var hudRank by remember { mutableIntStateOf(1) }
    var finished by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    LaunchedEffect(Unit) {
        var lastFrameNs = 0L
        var hasPrevious = false
        while (!engine.finished) {
            withFrameNanos { now ->
                if (hasPrevious) {
                    val dt = ((now - lastFrameNs) / 1_000_000_000f).coerceAtMost(0.05f)
                    val keys = pressedKeys.value

                    val throttle = when {
                        Key.W in keys || Key.DirectionUp in keys -> 1f
                        Key.S in keys || Key.DirectionDown in keys -> -1f
                        else -> 0f
                    }
                    val steer = when {
                        Key.D in keys || Key.DirectionRight in keys -> 1f
                        Key.A in keys || Key.DirectionLeft in keys -> -1f
                        else -> 0f
                    }

                    engine.update(dt, throttle, steer)
                    hudSpeed = engine.speed
                    hudTime = engine.elapsed
                    hudProgress = engine.progress
                    if (mode == RaceMode.BOTS) hudRank = engine.rank()
                }
                lastFrameNs = now
                hasPrevious = true
            }
        }
        finished = true
    }

    Box(
        Modifier.fillMaxSize()
            .focusRequester(focusRequester)
            .focusable()
            .onPreviewKeyEvent { event ->
                pressedKeys.value = when (event.type) {
                    KeyEventType.KeyDown -> pressedKeys.value + event.key
                    KeyEventType.KeyUp -> pressedKeys.value - event.key
                    else -> pressedKeys.value
                }
                true
            },
    ) {
        RaceCanvas(engine, track, hudProgress)

        Column(Modifier.align(Alignment.TopStart).padding(20.dp)) {
            HudText(track.name)
            HudText("Время: ${fmt(hudTime)} с")
            HudText("Скорость: ${hudSpeed.toInt()}")
            if (mode == RaceMode.BOTS) HudText("Место: $hudRank / 4")
            HudText("Дистанция: ${hudProgress.toInt()} / ${track.length.toInt()} м")
        }

        Text(
            "WASD / стрелки — газ и руль. S или ↓ — тормоз.",
            color = Color.White.copy(alpha = 0.55f),
            fontSize = 14.sp,
            modifier = Modifier.align(Alignment.BottomStart).padding(20.dp),
        )

        Button(
            onClick = { onFinish(0, 0) },
            modifier = Modifier.align(Alignment.TopEnd).padding(20.dp),
        ) { Text("Выход") }

        if (finished) {
            val baseReward = when (mode) {
                RaceMode.TIME -> 100 + max(0, 200 - hudTime.toInt())
                RaceMode.BOTS -> 100 + (4 - hudRank) * 75
            }
            val reward = (baseReward * track.rewardMultiplier).toInt()
            val baseExp = 40 + (4 - hudRank) * 15
            val exp = (baseExp * track.rewardMultiplier).toInt()

            AlertDialog(
                onDismissRequest = {},
                title = { Text("Финиш — ${track.name}") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Время: ${fmt(hudTime)} с")
                        if (mode == RaceMode.BOTS) Text("Место: $hudRank из 4")
                        HorizontalDivider(Modifier.padding(vertical = 8.dp))
                        Text("Монеты: +$reward", fontWeight = FontWeight.Bold)
                        Text("Опыт: +$exp", fontWeight = FontWeight.Bold)
                    }
                },
                confirmButton = {
                    Button(onClick = { onFinish(reward, exp) }) { Text("Забрать награду") }
                },
            )
        }
    }
}

@Composable
private fun HudText(text: String) {
    Text(text, color = Color.White, fontSize = 18.sp)
}

@Composable
private fun RaceCanvas(engine: Engine, track: Track, progress: Float) {
    val theme = track.theme
    Canvas(Modifier.fillMaxSize()) {
        drawRect(theme.ambient)

        // Визуальный изгиб для «извилистых» трасс
        val curve = if (theme.hasCurves) sin(progress * 0.004f) * 90f else 0f
        val centerX = size.width / 2 + curve

        // Разметка
        val scroll = progress % 120f
        var y = -120f + scroll
        while (y < size.height) {
            drawRect(theme.marking, Offset(centerX - 4f, y), Size(8f, 60f))
            y += 120f
        }

        val playerScreenY = size.height * 0.75f
        val pxPerMeter = 2f

        // Боты
        engine.bots.forEach { bot ->
            val relative = -bot.y - progress
            val botScreenY = playerScreenY - relative * pxPerMeter - 28f
            if (botScreenY in -60f..(size.height + 60f)) {
                drawRoundRect(
                    theme.accent,
                    Offset(centerX + bot.laneX - 16f, botScreenY),
                    Size(32f, 56f),
                    CornerRadius(8f, 8f),
                )
            }
        }

        // Игрок
        val playerOffsetX = engine.x.coerceIn(-350f, 350f)
        drawRoundRect(
            Color(0xFFE53935),
            Offset(centerX + playerOffsetX - 16f, playerScreenY - 28f),
            Size(32f, 56f),
            CornerRadius(8f, 8f),
        )
    }
}

private fun fmt(value: Float): String = String.format(Locale.US, "%.2f", value)