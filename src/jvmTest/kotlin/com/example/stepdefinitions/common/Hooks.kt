package com.example.stepdefinitions.common

import com.example.validateiban.IbanPage
import io.cucumber.java.Before
import io.cucumber.java.After
import org.openqa.selenium.chrome.ChromeDriver

class Hooks {

    @Before
    fun setUp() {
        val options = org.openqa.selenium.chrome.ChromeOptions().apply {
            addArguments("--headless=new") // or "--headless" for older versions
            addArguments("--disable-gpu")
            addArguments("--window-size=1920,1080")
        }

        val driver = ChromeDriver(options)
        val context = ScenarioContextHolder.ValidateIbanScenarioContext
        context.driver = driver
        context.ibanPage = IbanPage(driver)
    }

    @After
    fun tearDown() {
        // Tear down the WebDriver after each scenario.
        ScenarioContextHolder.ValidateIbanScenarioContext.driver.quit()
    }
}
