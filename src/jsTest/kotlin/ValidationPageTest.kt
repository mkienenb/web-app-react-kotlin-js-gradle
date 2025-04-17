import io.kotest.matchers.nulls.shouldNotBeNull
import kotest.ReactComponentTestBase
import kotlinx.coroutines.test.runTest
import react.create
import react.dom.client.createRoot
import react.dom.test.act
import web.html.HTMLInputElement

class ValidationPageTest : ReactComponentTestBase() {

    init {
        browserOnlyCode {
            should("shouldRenderValidationForm") {
                runTest {
                    act {
                        val root = createRoot(container)
                        root.render(ValidationPage.create())
                    }

                    val input = container.querySelector("[data-test='iban-entry']") as? HTMLInputElement
                    input.shouldNotBeNull()
                }
            }
        }
    }
}
