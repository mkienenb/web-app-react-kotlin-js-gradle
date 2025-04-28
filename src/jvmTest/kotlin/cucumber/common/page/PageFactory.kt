package cucumber.common.page

import org.openqa.selenium.WebDriver

object PageFactory {
    private val cache = mutableMapOf<Class<*>, BasePage>()

    @Suppress("UNCHECKED_CAST")
    fun <T : BasePage> getPage(driver: WebDriver, clazz: Class<T>): T {
        return cache.getOrPut(clazz) {
            val constructor = clazz.getConstructor(WebDriver::class.java)
            constructor.newInstance(driver)
        } as T
    }

    fun reset() = cache.clear()
}
