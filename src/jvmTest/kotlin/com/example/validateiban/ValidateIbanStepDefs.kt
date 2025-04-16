package com.example.validateiban

import com.example.stepdefinitions.common.ScenarioContext
import com.example.stepdefinitions.common.ScenarioContextHolder
import io.cucumber.java.en.Given
import io.cucumber.java.en.When
import io.cucumber.java.en.Then

public class ValidateIbanStepDefs(private val scenarioContext : ScenarioContext) {
    // Get a reference to the shared ScenarioContext.
    private val context = ScenarioContextHolder.ValidateIbanScenarioContext

    @Given("I open a validation page")
    public fun iOpenAValidationPage() {
        context.driver.get("http://localhost:${scenarioContext.reactPort}") // Update the URL if necessary.
    }

    @When("I provide an IBAN")
    public fun iProvideAnIBAN() {
        context.ibanPage.enterIban("AT0309000000000019176655")
    }

    @Then("I see validation details")
    public fun iSeeValidationDetails() {
        // Assert that various pieces of text are visible on the page.
        context.ibanPage.assertThatTextIsVisible("Valid IBAN")
        context.ibanPage.assertThatTextIsVisible("Trusted bank")
        context.ibanPage.assertThatTextIsVisible("Accepts instant payments")
        context.ibanPage.assertThatTextIsVisible("Positive operation history")
        context.ibanPage.assertThatTextIsVisible("No security claims")
        context.ibanPage.assertThatTextIsVisible("Complies with Payment Services Directive (PSD2)")    }
}
