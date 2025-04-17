package cucumber.validateiban

import cucumber.common.page.BasePage
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy

class IbanPage(driver: WebDriver) : BasePage(driver) {
    @FindBy(css = "form [data-test='iban-entry']")
    private lateinit var ibanEntryField: WebElement

    fun enterIban(iban: String) {
        ibanEntryField.sendKeys(iban, Keys.ENTER)
    }
}