package cucumber.confexplorer

import confexplorer.ElementHandle.LOADING
import confexplorer.ElementHandle.MARK_AS_WATCHED_BUTTON
import confexplorer.ElementHandle.REACT_PLAYER
import confexplorer.ElementHandle.UNWATCHED_VIDEO_LIST
import confexplorer.ElementHandle.VIDEO_DETAIL_TITLE
import confexplorer.ElementHandle.VIDEO_TITLE
import confexplorer.ElementHandle.WATCHED_VIDEO_LIST
import confexplorer.getCodeElementHandle
import cucumber.common.driver.waitUntilSelectorElementIsInvisible
import cucumber.common.driver.waitUntilSelectorElementIsVisible
import cucumber.common.page.BasePage
import cucumber.common.page.GenerateCucumberPageHelper
import cucumber.common.screen.*
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.PageFactory
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds


@GenerateCucumberPageHelper
class ViewVideoPage(driver: WebDriver) : BasePage(driver) {

    private fun getUnwatchedVideoElements(): MutableList<WebElement> =
        driver.getAllByLabelText(UNWATCHED_VIDEO_LIST).flatMap { it.getAllByLabelText(VIDEO_TITLE) }.toMutableList()

    private fun getWatchedVideoElements(): MutableList<WebElement> =
        driver.getAllByLabelText(WATCHED_VIDEO_LIST).flatMap { it.getAllByLabelText(VIDEO_TITLE) }.toMutableList()

    val unwatchedVideoNameList: List<String>
        get() = getUnwatchedVideoElements().map { it.text }

    val watchedVideoNameList: List<String>
        get() = getWatchedVideoElements().map { it.text }

    val videoDetailTitle: String?
        get() = driver.getByLabelText(VIDEO_DETAIL_TITLE).text

    val videoPlayerUrl: String?
        get() = videoPlayerIFrameElement().getDomAttribute("src")

    private fun videoPlayerIFrameElement(): WebElement =
        driver.getByLabelText(REACT_PLAYER).findElement(By.cssSelector("iframe"))

    val selectedVideoTitle: String?
        get() {
            return driver.queryByRole("option", RoleOptions(selected = true))?.findElement(By.cssSelector("[aria-label='$VIDEO_TITLE']"))?.text
        }

    fun markSelectedVideoAsWatched() {
        driver.findByLabelText(MARK_AS_WATCHED_BUTTON).click()
    }

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
