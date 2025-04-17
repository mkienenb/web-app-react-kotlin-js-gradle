import io.kotest.core.spec.style.ShouldSpec

open class BrowserOnlyShouldSpec : ShouldSpec({
    if (isBrowser()) {
        // subclasses will register tests inside `init {}` block
        // or you can use a DSL hook here if desired
    }
})

fun isBrowser(): Boolean = js("typeof window !== 'undefined'") as Boolean
