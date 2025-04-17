package cucumber.common.page

import cucumber.common.ScenarioContext

fun <T : BasePage> ScenarioContext.page(clazz: Class<T>): T {
    return PageFactory.getPage(driver, clazz)
}

inline fun <reified T : BasePage> ScenarioContext.page(): T {
    return this.page(T::class.java)
}
