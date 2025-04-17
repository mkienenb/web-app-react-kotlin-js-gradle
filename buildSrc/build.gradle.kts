plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    // Use the Gradle Plugin API instead of the full plugin itself
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin-api:1.9.23")
}
