package reactdi

import io.kotest.core.spec.style.ShouldSpec
import kotest.testDI
import react.FC
import react.Props
import com.zegreatrob.wrapper.testinglibrary.react.TestingLibraryReact.act
import react.create
import react.dom.client.createRoot
import web.dom.document
import web.html.HTMLDivElement

open class ReactShouldSpecBase : ShouldSpec() {

    protected lateinit var container: HTMLDivElement

    init {
        beforeTest {
            container = document.createElement("div") as HTMLDivElement
            document.body.appendChild(container)
        }

        afterTest {
            container.remove()
        }
    }

    protected suspend fun <P : Props> renderReactComponent(componentUnderTest: FC<P>,
                                               propsBuilder: P.() -> Unit = {}) {
        val root = createRoot(container)
        act {
            root.render(KodeinProvider.create {
                di = testDI
                +componentUnderTest.create(propsBuilder)
            })
        }
    }

    protected fun runningInBrowser(): Boolean {
        return js("typeof window.fetch !== 'undefined'") as Boolean
    }

    protected suspend fun waitUntilElementExists(
        container: HTMLDivElement,
        selector: String,
        timeout: Duration = 5000.milliseconds
    ) {
        waitUntil(timeout) {
            container.querySelector(selector) != null
        }
    }
}
