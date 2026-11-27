
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
fun CarSelect(player: Player, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Выбор автомобиля") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                cars.forEach { car ->
                    val selected = player.selectedCarId == car.id

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (selected)
                                MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
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
                                    modifier = Modifier.size(65.dp, 90.dp),
                                    contentScale = ContentScale.Fit
                                )
                            } else {
                                Text("Нет фото", modifier = Modifier.width(65.dp))
                            }

                            Spacer(Modifier.width(12.dp))

                            Column(Modifier.weight(1f)) {
                                Text(
                                    car.name,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    car.description,
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text("Скорость: ${car.maxSpeed.toInt()}")
                                Text("Ускорение: ${car.acceleration.toInt()}/10")
                                Text("Управление: ${car.handling.toInt()}/10")
                            }

                            if (selected) {
                                Text("✓")
                            } else {
                                TextButton(
                                    onClick = {
                                        player.selectedCarId = car.id
                                        Save.save(player)
                                    }
                                ) {
                                    Text("Выбрать")
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Готово")
            }
        }
    )
}
