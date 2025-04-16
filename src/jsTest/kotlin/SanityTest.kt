import kotlinx.coroutines.*
import kotlin.coroutines.*
import kotlin.test.*
import kotlinx.coroutines.test.*



class SanityTest {
    @Test
    fun sanityCheckMainScopeLaunch() {
        val scope = MainScope()
        scope.launch {
            console.log("✅ Coroutine started without Dispatchers")
        }
    }

    @Test
    fun testSimpleSuspendFunInRunTest() = runTest {
        suspendFun()
    }

    private suspend fun suspendFun(): Int {
        return 42
    }

    @Test
    fun testSuspendFunInGlobalScopePromise(): dynamic = GlobalScope.promise {
        val result = suspendFun()
        assertEquals(42, result)
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
}

class SanityTestDebug {

    @Test
    fun logDispatcherKind() {
        console.log("Dispatchers.Default = ", Dispatchers.Default.toString())
    }


    @Test
    fun logCoroutineContext() {
        val context = Dispatchers.Default + CoroutineName("test")
        context.fold(Unit) { _, element ->
            console.log("Context element: ${element.key} = $element")
        }
    }

    @Test
    fun printVersions() {
        console.log("Kotlin: ${KotlinVersion.CURRENT}")
    }
}
