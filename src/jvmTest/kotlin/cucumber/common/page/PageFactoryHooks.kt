package cucumber.common.page

import cucumber.common.ScenarioContext
import io.cucumber.java.Before
import io.cucumber.java.After
import org.openqa.selenium.chrome.ChromeDriver

class PageFactoryHooks(private val scenarioContext : ScenarioContext) {

    @Before(order = -500)
    fun setUp() {
        PageFactory.reset()
    }
}
