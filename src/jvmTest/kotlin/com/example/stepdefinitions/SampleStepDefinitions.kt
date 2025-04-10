package com.example.stepdefinitions

import io.cucumber.java.After
import io.cucumber.java.Before
import io.cucumber.java.en.Given
import io.cucumber.java.en.When
import io.cucumber.java.en.Then
import org.assertj.core.api.Assertions.assertThat
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver

class SampleStepDefinitions {

    private lateinit var driver: WebDriver

    private val reactPort = System.getProperty("org.example.reactPort")

    @Before
    fun setUp() {
        val options = org.openqa.selenium.chrome.ChromeOptions().apply {
            addArguments("--headless=new") // or "--headless" for older versions
            addArguments("--disable-gpu")
            addArguments("--window-size=1920,1080")
        }

        driver = ChromeDriver(options)
    }

    @After fun tearDown() {
        driver.quit()
    }

    @Given("the React app is running")
    fun theReactAppIsRunning() {
    }

    @When("I visit the homepage")
    fun iVisitTheHomepage() {
        driver.get("http://localhost:${reactPort}") // Update the URL if necessary.
    }

    @Then("I should see the title {string}")
    fun iShouldSeeTheTitle(expectedTitle: String) {
        val actualTitle = driver.title
        assertThat(actualTitle).isEqualTo(expectedTitle)
        driver.quit()
    }
}
