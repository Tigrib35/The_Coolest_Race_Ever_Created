package racing

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TrackSelectScreen(
    player: Player,
    mode: RaceMode,
    onBack: () -> Unit,
    onPick: (Track) -> Unit,
) {
    Column(
        Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text("Выбор трассы", style = MaterialTheme.typography.headlineMedium)
                Text(
                    if (mode == RaceMode.TIME) "Режим: На время"
                    else "Режим: С ботами",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            AssistChip(
                onClick = onBack,
                label = { Text("Назад") },
            )
        }

        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Tracks.all.forEach { track ->
                TrackCard(
                    track = track,
                    unlocked = player.level >= track.requiredLevel,
                    onClick = { onPick(track) },
                )
            }
        }
    }
}

@Composable
private fun TrackCard(track: Track, unlocked: Boolean, onClick: () -> Unit) {
    Card(
        Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = track.theme.asphalt),
    ) {
        Row(
            Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TrackPreview(track, Modifier.size(96.dp, 72.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    track.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                )
                Text(
                    track.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.75f),
                )
                Spacer(Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    StatLabel("Длина", "${track.length.toInt()} м")
                    StatLabel("Сложность", "★".repeat(track.difficulty))
                    StatLabel("Награда", "×${track.rewardMultiplier}")
                }
            }

            if (unlocked) {
                Button(onClick = onClick) { Text("Поехали") }
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🔒", fontSize = 24.sp)
                    Text(
                        "Ур. ${track.requiredLevel}",
                        color = Color.White.copy(alpha = 0.6f),
                    )
                }
            }
        }
    }
}

@Composable
private fun StatLabel(label: String, value: String) {
    Column {
        Text(label, color = Color.White.copy(alpha = 0.5f), fontSize = 11.sp)
        Text(value, color = Color.White, fontSize = 13.sp)
    }
}

@Composable
private fun TrackPreview(track: Track, modifier: Modifier = Modifier) {
    Canvas(modifier.clip(RoundedCornerShape(8.dp))) {
        drawRect(track.theme.ambient)
        drawRect(
            track.theme.asphalt,
            Offset(size.width * 0.2f, 0f),
            Size(size.width * 0.6f, size.height),
        )
        val step = 18f
        var y = 0f
        while (y < size.height) {
            drawRect(
                track.theme.marking,
                Offset(size.width / 2 - 2f, y),
                Size(4f, 10f),
            )
            y += step
        }
    }
}