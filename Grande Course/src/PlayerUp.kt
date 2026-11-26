package racing

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PlayerUp(player: Player, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = { TextButton(onClick = onDismiss) { Text("Ок") } },
        title = { Text("Персонаж") },
        text = {
            Column(Modifier.fillMaxWidth()) {
                Text("Ур. ${player.level}  ${player.exp}/${player.expNext}")
                Text("Монеты: ${player.coins}")
                Spacer(Modifier.height(12.dp))

                Row(
                    Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text("Реакция ${player.reaction}")
                    Button(
                        onClick = { player.tryUpgradeReaction() },
                        enabled = player.coins >= player.reactionCost,
                    ) { Text("${player.reactionCost}") }
                }

                Row(
                    Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text("Управление ${player.handling}")
                    Button(
                        onClick = { player.tryUpgradeHandling() },
                        enabled = player.coins >= player.handlingCost,
                    ) { Text("${player.handlingCost}") }
                }
            }
        },
    )
}