package cucumber.common

import cucumber.common.driver.ReactApplication
import cucumber.common.fakewebservice.FakeWebServiceFactory
import org.openqa.selenium.WebDriver

class ScenarioContext {
    @Suppress("PropertyName")
    internal val _reactApplication = ReactApplication()
    @Suppress("PropertyName")
    internal val _fakeWebServiceFactory: FakeWebServiceFactory = FakeWebServiceFactory()

    lateinit var driver: WebDriver
}
