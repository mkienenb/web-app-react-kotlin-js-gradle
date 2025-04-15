import react.FC
import react.Props
import react.create
import react.dom.client.createRoot
import tanstack.query.core.QueryClient
import tanstack.react.query.QueryClientProvider
import web.dom.document


// Create a QueryClient instance that will be shared with your components.
val queryClient = QueryClient()

fun main() {
    val container = document.createElement("div")
    document.body.appendChild(container)

    createRoot(container).render(root.create())
}

val root = FC<Props> {
    QueryClientProvider {
        client = queryClient
        ValidationPage()
    }
}
