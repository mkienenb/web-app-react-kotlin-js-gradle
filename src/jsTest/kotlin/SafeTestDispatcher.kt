import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable
import kotlin.coroutines.CoroutineContext
import kotlin.js.Promise

object SafeTestDispatcher : CoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        // Schedule block using JS microtask queue
        Promise<Unit> { resolve, _ ->
            resolve(Unit)
        }.then {
            block.run()
        }
    }
}
