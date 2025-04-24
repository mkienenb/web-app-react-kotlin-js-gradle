
package kotest

import BrowserOnlyShouldSpec
import browserOnlyCode
import isBrowser
import kotlinx.coroutines.delay
import react.FC
import react.Props
import react.create
import react.dom.client.createRoot
import react.act
import web.dom.document
import web.html.HTMLDivElement
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

open class ReactComponentTestBase : BrowserOnlyShouldSpec() {

    protected lateinit var container: HTMLDivElement

    init {
        browserOnlyCode {
            beforeTest {
                setUpReactContainer()
            }
            afterTest {
                container.remove()
            }
        }
    }

    private fun setUpReactContainer() {
        container = document.createElement("div") as HTMLDivElement
        document.body.appendChild(container)
    }

    // Extracted helper for performing act and rendering the component
    private suspend fun <P : Props> actRenderComponent(component: FC<P>, propsBuilder: P.() -> Unit) {
        act {
            val root = createRoot(container)
            root.render(component.create(propsBuilder))
        }
    }

    // Use when your test block itself is suspendable (you need delay, await etc.)
    protected suspend fun <P : Props> ForComponentCallingCoroutines(
        component: FC<P>,
        propsBuilder: P.() -> Unit = {},
        test: suspend () -> Unit  // suspend test block
    ) {
        actRenderComponent(component, propsBuilder)  // now we use our extracted act-render helper
        test()  // then run the suspendable test code
    }

    // Use for test blocks that are not suspend (fast DOM-only tests)
    protected suspend fun <P : Props> ForComponent(
        component: FC<P>,
        propsBuilder: P.() -> Unit = {},
        test: () -> Unit  // non-suspend test block
    ) {
        actRenderComponent(component, propsBuilder)
        test()
    }

    protected suspend fun waitUntil(
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
        selector: String,
        timeout: Duration = 5000.milliseconds
    ) {
        waitUntil(timeout) {
            container.querySelector(selector) == null
        }
    }
}
