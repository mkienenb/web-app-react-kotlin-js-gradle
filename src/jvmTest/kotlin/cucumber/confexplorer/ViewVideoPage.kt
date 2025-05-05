package cucumber.confexplorer

import confexplorer.ElementLocator.UNWATCHED_VIDEO_TITLE_FOR_SELECTED_VIDEO_XPATH_EXPRESSION
import cucumber.common.page.BasePage
import cucumber.common.page.GenerateCucumberPageHelper
import io.kotest.assertions.withClue
import io.kotest.matchers.ints.shouldBeLessThanOrEqual
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
        get() = getWebElementByCodeElementHandle("video-detail-title").text

    val videoPlayerUrl: String?
        get() = getWebElementByCodeElementHandle("react-player").findElement(By.tagName("iframe")).getDomAttribute("src")

    val selectedVideoTitle: String?
        get() {
            val elements = driver.findElements(By.xpath(UNWATCHED_VIDEO_TITLE_FOR_SELECTED_VIDEO_XPATH_EXPRESSION))
            withClue("number of selected videos") {
                elements.size shouldBeLessThanOrEqual 1
            }
            return elements.firstOrNull()?.text
        }

    private fun getUnwatchedVideoElements(): MutableList<WebElement> =
        getWebElementsByCodeElementHandle("unwatched-video-title")

    init {
        PageFactory.initElements(driver, this)
    }

    fun selectVideo(videoName: String) {
        val unwatchedVideoElements = getUnwatchedVideoElements()
        check(unwatchedVideoElements.isNotEmpty()) { "No unwatched videos" }
        unwatchedVideoElements.firstOrNull { it.text == videoName}?.click() ?: throw RuntimeException("Video not found")
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
