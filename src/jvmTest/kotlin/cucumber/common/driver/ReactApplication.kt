package cucumber.common.driver

import kotlinx.coroutines.*
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit

class ReactApplication() {
    private val reactAppStartupTimeoutInSeconds: Long = 200

    private var reactProcess: Process? = null
    private var reactStarted = false
    var port: Int? = null

    private val reactAppProcessBuilder: ProcessBuilder
    init {
        // Launch the React app with the org.example.headless property.
        val projectDir = File(".")
        val gradleExecutable: String = if (System.getProperty("os.name").lowercase().contains("win"))
            "gradlew.bat" else "./gradlew"
        reactAppProcessBuilder =
            ProcessBuilder(gradleExecutable, "jsBrowserDevelopmentRun", "-Porg.example.headless=true")
                .directory(projectDir)
                .redirectErrorStream(true)
    }

    fun addEnvironmentVariable(envName: String, envValue: String) {
        check(!reactStarted) {
            "Cannot assign environment variables after React application is started"
        }
        reactAppProcessBuilder.environment()[envName] = envValue
    }

    fun start(environmentVariables: Map<String, String> = emptyMap()) {
        check(!reactStarted) {
            "React application has already been started"
        }
        reactAppProcessBuilder.environment().putAll(environmentVariables)

        println("Starting React app in headless mode on a random port...")
        val process = reactAppProcessBuilder.start()
        reactProcess = process
        reactStarted = true
        val pid = process.pid()
        println("React app started with PID: $pid")
        println("React app started successfully.")

        // Capture output to discover the randomly chosen port using a coroutine.
        val portRegex = Regex("http://localhost:(\\d+)")
        GlobalScope.launch(Dispatchers.IO) {
            process.inputStream.bufferedReader().forEachLine { line ->
                println(line) // Log the output.
                if (port == null) {
                    val match = portRegex.find(line)
                    if (match != null) {
                        val portStr = match.groupValues[1]
                        port = portStr.toIntOrNull()
                        if (port != null) {
                            println("Detected React app running on port: $port")
                        }
                    }
                }
            }
        }

        // Wait for the port to be discovered with a timeout.
        runBlocking {
            try {
                withTimeout(reactAppStartupTimeoutInSeconds * 1000) {
                    while (port == null) {
                        delay(100)
                    }
                }
            } catch (e: TimeoutCancellationException) {
                throw Exception("React port was not discovered within $reactAppStartupTimeoutInSeconds seconds!", e)
            }
        }

        // Now poll for the React app to be responsive using the discovered port with a timeout.
        println("Waiting for React app to be responsive on port $port...")
        val url = URL("http://localhost:$port")
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
                                    println("React app is responsive on port $port!")
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
                throw Exception("React app did not become responsive within $reactAppStartupTimeoutInSeconds seconds on port $port")
            }
        }
    }

    fun stop() {
        if (reactStarted && reactProcess != null && reactProcess!!.isAlive) {
            val pid = reactProcess!!.pid()
            println("Stopping React app with PID: $pid")

            val os = System.getProperty("os.name").lowercase()
            if (os.contains("win")) {
                println("Windows detected. Using taskkill to terminate the process tree.")
                try {
                    ProcessBuilder("cmd.exe", "/c", "taskkill /PID $pid /T /F")
                        .inheritIO() // optional: show output/errors
                        .start()
                        .waitFor()
                } catch (e: Exception) {
                    println("Failed to taskkill React app with PID $pid: ${e.message}")
                }
            } else {
                println("Non-Windows OS detected. Using destroy()/destroyForcibly().")
                reactProcess!!.destroy()
                if (!reactProcess!!.waitFor(10, TimeUnit.SECONDS)) {
                    println("Force killing React app with PID: $pid")
                    reactProcess!!.destroyForcibly()
                }
            }
        }
    }
}
