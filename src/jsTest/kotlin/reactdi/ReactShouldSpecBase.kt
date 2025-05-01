package reactdi

import io.kotest.core.spec.style.ShouldSpec
import kotest.testDI
import kotlinx.coroutines.delay
import react.FC
import react.Props
import react.act
import react.create
import react.dom.client.createRoot
import web.dom.document
import web.html.HTMLDivElement
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

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

    protected suspend fun renderReactComponent(componentUnderTest: FC<Props>) {
        val root = createRoot(container)
        act {
            root.render(KodeinProvider.create {
                di = testDI
                +componentUnderTest.create {}
            })
        }
    }

    private suspend fun waitUntil(
        timeout: Duration = 5000.milliseconds,
        interval: Duration = 50.milliseconds,
        condition: () -> Boolean
    ) {
        val startTime = js("Date.now()").unsafeCast<Double>()
        while (!condition()) {
            delay(interval)
            val now = js("Date.now()").unsafeCast<Double>()
            if (now - startTime > timeout.inWholeMilliseconds) {
                throw AssertionError("Timed out after $timeout waiting for condition to be true")
            }
        }
    }

    protected suspend fun waitUntilElementGone(
        container: HTMLDivElement,
        selector: String,
        timeout: Duration = 5000.milliseconds
    ) {
        waitUntil(timeout) {
            container.querySelector(selector) == null
        }
    }
}
