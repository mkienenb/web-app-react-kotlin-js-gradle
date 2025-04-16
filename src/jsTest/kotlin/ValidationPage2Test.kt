//import js.promise.Promise
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.MainScope
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.promise
//import kotlinx.coroutines.test.runTest
//import org.koin.core.context.GlobalContext
//import org.koin.core.context.startKoin
//import org.koin.core.context.stopKoin
//import react.create
//import react.dom.client.createRoot
//import react.dom.test.actAsync
//import web.dom.document
//import web.html.HTMLDivElement
//import web.html.HTMLInputElement
//import kotlin.coroutines.EmptyCoroutineContext
//import kotlin.test.*
//
//class ValidationPage2Test {
//
//    private var koinStarted = false
//
//    fun startKoinOnce() {
//        if (!koinStarted && GlobalContext.getOrNull() == null) {
//            startKoin {
//                modules(ibanValidationModule)
//            }
//            koinStarted = true
//        }
//    }
//
//    private lateinit var container: HTMLDivElement
//
//    @BeforeTest
//    fun setup() {
//        startKoinOnce()
////        container = document.createElement("div") as HTMLDivElement
////        document.body.appendChild(container)
//    }
//
//    @AfterTest
//    fun teardown() {
////        container.remove()
//    }
//
////    @Test
////    fun shouldRenderValidationForm2(): Promise<Unit> {
////        val result: Promise<Unit> = actAsync {
////            val root = createRoot(container)
////            root.render(ValidationPage.create())
////        }
////
////        return result.then {
////            val input = container.querySelector("[data-test='iban-entry']") as? HTMLInputElement
////            assertNotNull(input, "Expected IBAN input field to be present")
////        }
////    }
//
//    private fun returnFortyTwo() = 42
//    private suspend fun suspendFun2() = returnFortyTwo()
//
//    private val testScope = CoroutineScope(EmptyCoroutineContext)
//
//
//    private suspend fun suspendFun() = returnFortyTwo()
//
//
//    @Test
//    fun testSuspendFunWithPromise() = kotlinx.coroutines.GlobalScope.promise {
//        val result = suspendFun()
//        assertEquals(42, result)
//    }
//
//    @Test
//    fun testPureFunction() {
//        val result = returnFortyTwo()
//        assertEquals(42, result)
//    }
//
//
//    @Test
//    fun sanityCheckLaunch() {
//        val scope = testScope
//        scope.launch {
//            console.log("✅ Coroutine started without Dispatchers")
//        }
//    }
//
//    @Test
//    fun test(): kotlin.js.Promise<Unit> = testScope.promise {
//        val result = suspendFun2()
//        assertEquals(42, result)
//    }
//
////    @Test
////    fun testUsingPromise(): Promise<Unit> {
////        return suspendFun2().then { result ->
////            assertEquals(42, result)
////        }
////    }
//
//    @Test
//    fun testSimpleSuspend2() = runTest {
//        suspendFun2()
//    }
//}
//
