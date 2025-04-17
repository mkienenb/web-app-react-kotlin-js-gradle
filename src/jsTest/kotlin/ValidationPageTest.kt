import io.kotest.matchers.nulls.shouldNotBeNull
import kotlinx.coroutines.test.runTest
import react.create
import react.dom.client.createRoot
import react.dom.test.act
import web.dom.document
import web.html.HTMLDivElement
import web.html.HTMLInputElement

class ValidationPageTest : BrowserOnlyShouldSpec() {

    private lateinit var container: HTMLDivElement

    init {
        if (isBrowser()) {

            beforeTest {
                container = document.createElement("div") as HTMLDivElement
                document.body.appendChild(container)
            }

            afterTest {
                container.remove()
            }

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
