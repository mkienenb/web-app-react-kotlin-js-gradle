import kotlinx.coroutines.test.runTest
import react.create
import react.dom.client.createRoot
import react.dom.test.act
import web.dom.document
import web.html.HTMLDivElement
import web.html.HTMLInputElement
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.nulls.shouldNotBeNull

class ValidationPageTest : ShouldSpec() {

    private lateinit var container: HTMLDivElement

    init {
        beforeTest {
            if (js("typeof window !== 'undefined'") as Boolean) {
                container = document.createElement("div") as HTMLDivElement
                document.body.appendChild(container)
            }
        }

        afterTest {
            if (js("typeof window !== 'undefined'") as Boolean) {
                container.remove()
            }
        }

        should("shouldRenderValidationForm").config(
            enabled = js("typeof window !== 'undefined'") as Boolean
        ) {
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
