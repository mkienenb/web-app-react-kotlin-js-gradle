package kotest

import BrowserOnlyShouldSpec
import browserOnlyCode
import web.dom.document
import web.html.HTMLDivElement

open class ReactComponentTestBase : BrowserOnlyShouldSpec() {

    protected lateinit var container: HTMLDivElement

    init {
        browserOnlyCode {
            beforeTest {
                container = document.createElement("div") as HTMLDivElement
                document.body.appendChild(container)
            }

            afterTest {
                container.remove()
            }
        }
    }
}
