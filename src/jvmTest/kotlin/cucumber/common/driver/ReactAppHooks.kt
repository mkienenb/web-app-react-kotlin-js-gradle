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
    @After(order = Int.MAX_VALUE - 1000)
    fun tearDown() {
        scenarioContext._reactApplication.stop()
    }
}
