package racing

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CarUp(player: Player, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = { TextButton(onClick = onDismiss) { Text("Ок") } },
        title = { Text("Машина") },
        text = {
            Column(Modifier.fillMaxWidth()) {
                Text("Монеты: ${player.coins}")
                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Двигатель ${player.car.engine}")
                    Button(onClick = {
                        val c = player.car.engineCost()
                        if (player.coins >= c) { player.coins -= c; player.car.engine++ }
                    }) { Text("${player.car.engineCost()}") }
                }
                Row(Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Шины ${player.car.tires}")
                    Button(onClick = {
                        val c = player.car.tiresCost()
                        if (player.coins >= c) { player.coins -= c; player.car.tires++ }
                    }) { Text("${player.car.tiresCost()}") }
                }
                Row(Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Тормоза ${player.car.brakes}")
                    Button(onClick = {
                        val c = player.car.brakesCost()
                        if (player.coins >= c) { player.coins -= c; player.car.brakes++ }
                    }) { Text("${player.car.brakesCost()}") }
                }
            }
        }
    )
}