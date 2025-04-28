package cucumber.confexplorer

import cucumber.common.page.BasePage
import cucumber.common.page.GenerateCucumberPageHelper
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory

@GenerateCucumberPageHelper
class ViewVideoPage(driver: WebDriver) : BasePage(driver) {

    // TODO: revisit if @FindBy works if element is found
    val unwatchedVideoNameList: List<String>
        get() = getUnwatchedVideoElements().map { it.text }

    private fun getUnwatchedVideoElements(): MutableList<WebElement> =
        driver.findElements(By.cssSelector("[data-code-element-handle='unwatchedVideo']"))

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
}
