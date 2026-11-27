
package racing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TrackSelect(
    player: Player,
    onBack: () -> Unit,
    onPick: (Track) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF10151D))
            .padding(28.dp)
    ) {
        Text(
            text = "ВЫБОР ТРАССЫ",
            fontSize = 32.sp,
            color = Color.White
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Твой уровень: ${player.level}",
            color = Color.LightGray
        )

        Spacer(Modifier.height(24.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(Tracks.all) { track ->
                val unlocked = player.level >= track.requiredLevel

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color(0xFF202A38),
                            RoundedCornerShape(14.dp)
                        )
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = track.name,
                            color = Color.White,
                            fontSize = 22.sp
                        )

                        Spacer(Modifier.height(5.dp))

                        Text(
                            text = track.description,
                            color = Color.LightGray
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = "Сложность: ${track.difficulty}/5",
                            color = track.theme.accent
                        )

                        Text(
                            text = "Нужен уровень: ${track.requiredLevel}",
                            color = Color.Gray
                        )

                        Text(
                            text = "Награда: x${track.rewardMultiplier}",
                            color = Color(0xFFFFD54F)
                        )
                    }

                    if (unlocked) {
                        Button(
                            onClick = { onPick(track) }
                        ) {
                            Text("ВЫБРАТЬ")
                        }
                    } else {
                        Text(
                            text = "ЗАКРЫТО",
                            color = Color(0xFFFF7777)
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("НАЗАД")
        }
    }
}
