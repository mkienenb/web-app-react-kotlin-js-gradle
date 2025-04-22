import react.StrictMode
import react.create
import react.dom.client.createRoot
import web.dom.document

fun main() {

    val container = document.getElementById("root") ?: error("Root element not found")
    val root = createRoot(container)

    root.render(
        StrictMode.create {
            App {} // your root component goes here
        }
    )
}
