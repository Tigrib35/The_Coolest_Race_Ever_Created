package racing

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

private sealed interface Screen {
    data object Garage : Screen
    data object ModePick : Screen
    data class TrackPick(val mode: RaceMode) : Screen
    data class InRace(val mode: RaceMode, val track: Track) : Screen
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Racing",
        state = rememberWindowState(width = 1280.dp, height = 800.dp),
    ) {
        MaterialTheme(colorScheme = darkColorScheme()) { App() }
    }
}

@Composable
private fun App() {
    val player = remember { Save.load() }
    var screen: Screen by remember { mutableStateOf(Screen.Garage) }

    LaunchedEffect(
        player.coins, player.exp, player.level, player.expNext,
        player.handling, player.reaction,
        player.car.engine, player.car.tires, player.car.brakes,
    ) {
        Save.save(player)
    }

    when (val current = screen) {
        Screen.Garage -> GarageScreen(
            player = player,
            onPlay = { screen = Screen.ModePick },
        )

        Screen.ModePick -> ModeScreen(
            onBack = { screen = Screen.Garage },
            onPick = { screen = Screen.TrackPick(it) },   // ← ведём на выбор трассы
        )

        is Screen.TrackPick -> TrackSelectScreen(
            player = player,
            mode = current.mode,
            onBack = { screen = Screen.ModePick },
            onPick = { track -> screen = Screen.InRace(current.mode, track) },
        )

        is Screen.InRace -> RaceScreen(
            player = player,
            mode = current.mode,
            track = current.track,
            onFinish = { reward, exp ->
                player.coins += reward
                player.gainExp(exp)
                screen = Screen.Garage
            },
        )
    }
}