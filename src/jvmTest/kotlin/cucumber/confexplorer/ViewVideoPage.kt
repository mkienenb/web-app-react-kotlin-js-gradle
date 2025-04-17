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
        get() = driver.findElements(By.cssSelector("li[data-code-element-handle='unwatchedVideo']")).map { it.text }

    init {
        PageFactory.initElements(driver, this)
    }
}
