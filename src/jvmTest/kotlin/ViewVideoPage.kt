package com.example.validateiban

import cucumber.common.page.BasePage
import cucumber.common.page.GenerateCucumberPageHelper
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory

@GenerateCucumberPageHelper
class ViewVideoPage(driver: WebDriver) : BasePage(driver) {
    @FindBy(css = "")
    private lateinit var unwatchedVideoNameList: List<String>

    init {
        PageFactory.initElements(driver, this)
    }
}
