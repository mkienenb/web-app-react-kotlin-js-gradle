package com.example.validateiban

import com.example.stepdefinitions.common.BasePage
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory

// page_url = about:blank
class IbanPage(driver: WebDriver) : BasePage(driver) {
    @FindBy(css = "form [data-test='iban-entry']")
    private lateinit var ibanEntryField: WebElement

    init {
        PageFactory.initElements(driver, this)
    }

    fun enterIban(iban: String) {
        ibanEntryField.sendKeys(iban, Keys.ENTER)
    }
}