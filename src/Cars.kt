

package racing

data class CarInfo(
    val id: String,
    val name: String,
    val description: String,
    val maxSpeed: Float,
    val acceleration: Float,
    val handling: Float,
    val image: String
)

val cars = listOf(
    CarInfo(
        id = "black",
        name = "SHADOW",
        description = "Тёмный хищник. Отлично держит дорогу и уверенно проходит повороты.",
        maxSpeed = 220f,
        acceleration = 7f,
        handling = 9f,
        image = "cars/black_car.png"
    ),
    CarInfo(
        id = "green",
        name = "VENOM",
        description = "Агрессивный автомобиль с мощным ускорением.",
        maxSpeed = 230f,
        acceleration = 9f,
        handling = 6f,
        image = "cars/green_car.png"
    ),
    CarInfo(
        id = "red",
        name = "INFERNO",
        description = "Настоящий уличный хищник с высокой максимальной скоростью.",
        maxSpeed = 260f,
        acceleration = 8f,
        handling = 7f,
        image = "cars/red_car.png"
    ),
    CarInfo(
        id = "yellow",
        name = "FLASH",
        description = "Лёгкий и манёвренный автомобиль с быстрым ускорением.",
        maxSpeed = 210f,
        acceleration = 10f,
        handling = 9f,
        image = "cars/yellow_car.png"
    )
)
