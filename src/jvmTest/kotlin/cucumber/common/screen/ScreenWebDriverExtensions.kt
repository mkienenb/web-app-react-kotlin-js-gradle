package cucumber.common.screen

import cucumber.common.driver.waitUntilSelectorElementIsVisible
import io.kotest.assertions.withClue
import io.kotest.matchers.ints.shouldBeLessThanOrEqual
import io.kotest.matchers.shouldBe
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.By

fun WebDriver.getByRole(role: String, options: RoleOptions = RoleOptions()): WebElement {
    val selectors = mutableListOf("[role='$role']")
    options.name?.let { selectors.add("[aria-label='$it']") }
    options.selected?.let { selectors.add("[aria-selected='$it']") }

    val selector = selectors.joinToString("")
    val allByRole = this.findElements(By.cssSelector(selector))
    withClue("getByRole($role) does not return a single item") {
        allByRole.size shouldBe 1
    }
    return allByRole.first()
}

fun WebDriver.queryByRole(role: String, options: RoleOptions = RoleOptions()): WebElement? {
    val selectors = mutableListOf("[role='$role']")
    options.name?.let { selectors.add("[aria-label='$it']") }
    options.selected?.let { selectors.add("[aria-selected='$it']") }

    val selector = selectors.joinToString("")
    val allByRole = this.findElements(By.cssSelector(selector))
    withClue("getByRole($role) does not return a single item") {
        allByRole.size shouldBeLessThanOrEqual 1
    }
    return allByRole.firstOrNull()
}

fun WebDriver.getByLabelText(labelText: String): WebElement {
    return findElement(By.cssSelector("[aria-label='$labelText']"))
}

fun WebDriver.getAllByLabelText(labelText: String): List<WebElement> {
    return findElements(By.cssSelector("[aria-label='$labelText']"))
}

fun WebDriver.findByLabelText(labelText: String): WebElement {
    val selector = "[aria-label='$labelText']"
    waitUntilSelectorElementIsVisible(selector)
    return findElement(By.cssSelector(selector))
}

// TODO: Might want a different function findAllByRole, and have this one have 0 wait time
fun WebDriver.getAllByRole(role: String, options: RoleOptions = RoleOptions()): List<WebElement> {
    val selectors = mutableListOf("[role='$role']")
    options.name?.let { selectors.add("[aria-label='$it']") }
    options.selected?.let { selectors.add("[aria-selected='$it']") }

    val selector = selectors.joinToString("")
    return this.findElements(By.cssSelector(selector))
}