plugins {
    kotlin("jvm") version "1.9.21"
    `maven-publish`
}

val artifact: String = "NooraEye"
group = "io.github.janbarari"
version = "0.1-beta1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = group as String
            artifactId = artifact
            version = version

            from(components["java"])
        }
    }
}
