package cucumber.common.driver

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toJavaDuration

fun WebDriver.waitUntilSelectorElementIsVisible(selector: String,
                                                timeout: Duration = 5000.milliseconds) {
    val wait = WebDriverWait(this, timeout.toJavaDuration())
    wait.until(
        ExpectedConditions.visibilityOfElementLocated
            (By.cssSelector(selector))
    )
}
fun WebDriver.waitUntilSelectorElementIsInvisible(selector: String,
                                                  timeout: Duration = 5000.milliseconds) {
    val wait = WebDriverWait(this, timeout.toJavaDuration())
    wait.until(
        ExpectedConditions.invisibilityOfElementLocated
            (By.cssSelector(selector))
    )
}