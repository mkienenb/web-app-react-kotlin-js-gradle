
package kotest

import BrowserOnlyShouldSpec
import browserOnlyCode
import isBrowser
import react.FC
import react.Props
import react.create
import react.dom.client.createRoot
import react.dom.test.act
import web.dom.document
import web.html.HTMLDivElement

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
}
