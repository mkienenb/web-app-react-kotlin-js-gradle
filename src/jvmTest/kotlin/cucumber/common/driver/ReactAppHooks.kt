package cucumber.common.driver

import cucumber.common.ScenarioContext
import io.cucumber.java.After
import io.cucumber.java.Before
import kotlinx.coroutines.*
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit

class ReactAppHooks(private val scenarioContext : ScenarioContext) {
    private val reactAppStartupTimeoutInSeconds: Long = 200

    private var reactProcess: Process? = null
    private var reactStarted = false
    private var discoveredPort: Int? = null

    @Before(order = Int.MIN_VALUE + 1000)
    fun setUp() {
        println("Starting React app in headless mode on a random port...")
        // Launch the React app with the org.example.headless property.
        val projectDir = File(".")
        val gradleExecutable: String = if (System.getProperty("os.name").lowercase().contains("win"))
            "gradlew.bat" else "./gradlew"
        val process = ProcessBuilder(gradleExecutable, "jsBrowserDevelopmentRun", "-Porg.example.headless=true")
            .directory(projectDir)
            .redirectErrorStream(true)
            .start()
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
                if (discoveredPort == null) {
                    val match = portRegex.find(line)
                    if (match != null) {
                        val portStr = match.groupValues[1]
                        discoveredPort = portStr.toIntOrNull()
                        if (discoveredPort != null) {
                            println("Detected React app running on port: $discoveredPort")
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
                throw Exception("React port was not discovered within $reactAppStartupTimeoutInSeconds seconds!", e)
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
                throw Exception("React app did not become responsive within $reactAppStartupTimeoutInSeconds seconds on port $discoveredPort")
            }
        }

        scenarioContext.applicationPort = discoveredPort
    }

    @After(order = Int.MAX_VALUE - 1000)
    fun tearDown() {
        if (reactStarted && reactProcess != null && reactProcess!!.isAlive) {
            println("Stopping React app with PID: ${reactProcess!!.pid()}")
            reactProcess!!.destroy()
            if (!reactProcess!!.waitFor(10, TimeUnit.SECONDS)) {
                println("Force killing React app with PID: ${reactProcess!!.pid()}")
                reactProcess!!.destroyForcibly()
            }
        }
    }
}
