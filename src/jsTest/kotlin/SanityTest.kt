import kotlinx.coroutines.*
import kotlin.coroutines.*
import kotlinx.coroutines.test.*
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe


class SanityTest : ShouldSpec({

    should("sanityCheckMainScopeLaunch") {
        val scope = MainScope()
        scope.launch {
            console.log("✅ Coroutine started without Dispatchers")
        }
    }

    suspend fun suspendFun(): Int {
        return 42
    }

    should("testSimpleSuspendFunInRunTest") {
        runTest {
            suspendFun()
        }
    }

    should("testSuspendFunInGlobalScopePromise") {
        val result = GlobalScope.promise {
            val result = suspendFun()
            result
        }
        result.await() shouldBe 42
    }

//    private val testScope = CoroutineScope(EmptyCoroutineContext)
//    private suspend fun returnFortyTwo(): Int = 42
//    @Test
//    fun testEmptyCoroutineContext(): dynamic = testScope.promise {
//        val result = returnFortyTwo()
//        assertEquals(42, result)
//    }

//    private val testScope = MainScope()
//    private suspend fun returnFortyTwo(): Int = 42
//    @Test
//    fun testEmptyCoroutineContext(): dynamic = testScope.promise {
//        val result = returnFortyTwo()
//        assertEquals(42, result)
//    }

//    @Test
//    fun testEmptyCoroutineContext(): dynamic {
//        val dispatcher = js("({ dispatch: function(_, f) { setTimeout(f, 0); } })")
//        val context = object : CoroutineDispatcher() {
//            override fun dispatch(context: CoroutineContext, block: Runnable) {
//                js("setTimeout")(block, 0)
//            }
//        }
//        return CoroutineScope(context).promise {
//            val result = returnFortyTwo()
//            assertEquals(42, result)
//        }
//    }
})

class SanityTestDebug : ShouldSpec({

    should("logDispatcherKind") {
        console.log("Dispatchers.Default = ", Dispatchers.Default.toString())
    }


    should("logCoroutineContext") {
        val context = Dispatchers.Default + CoroutineName("test")
        context.fold(Unit) { _, element ->
            console.log("Context element: ${element.key} = $element")
        }
    }

    should("printVersions") {
        console.log("Kotlin: ${KotlinVersion.CURRENT}")
    }
})