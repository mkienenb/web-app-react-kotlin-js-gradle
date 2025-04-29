package cucumber.confexplorer

import confexplorer.UISymbol.VIDEO_SELECTOR_SYMBOL
import cucumber.common.page.BasePage
import cucumber.common.page.GenerateCucumberPageHelper
import org.openqa.selenium.By
import org.openqa.selenium.NoSuchElementException
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

    val videoPlayerUrl: String
        get() = getWebElementByCodeElementHandle("react-player-url").text

    val selectedVideoTitle: String
        get() {
            val videoElements = getUnwatchedVideoElements() + getWatchedVideoElements()
            val selectedVideoElement = videoElements.firstOrNull { it.text.startsWith(VIDEO_SELECTOR_SYMBOL) }
            if (selectedVideoElement == null) {
                throw NoSuchElementException("Selected video not found.")
            }
            return selectedVideoElement.findElement(By.cssSelector("[data-code-element-handle='videoTitle']")).text
        }

    private fun getUnwatchedVideoElements(): MutableList<WebElement> =
        getWebElementsByCodeElementHandle("unwatchedVideo")

    private fun getWatchedVideoElements(): MutableList<WebElement> =
        getWebElementsByCodeElementHandle("watchedVideo")

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
