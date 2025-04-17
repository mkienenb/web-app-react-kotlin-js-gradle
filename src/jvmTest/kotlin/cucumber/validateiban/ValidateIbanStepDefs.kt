package cucumber.validateiban

import cucumber.common.ScenarioContext
import cucumber.common.driver.baseUrl
import cucumber.common.page.page
import io.cucumber.java.en.Given
import io.cucumber.java.en.When
import io.cucumber.java.en.Then

public class ValidateIbanStepDefs(private val scenarioContext : ScenarioContext) {

    @Given("I open a validation page")
    public fun iOpenAValidationPage() {
        with (scenarioContext) {
            driver.get(baseUrl())
        }
    }

    @When("I provide an IBAN")
    public fun iProvideAnIBAN() {
        val ibanPage = scenarioContext.page<IbanPage>()
        ibanPage.enterIban("AT0309000000000019176655")
    }

    @Then("I see validation details")
    public fun iSeeValidationDetails() {
        val ibanPage = scenarioContext.page<IbanPage>()
        // Assert that various pieces of text are visible on the page.
        ibanPage.assertThatTextIsVisible("Valid IBAN")
        ibanPage.assertThatTextIsVisible("Trusted bank")
        ibanPage.assertThatTextIsVisible("Accepts instant payments")
        ibanPage.assertThatTextIsVisible("Positive operation history")
        ibanPage.assertThatTextIsVisible("No security claims")
        ibanPage.assertThatTextIsVisible("Complies with Payment Services Directive (PSD2)")    }
}
