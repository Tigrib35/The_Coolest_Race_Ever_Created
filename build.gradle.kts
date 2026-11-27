
plugins {
    kotlin("jvm") version "2.0.20"
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.20"
    id("org.jetbrains.compose") version "1.7.3"
}

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}
val jinputNatives by configurations.creating

dependencies {
    implementation("net.java.jinput:jinput:2.0.10")
    jinputNatives("net.java.jinput:jinput:2.0.10:natives-all")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.material3)

    implementation("net.java.jinput:jinput:2.0.10")

}

kotlin {
    jvmToolchain(17)
}

sourceSets {
    main {
        kotlin.srcDir("src")
        resources.srcDir("src/resources")
    }
}

compose.desktop {
    application {
        mainClass = "racing.MainKt"
    }
    }

    val extractJinputNatives by tasks.registering(Copy::class) {
        from({
            jinputNatives.map { zipTree(it) }
        })
        include("*.dll")
        into(layout.buildDirectory.dir("jinput-natives"))
    }

    tasks.withType<JavaExec>().configureEach {
        dependsOn(extractJinputNatives)

        systemProperty(
            "java.library.path",
            layout.buildDirectory.dir("jinput-natives").get().asFile.absolutePath
        )
    }

