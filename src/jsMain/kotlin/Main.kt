import org.kodein.di.DI
import confexplorer.App
import react.StrictMode
import react.create
import react.dom.client.createRoot
import web.dom.document
import reactdi.KodeinProvider

fun main() {

    val container = document.getElementById("root") ?: error("Root element not found")
    val root = createRoot(container)

    root.render(
        StrictMode.create {
            KodeinProvider {
                di = DI {
                    import(productionModule)
                }
                App {}
            }
        }
    )
}
