
package racing

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.unit.dp

@Composable
fun Garage(player: Player, onPlay: () -> Unit) {
    var showCarUp by remember { mutableStateOf(false) }
    var showPlayerUp by remember { mutableStateOf(false) }
    var showCarSelect by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize().padding(24.dp)) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Монеты: ${player.coins}")
            Text("Ур. ${player.level}  ${player.exp}/${player.expNext}")
        }

        Box(
            Modifier.weight(1f).fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val car = player.selectedCar

                val bitmap = remember(car.image) {
                    Thread.currentThread()
                        .contextClassLoader
                        .getResourceAsStream(car.image)
                        ?.use { loadImageBitmap(it) }
                }

                if (bitmap != null) {
                    Image(
                        bitmap = bitmap,
                        contentDescription = car.name,
                        modifier = Modifier.size(220.dp, 330.dp),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Text("Изображение не найдено: ${car.image}")
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    car.name,
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    car.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = onPlay,
                modifier = Modifier.weight(1f).height(64.dp)
            ) {
                Text("Играть")
            }

            Button(
                onClick = { showCarSelect = true },
                modifier = Modifier.weight(1f).height(64.dp)
            ) {
                Text("Автопарк")
            }

            Button(
                onClick = { showCarUp = true },
                modifier = Modifier.weight(1f).height(64.dp)
            ) {
                Text("Улучшения")
            }

            OutlinedButton(
                onClick = { showPlayerUp = true },
                modifier = Modifier.weight(1f).height(64.dp)
            ) {
                Text("Персонаж")
            }
        }
    }

    if (showCarSelect) {
        CarSelect(player) { showCarSelect = false }
    }

    if (showCarUp) {
        CarUp(player) { showCarUp = false }
    }

    if (showPlayerUp) {
        PlayerUp(player) { showPlayerUp = false }
    }
}
