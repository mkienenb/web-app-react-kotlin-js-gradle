package confexplorer

import browserOnlyCode
import io.kotest.assertions.withClue
import io.kotest.matchers.shouldBe
import kotest.ReactComponentTestBase

class AppComponentTest: ReactComponentTestBase() {
    init {
        browserOnlyCode {
            should("show 'Conference Explorer' on page") {
                ForComponent(AppComponent) {
                    val pageHeader = container.querySelector("h1")?.textContent
                    withClue("page header") {
                        pageHeader shouldBe "Conference Explorer"
                    }
                }
            }
        }
    }
}