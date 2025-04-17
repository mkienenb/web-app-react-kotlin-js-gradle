package cucumber.common.driver

import cucumber.common.ScenarioContext
import io.cucumber.java.Before
import io.cucumber.java.After
import org.openqa.selenium.chrome.ChromeDriver

class ChromeDriverHooks(private val scenarioContext : ScenarioContext) {

    @Before(order = -1000)
    fun setUp() {
        val options = org.openqa.selenium.chrome.ChromeOptions().apply {
            addArguments("--headless=new") // or "--headless" for older versions
            addArguments("--disable-gpu")
            addArguments("--window-size=1920,1080")
        }

        scenarioContext.driver = ChromeDriver(options)
    }

    @After(order = 1000)
    fun tearDown() {
        scenarioContext.driver?.quit()
    }
}
