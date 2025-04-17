package cucumber.validateiban

import cucumber.common.ScenarioContext
import cucumber.common.driver.baseUrl
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When

public class ValidateIbanStepDefs(private val scenarioContext : ScenarioContext) {

    @Given("I open a validation page")
    public fun iOpenAValidationPage() {
        with (scenarioContext) {
            driver.get(baseUrl())
        }
    }

    @When("I provide an IBAN")
    public fun iProvideAnIBAN() {
        scenarioContext.withIbanPage {
            enterIban("AT0309000000000019176655")
        }
    }

    @Then("I see validation details")
    public fun iSeeValidationDetails() {
        scenarioContext.withIbanPage {
            // Assert that various pieces of text are visible on the page.
            assertThatTextIsVisible("Valid IBAN")
            assertThatTextIsVisible("Trusted bank")
            assertThatTextIsVisible("Accepts instant payments")
            assertThatTextIsVisible("Positive operation history")
            assertThatTextIsVisible("No security claims")
            assertThatTextIsVisible("Complies with Payment Services Directive (PSD2)")
        }
    }
}
