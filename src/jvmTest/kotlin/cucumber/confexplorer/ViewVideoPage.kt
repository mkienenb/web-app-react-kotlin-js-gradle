package cucumber.confexplorer

import confexplorer.ElementHandle.LOADING
import confexplorer.ElementHandle.REACT_PLAYER
import confexplorer.ElementHandle.UNWATCHED_VIDEO_TITLE
import confexplorer.ElementHandle.VIDEO_DETAIL_TITLE
import confexplorer.ElementLocator.UNWATCHED_VIDEO_TITLE_FOR_SELECTED_VIDEO_CSS_SELECTOR
import confexplorer.getCodeElementHandle
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
        get() = getWebElementByCodeElementHandle(VIDEO_DETAIL_TITLE).text

    val videoPlayerUrl: String?
        get() = videoPlayerIFrameElement().getDomAttribute("src")

    private fun videoPlayerIFrameElement(): WebElement =
        getWebElementByCodeElementHandle(REACT_PLAYER).findElement(By.cssSelector("iframe"))

    val selectedVideoTitle: String?
        get() {
            val elements = driver.findElements(By.cssSelector(UNWATCHED_VIDEO_TITLE_FOR_SELECTED_VIDEO_CSS_SELECTOR))
            withClue("number of selected videos") {
                elements.size shouldBeLessThanOrEqual 1
            }
            return elements.firstOrNull()?.text
        }

    private fun getUnwatchedVideoElements(): MutableList<WebElement> =
        driver.findElements(By.cssSelector("li[role^=option]"))

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
        driver.waitUntilSelectorElementIsInvisible(getCodeElementHandle(LOADING), timeout)
    }

    fun waitForVideoPlayerToBeLoaded(
        timeout: Duration = 5000.milliseconds
    ) {
        driver.waitUntilSelectorElementIsVisible("iframe", timeout)
    }
}
