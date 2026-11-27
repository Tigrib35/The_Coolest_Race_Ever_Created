plugins {
    kotlin("jvm") version "2.0.20"
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.20" // Обязателен для Kotlin 2.0+
    id("org.jetbrains.compose") version "1.7.3"
}

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.material3)
}

kotlin {
    jvmToolchain(17)
}

compose.desktop {
    application {
        mainClass = "racing.MainKt"
    }
}
