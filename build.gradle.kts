plugins {
    kotlin("jvm") version "1.9.21"
}

group = "io.github.janbarari"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    implementation("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}