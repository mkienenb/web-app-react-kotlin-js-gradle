package cucumber.common

import cucumber.common.fakewebservice.FakeWebServiceFactory
import org.openqa.selenium.WebDriver

class ScenarioContext {
    @Suppress("PropertyName")
    internal val _fakeWebServiceFactory: FakeWebServiceFactory = FakeWebServiceFactory()
    lateinit var driver: WebDriver
    var applicationPort: Int? = null
}
