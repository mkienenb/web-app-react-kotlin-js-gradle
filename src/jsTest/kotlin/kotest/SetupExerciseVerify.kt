package kotest

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import org.kodein.di.DI
import reactdi.DITestBuilder

class SuspendTestContext<T : Any>(
    private val context: T
) {
    private lateinit var exerciseBlock: suspend T.() -> Any?
    private var diBuilderBlock: (suspend T.(DITestBuilder) -> Unit)? = null
    private var di: DI? = null

    init {
        currentTestContext = this
    }

    fun withDI(builder: suspend T.(DITestBuilder) -> Unit): SuspendTestContext<T> {
        this.diBuilderBlock = builder
        return this
    }

    val testDI: DI
        get() = di ?: error("testDI accessed before it was built")

    private suspend fun buildTestDi(): DI {
        val builder = DITestBuilder()
        builder.scope = CoroutineScope(currentCoroutineContext())
        diBuilderBlock?.let { context.it(builder) }
        return builder.build()
    }

    fun exercise(block: suspend T.() -> Any?): SuspendTestContext<T> {
        this.exerciseBlock = block
        return this
    }

    fun <R> verify(block: suspend T.(R) -> Unit): suspend () -> Unit {
        @Suppress("UNCHECKED_CAST")
        return {
            di = buildTestDi()
            with(context) {
                val result = exerciseBlock() as R
                block(result)
            }
        }
    }
}

fun <T : Any> suspendSetup(context: T): SuspendTestContext<T> = SuspendTestContext(context)

internal var currentTestContext: SuspendTestContext<*>? = null

val testDI: DI
    get() = currentTestContext?.testDI
        ?: error("testDI accessed outside of test context")
