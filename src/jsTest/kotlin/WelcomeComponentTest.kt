import react.create
import react.dom.client.createRoot
import react.dom.test.act
import web.dom.document
import web.html.HTMLDivElement
import kotlin.test.*
import kotlinx.coroutines.test.runTest

class WelcomeComponentTest {

    private lateinit var container: HTMLDivElement

    @BeforeTest
    fun setup() {
        container = document.createElement("div") as HTMLDivElement
        document.body.appendChild(container)
    }

    @AfterTest
    fun teardown() {
        container.remove()
    }


    @Test
    fun shouldRenderWelcomeMessage() = runTest {
        act {
            val root = createRoot(container)
            root.render(
                WelcomeComponent.create {
                    userName = "TestUser"
                }
            )
        }

        assertTrue(
            container.innerHTML.contains("Welcome, TestUser!"),
            "Expected welcome message to be rendered"
        )
    }
}
