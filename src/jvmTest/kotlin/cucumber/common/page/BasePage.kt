package cucumber.common.page

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.NoSuchElementException

import org.assertj.core.api.Assertions.assertThat
import org.openqa.selenium.support.PageFactory

open class BasePage(protected val driver: WebDriver) {
    init {
        PageFactory.initElements(driver, this)
    }

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
            assertThat(element.isDisplayed)
                .`as`("Expected text '$text' to be visible on the page, but it was not.")
                .isTrue()
        } catch (e: NoSuchElementException) {
            throw AssertionError("Failed to find any element containing text '$text'.", e)
        }
    }
}
