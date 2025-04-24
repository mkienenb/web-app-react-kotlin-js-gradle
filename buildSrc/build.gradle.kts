val kotlinVersion: String = "2.1.20"

plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    // Use the Gradle Plugin API instead of the full plugin itself
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin-api:$kotlinVersion")
//    implementation(libs.kotlin.gradle.plugin.api)
}
