package cucumber.common

import org.openqa.selenium.WebDriver

class ScenarioContext {
    lateinit var driver: WebDriver
    var applicationPort: Int? = null
}
