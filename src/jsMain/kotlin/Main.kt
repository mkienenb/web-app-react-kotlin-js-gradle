import react.FC
import react.Props
import react.create
import react.dom.client.createRoot
import react.dom.html.ReactHTML.li
import testsupport.dataCodeElementHandleAttribute
import web.dom.document

fun main() {
    val container = document.createElement("div")
    document.body.appendChild(container)

    createRoot(container).render(root.create())
}

val root = FC<Props> {
    li {
        dataCodeElementHandleAttribute = "unwatchedVideo"
        +"Learning kotlin"
    }
    li {
        dataCodeElementHandleAttribute = "unwatchedVideo"
        +"Learning koin"
    }
    li {
        dataCodeElementHandleAttribute = "unwatchedVideo"
        +"Learning kotest"
    }
    li {
        dataCodeElementHandleAttribute = "unwatchedVideo"
        +"Learning react"
    }
}
