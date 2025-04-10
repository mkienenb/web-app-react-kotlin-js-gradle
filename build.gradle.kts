import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import org.gradle.api.GradleException

// Shared variable for the startup timeout (in seconds) as a Long.
val reactAppStartupTimeoutInSeconds: Long = 20

plugins {
    kotlin("multiplatform") version "1.8.22"
    kotlin("plugin.serialization") version "1.8.22"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvm { }
    js {
        browser {
            // Configure the browser DSL to disable auto-opening when the org.example.headless property is set.
            browser {
                commonWebpackConfig {
                    // Let the dev server pick a random port.
                    devServer?.port = 0
                    devServer?.open = !project.hasProperty("org.example.headless")
                }
            }
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
            }
        }
        binaries.executable()
    }
    sourceSets {
        val jsMain by getting {
            dependencies {
                //React, React DOM + Wrappers (chapter 3)
                implementation(enforcedPlatform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:1.0.0-pre.430"))
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom")

                //Kotlin React Emotion (CSS) (chapter 3)
                implementation("org.jetbrains.kotlin-wrappers:kotlin-emotion")

                //Video Player (chapter 7)
                implementation(npm("react-player", "2.12.0"))

                //Share Buttons (chapter 7)
                implementation(npm("react-share", "4.4.1"))

                //Coroutines & serialization (chapter 8)
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
            }
        }
        // Our JVM tests (integration tests) reside in src/jvmTest.
        // (Client-side tests for Kotlin/JS would live in src/jsTest.)
        val jvmTest by getting {
            dependencies {
                implementation(platform("org.junit:junit-bom:5.12.1"))
                implementation(platform("io.cucumber:cucumber-bom:7.22.0"))
                implementation(platform("org.assertj:assertj-bom:3.27.3"))
                implementation(platform("org.seleniumhq.selenium:selenium-dependencies-bom:4.31.0"))

                implementation("io.cucumber:cucumber-java")
                implementation("io.cucumber:cucumber-junit-platform-engine")

                implementation("org.junit.jupiter:junit-jupiter-api")
                implementation("org.junit.platform:junit-platform-suite")

                implementation("org.assertj:assertj-core")

                // Selenium dependency for browser automation in tests.
                implementation("org.seleniumhq.selenium:selenium-java")
            }
        }
    }
}

// Global variables to manage the React app process and port.
var reactProcess: Process? = null
var reactStarted = false
var discoveredPort: Int? = null

// Custom task to start the React app before running JVM integration tests.
tasks.register("startReactApp") {
    group = "integration"
    description = "Starts the React app for integration testing using jsBrowserDevelopmentRun in headless mode with a random port"

    doLast {
        println("Starting React app in headless mode on a random port...")
        // Launch the React app with the org.example.headless property.
        val process = ProcessBuilder("./gradlew", "jsBrowserDevelopmentRun", "-Porg.example.headless=true")
            .directory(file(projectDir))
            .redirectErrorStream(true)
            .start()
        reactProcess = process
        reactStarted = true
        val pid = process.pid()
        println("React app started with PID: $pid")
        logger.lifecycle("React app started successfully.")

        // Capture output to discover the randomly chosen port using a coroutine.
        val portRegex = Regex("http://localhost:(\\d+)")
        kotlinx.coroutines.GlobalScope.launch(Dispatchers.IO) {
            process.inputStream.bufferedReader().forEachLine { line ->
                println(line) // Log the output.
                if (discoveredPort == null) {
                    val match = portRegex.find(line)
                    if (match != null) {
                        val portStr = match.groupValues[1]
                        discoveredPort = portStr.toIntOrNull()
                        if (discoveredPort != null) {
                            println("Detected React app running on port: $discoveredPort")
                            // Store the discovered port as a project extra.
                            project.extensions.extraProperties["org.example.reactPort"] = discoveredPort!!
                        }
                    }
                }
            }
        }

        // Wait for the port to be discovered with a timeout.
        runBlocking {
            try {
                withTimeout(reactAppStartupTimeoutInSeconds * 1000) {
                    while (discoveredPort == null) {
                        delay(100)
                    }
                }
            } catch (e: TimeoutCancellationException) {
                throw GradleException("React port was not discovered within $reactAppStartupTimeoutInSeconds seconds!")
            }
        }

        // Now poll for the React app to be responsive using the discovered port with a timeout.
        println("Waiting for React app to be responsive on port $discoveredPort...")
        val url = URL("http://localhost:$discoveredPort")
        var ready = false
        runBlocking {
            try {
                withTimeout(reactAppStartupTimeoutInSeconds * 1000) {
                    while (!ready) {
                        try {
                            (url.openConnection() as HttpURLConnection).apply {
                                connectTimeout = 2000
                                readTimeout = 2000
                                requestMethod = "GET"
                                connect()
                                if (responseCode == 200) {
                                    ready = true
                                    println("React app is responsive on port $discoveredPort!")
                                }
                            }
                        } catch (_: Exception) {
                            // Ignore exceptions while waiting for responsiveness.
                        }
                        if (!ready) {
                            delay(100)
                        }
                    }
                }
            } catch (e: TimeoutCancellationException) {
                throw GradleException("React app did not become responsive within $reactAppStartupTimeoutInSeconds seconds on port $discoveredPort")
            }
        }
    }
}

gradle.buildFinished {
    if (reactStarted && reactProcess != null && reactProcess!!.isAlive) {
        println("Stopping React app with PID: ${reactProcess!!.pid()}")
        reactProcess!!.destroy()
        if (!reactProcess!!.waitFor(10, TimeUnit.SECONDS)) {
            println("Force killing React app with PID: ${reactProcess!!.pid()}")
            reactProcess!!.destroyForcibly()
        }
    }
}

// Configure the test tasks to pass the discovered port to Cucumber tests.
tasks.withType<Test>().configureEach {
    useJUnitPlatform()

    // Enable full Cucumber debug output.
    systemProperty("cucumber.execution.verbose", "true")

    doFirst {
        if (!project.extensions.extraProperties.has("org.example.reactPort")) {
            throw GradleException("React port was not discovered!")
        }
        systemProperty("org.example.reactPort", project.extensions.extraProperties["org.example.reactPort"] as Int)
        println("Passing org.example.reactPort=${project.extensions.extraProperties["org.example.reactPort"]} to tests")
    }

    testLogging {
        showStandardStreams = true
        events("passed", "skipped", "failed", "standard_out", "standard_error")
    }
}

// Ensure JVM tests depend on starting the React app.
tasks.named("jvmTest") {
    dependsOn("startReactApp")
}

// Heroku Deployment (chapter 9)
tasks.register("stage") {
    dependsOn("build")
}