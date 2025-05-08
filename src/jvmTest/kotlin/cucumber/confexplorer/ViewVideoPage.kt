package cucumber.confexplorer

import confexplorer.ElementLocator.UNWATCHED_VIDEO_TITLE_FOR_SELECTED_VIDEO_XPATH_EXPRESSION
import cucumber.common.driver.waitUntilSelectorElementIsInvisible
import cucumber.common.driver.waitUntilSelectorElementIsVisible
import cucumber.common.page.BasePage
import cucumber.common.page.GenerateCucumberPageHelper
import io.kotest.assertions.withClue
import io.kotest.matchers.ints.shouldBeLessThanOrEqual
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.PageFactory
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds


@GenerateCucumberPageHelper
class ViewVideoPage(driver: WebDriver) : BasePage(driver) {

    // TODO: revisit if @FindBy works if element is found
    val unwatchedVideoNameList: List<String>
        get() = getUnwatchedVideoElements().map { it.text }

    val videoDetailTitle: String
        get() = getWebElementByCodeElementHandle("video-detail-title").text

    val videoPlayerUrl: String?
        get() = videoPlayerIFrameElement().getDomAttribute("src")

    private fun videoPlayerIFrameElement(): WebElement =
        getWebElementByCodeElementHandle("react-player").findElement(By.cssSelector("iframe"))

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
        driver.waitUntilSelectorElementIsInvisible(getCodeElementHandle("loading"), timeout)
    }

    fun waitForVideoPlayerToBeLoaded(
        timeout: Duration = 5000.milliseconds
    ) {
        driver.waitUntilSelectorElementIsVisible("iframe", timeout)
    }
}
