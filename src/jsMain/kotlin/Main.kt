import react.FC
import react.Props
import react.create
import react.dom.client.createRoot
import web.dom.document

fun main() {
    val container = document.createElement("div")
    document.body.appendChild(container)

    createRoot(container).render(root.create())
}

val root = FC<Props> {

    ValidationPage()
}
