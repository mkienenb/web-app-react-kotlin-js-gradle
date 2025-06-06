package cucumber.common.page

import io.kotest.assertions.withClue
import io.kotest.matchers.booleans.shouldBeTrue
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.NoSuchElementException

import org.openqa.selenium.support.PageFactory

open class BasePage(protected val driver: WebDriver) {
    init {
        PageFactory.initElements(driver, this)
    }

    protected fun getWebElementByCodeElementHandle(codeElementHandle: String): WebElement =
        driver.findElement(By.cssSelector("[data-code-element-handle='${codeElementHandle}']"))
    protected fun getWebElementsByCodeElementHandle(codeElementHandle: String): MutableList<WebElement> =
        driver.findElements(By.cssSelector("[data-code-element-handle='${codeElementHandle}']"))

    /**
     * Finds the first element in the DOM containing the specified text.
     *
     * @param text The text to search for.
     * @return The matching WebElement.
     * @throws NoSuchElementException if no element containing the text is found.
     */
    private fun findElementByContainsText(text: String): WebElement {
        return driver.findElement(By.xpath("//*[contains(text(), '$text')]"))
    }

    /**
     * Asserts that an element containing the specified text is visible on the page.
     *
     * @param text The text expected to be visible.
     */
    fun assertThatTextIsVisible(text: String) {
        try {
            val element = findElementByContainsText(text)
            withClue("Expected text '$text' to be visible on the page, but it was not.") {
                element.isDisplayed.shouldBeTrue()
            }
        } catch (e: NoSuchElementException) {
            throw AssertionError("Failed to find any element containing text '$text'.", e)
        }
    }
}
