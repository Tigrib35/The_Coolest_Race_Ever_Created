package racing

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
fun CarUp(player: Player, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = { TextButton(onClick = onDismiss) { Text("Ок") } },
        title = { Text("Машина") },
        text = {
            Column(Modifier.fillMaxWidth()) {
                Text("Монеты: ${player.coins}")
                Text("Макс. скорость: ${player.car.maxSpeed.toInt()}")
                Text("Сцепление: ${String.format(Locale.US, "%.2f", player.car.grip)}")
                Text("Тормоза: ${player.car.brakeForce.toInt()}")

                Spacer(Modifier.height(12.dp))

                UpgradeRow(
                    label = "Двигатель ${player.car.engine}",
                    cost = player.car.engineCost,
                    canAfford = player.coins >= player.car.engineCost,
                    onClick = {
                        val cost = player.car.engineCost
                        if (player.coins >= cost) {
                            player.coins -= cost
                            player.car.engine++
                        }
                    },
                )
                UpgradeRow(
                    label = "Шины ${player.car.tires}",
                    cost = player.car.tiresCost,
                    canAfford = player.coins >= player.car.tiresCost,
                    onClick = {
                        val cost = player.car.tiresCost
                        if (player.coins >= cost) {
                            player.coins -= cost
                            player.car.tires++
                        }
                    },
                )
                UpgradeRow(
                    label = "Тормоза ${player.car.brakes}",
                    cost = player.car.brakesCost,
                    canAfford = player.coins >= player.car.brakesCost,
                    onClick = {
                        val cost = player.car.brakesCost
                        if (player.coins >= cost) {
                            player.coins -= cost
                            player.car.brakes++
                        }
                    },
                )
            }
        },
    )
}

@Composable
private fun UpgradeRow(
    label: String,
    cost: Int,
    canAfford: Boolean,
    onClick: () -> Unit,
) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(label)
        Button(onClick = onClick, enabled = canAfford) { Text("$cost") }
    }
}