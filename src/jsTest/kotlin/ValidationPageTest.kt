//import kotlin.test.*
//import kotlinx.coroutines.test.runTest
//import react.create
//import react.dom.client.createRoot
//import react.dom.test.act
//import web.dom.document
//import web.html.HTMLDivElement
//import web.html.HTMLInputElement
//
//
//class ValidationPageTest {
//
//    private lateinit var container: HTMLDivElement
//
//    @BeforeTest
//    fun setup() {
//        container = document.createElement("div") as HTMLDivElement
//        document.body.appendChild(container)
//    }
//
//    @AfterTest
//    fun teardown() {
//        container.remove()
//    }
//
//    @Test
//    fun shouldRenderValidationForm() = runTest {
//        act {
//            val root = createRoot(container)
//            root.render(ValidationPage.create())
//        }
//
//        val input = container.querySelector("[data-test='iban-entry']") as? HTMLInputElement
//        assertNotNull(input, "Expected IBAN input field to be present")
//    }
//}
