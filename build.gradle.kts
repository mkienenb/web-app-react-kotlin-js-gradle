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
val kotestVersion = "5.9.1"

plugins {
    kotlin("multiplatform") version "1.9.23"
    kotlin("plugin.serialization") version "1.9.23"
    id("io.kotest.multiplatform") version "5.9.1"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvm { }
    js(IR) {
        browser {
            testTask {
                useKarma {
                    useChromeHeadless()
                }
            }
            commonWebpackConfig {
                // Let the dev server pick a random port.
                devServer?.port = 0
                // Configure the browser DSL to disable auto-opening when the org.example.headless property is set.
                devServer?.open = !project.hasProperty("org.example.headless")
                cssSupport {
                    enabled.set(true)
                }
            }
        }
        binaries.executable()
    }
    sourceSets {
        val kotlinWrapperVersion = "733"
        val kotlinxCoroutinesVersion = "1.7.3"

        val commonTest by getting {
            dependencies {
                implementation("io.kotest:kotest-assertions-core:$kotestVersion")
                implementation("io.kotest:kotest-framework-engine:$kotestVersion")
                implementation("io.kotest:kotest-framework-datatest:$kotestVersion")
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(project.dependencies.enforcedPlatform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:1.0.0-pre.$kotlinWrapperVersion"))

                //React, React DOM + Wrappers (chapter 3)
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-tanstack-react-query")

                //Kotlin React Emotion (CSS) (chapter 3)
                implementation("org.jetbrains.kotlin-wrappers:kotlin-emotion")

                //Video Player (chapter 7)
                implementation(npm("react-player", "2.12.0"))

                //Share Buttons (chapter 7)
                implementation(npm("react-share", "4.4.1"))

                //Coroutines & serialization (chapter 8)
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
            }
        }
        // (Client-side tests for Kotlin/JS would live in src/jsTest.)
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom-test-utils-js:18.2.0-pre.$kotlinWrapperVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinxCoroutinesVersion")
                // for chrome support
                implementation(npm("puppeteer", "21.3.8"))
            }
        }
        // Our JVM tests (integration tests) reside in src/jvmTest.
        val jvmTest by getting {
            dependencies {
                implementation("io.kotest:kotest-runner-junit5:$kotestVersion")
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
tasks.named<Test>("jvmTest") {
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

tasks.register("installPuppeteerChromium") {
    dependsOn("kotlinNodeJsSetup", "kotlinNpmInstall")

    doLast {
        val puppeteerDir = file("build/js/node_modules/puppeteer")
        val installScript = File(puppeteerDir, "install.mjs")

        if (!installScript.exists()) {
            throw GradleException("Puppeteer install.mjs not found at: $installScript")
        }

        val nodeExecutable = File(System.getProperty("user.home"))
            .resolve(".gradle/nodejs")
            .listFiles()
            ?.filter { it.isDirectory && it.name.startsWith("node-v") }
            ?.flatMap { listOf(it, *it.listFiles().orEmpty()) }
            ?.mapNotNull { it.resolve("bin/node").takeIf { node -> node.exists() && node.canExecute() } }
            ?.firstOrNull()
            ?: throw GradleException("Managed Node.js not found in ~/.gradle/nodejs")

        val command = listOf(nodeExecutable.absolutePath, installScript.absolutePath)
        println("Running Puppeteer install with: ${command.joinToString(" ")}")

        val processBuilder = ProcessBuilder(command)
            .directory(puppeteerDir)
            .redirectErrorStream(true)
            .apply {
                environment()["CI"] = "true"
                environment()["PUPPETEER_NO_PROGRESS"] = "true"
                environment()["PUPPETEER_SKIP_POSTINSTALL"] = "true"
                environment()["PUPPETEER_PRODUCT"] = "chrome"
                environment()["PUPPETEER_CACHE_DIR"] = File(System.getProperty("user.home"))
                    .resolve(".cache/puppeteer").absolutePath
            }

        val process = processBuilder.start()
        val reader = process.inputStream.bufferedReader()

        var chromePath: String? = null
        while (true) {
            val line = reader.readLine() ?: break
            println(line)
            if (line.contains("Chrome") && line.contains("downloaded to")) {
                val basePath = line.substringAfter("downloaded to").trim()
                val chromeExecutable = when {
                    org.gradle.internal.os.OperatingSystem.current().isWindows -> "chrome-win64\\chrome.exe"
                    org.gradle.internal.os.OperatingSystem.current().isMacOsX -> "chrome-mac-arm64/Chromium.app/Contents/MacOS/Chromium"
                    else -> "chrome-linux64/chrome"
                }
                chromePath = File(basePath, chromeExecutable).absolutePath
                project.extensions.extraProperties["org.example.puppeteerChromePath"] = chromePath
                println("✅ Chrome downloaded to: $chromePath")
                process.destroy()
                break
            }
        }

        val timeUnit = Class.forName("java.util.concurrent.TimeUnit")
            .getField("MINUTES")
            .get(null) as java.util.concurrent.TimeUnit

        val finished = process.waitFor(1, timeUnit)

        if (!finished) {
            process.destroyForcibly()
            throw GradleException("Puppeteer install process hung and was forcibly terminated")
        }

        val exit = process.exitValue()
        if (exit != 0 && !(exit == 143 && chromePath != null)) {
            throw GradleException("Puppeteer install failed with exit code $exit")
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.targets.js.testing.KotlinJsTest>().configureEach {
    dependsOn("installPuppeteerChromium")

    doFirst {
        val chromePath = project.extensions.extraProperties["org.example.puppeteerChromePath"] as? String
            ?: throw GradleException("Chrome path was not discovered from Puppeteer install")

        println("✅ Using Puppeteer Chrome at: $chromePath")
        environment("CHROME_BIN", chromePath)
    }
}

// Ensure JVM tests depend on starting the React app.
tasks.named("jvmTest") {
    dependsOn("startReactApp")
}

tasks.named("check") {
    dependsOn("jsTest", "jvmTest")
}


// error about missing or outdated package-lock.json can't be solved at present
// Disabling kotlinStoreYarnLock could cause problems at some point
tasks.named("kotlinStoreYarnLock") {
    enabled = false
}

// Heroku Deployment (chapter 9)
tasks.register("stage") {
    dependsOn("build")
}