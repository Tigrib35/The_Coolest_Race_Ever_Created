package racing

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

enum class Screen {
    GARAGE, MODE, TRACKS, RACE
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Racing",
        state = rememberWindowState(
            width = 1280.dp,
            height = 800.dp
        )
    ) {
        MaterialTheme(
            colorScheme = darkColorScheme()
        ) {
            App()
        }
    }
}

@Composable
private fun App() {

    val player = remember {
        Save.load()
    }

    var screen by remember {
        mutableStateOf(Screen.GARAGE)
    }

    var mode by remember {
        mutableStateOf(RaceMode.TIME)
    }

    var selectedTrack by remember {
        mutableStateOf<Track>(Tracks.default)
    }

    LaunchedEffect(
        player.coins,
        player.level,
        player.exp,
        player.handling,
        player.reaction,
        player.car.engine,
        player.car.tires,
        player.car.brakes
    ) {
        Save.save(player)
    }

    when (screen) {

        Screen.GARAGE -> Garage(player) {
            screen = Screen.MODE
        }

        Screen.MODE -> Mode(
            onBack = {
                screen = Screen.GARAGE
            },
            onPick = { selectedMode ->
                mode = selectedMode
                screen = Screen.TRACKS
            }
        )

        Screen.TRACKS -> TrackSelect(
            player = player,
            onBack = {
                screen = Screen.MODE
            },
            onPick = { track ->
                selectedTrack = track
                screen = Screen.RACE
            }
        )

        Screen.RACE -> Race(
            player = player,
            mode = mode,
            track = selectedTrack,
            onFinish = { reward, exp ->

                player.coins += reward
                player.addExp(exp)

                Save.save(player)

                screen = Screen.GARAGE
            },
            onMenu = {
                screen = Screen.GARAGE
            }
        )
    }
}