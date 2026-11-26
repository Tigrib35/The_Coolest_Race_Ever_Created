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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Garage(player: Player, onPlay: () -> Unit) {
    var showCarUp by remember { mutableStateOf(false) }
    var showPlayerUp by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize().padding(24.dp)) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Монеты: ${player.coins}", style = MaterialTheme.typography.headlineSmall)
            Text("Ур. ${player.level}  ${player.exp}/${player.expNext}",
                style = MaterialTheme.typography.titleMedium)
        }

        Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
            CarView()
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = onPlay, modifier = Modifier.weight(1f).height(64.dp)) {
                Text("Играть", style = MaterialTheme.typography.titleMedium)
            }
            Button(onClick = { showCarUp = true }, modifier = Modifier.weight(1f).height(64.dp)) {
                Text("Машина", style = MaterialTheme.typography.titleMedium)
            }
            OutlinedButton(onClick = { showPlayerUp = true },
                modifier = Modifier.weight(1f).height(64.dp)) {
                Text("Персонаж", style = MaterialTheme.typography.titleMedium)
            }
        }
    }

    if (showCarUp) CarUp(player) { showCarUp = false }
    if (showPlayerUp) PlayerUp(player) { showPlayerUp = false }
}

@Composable
private fun CarView() {
    Canvas(Modifier.size(200.dp, 300.dp)) {
        val w = size.width; val h = size.height
        drawRoundRect(
            Color(0xFFE53935),
            Offset(w * 0.15f, 0f),
            Size(w * 0.7f, h),
            CornerRadius(32f, 32f)
        )
        drawRoundRect(
            Color(0xFF90CAF9),
            Offset(w * 0.25f, h * 0.25f),
            Size(w * 0.5f, h * 0.2f),
            CornerRadius(16f, 16f)
        )
        drawRoundRect(
            Color(0xFF90CAF9),
            Offset(w * 0.25f, h * 0.6f),
            Size(w * 0.5f, h * 0.18f),
            CornerRadius(16f, 16f)
        )
        drawCircle(Color.Yellow, w * 0.08f, Offset(w * 0.3f, h * 0.06f))
        drawCircle(Color.Yellow, w * 0.08f, Offset(w * 0.7f, h * 0.06f))
    }
}