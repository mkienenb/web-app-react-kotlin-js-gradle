package confexplorer.viewvideo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

object ScopeProvider {
    interface ScopeFactory {
        fun scope(): CoroutineScope
    }

    private object DefaultFactory : ScopeFactory {
        override fun scope(): CoroutineScope = MainScope()
    }

    var scopeFactory: ScopeFactory = DefaultFactory

    fun getScope(): CoroutineScope = scopeFactory.scope()
}
