package cucumber.confexplorer

import cucumber.common.page.BasePage
import cucumber.common.page.GenerateCucumberPageHelper
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toJavaDuration

@GenerateCucumberPageHelper
class ViewVideoPage(driver: WebDriver) : BasePage(driver) {

    // TODO: revisit if @FindBy works if element is found
    val unwatchedVideoNameList: List<String>
        get() = getUnwatchedVideoElements().map { it.text }

    val videoDetailTitle: String
        get() = getWebElementByCodeElementHandle("videoDetailTitle").text

    private fun getUnwatchedVideoElements(): MutableList<WebElement> =
        getWebElementsByCodeElementHandle("unwatchedVideo")

    init {
        PageFactory.initElements(driver, this)
    }

    fun selectVideo(videoName: String) {
        for (video in getUnwatchedVideoElements()) {
            if (video.text == videoName) {
                video.click()
                return
            }
        }
        throw RuntimeException("Video not found")
    }

    fun waitUntilDoneLoading(
        timeout: Duration = 5000.milliseconds
    ) {
        val wait = WebDriverWait(driver, timeout.toJavaDuration())
        wait.until(
            ExpectedConditions.invisibilityOfElementLocated
                (By.cssSelector("[data-code-element-handle='loading']"))
        )
    }
}
