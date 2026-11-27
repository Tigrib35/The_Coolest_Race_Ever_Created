package racing

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

enum class RaceMode { TIME, BOTS }

@Composable
fun ModeScreen(onBack: () -> Unit, onPick: (RaceMode) -> Unit) {
    Column(
        Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Режим", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(48.dp))

        Button(
            onClick = { onPick(RaceMode.TIME) },
            modifier = Modifier.fillMaxWidth(0.5f).height(72.dp),
        ) { Text("На время", style = MaterialTheme.typography.titleLarge) }

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = { onPick(RaceMode.BOTS) },
            modifier = Modifier.fillMaxWidth(0.5f).height(72.dp),
        ) { Text("С ботами", style = MaterialTheme.typography.titleLarge) }

        Spacer(Modifier.height(48.dp))

        OutlinedButton(onClick = onBack) { Text("Назад") }
    }
}