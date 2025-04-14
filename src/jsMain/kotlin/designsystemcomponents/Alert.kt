package designsystemcomponents

import react.FC
import react.PropsWithChildren
import web.cssom.ClassName
import react.ReactNode
import react.dom.html.ReactHTML.div
import react.dom.aria.AriaRole

val classNameByVariant: Map<String, ClassName> = mapOf(
    "error" to ClassName("rounded bg-red-100 py-2 px-4 mt-2"),
    "info" to ClassName("rounded border-s-4 border-blue-300/40 bg-sky-50 p-4")
)

external interface AlertProps : PropsWithChildren {
    var variant : String?;
}

val Alert = FC<AlertProps> { props ->
    div {
        role = AriaRole.alert
        classNameByVariant[props.variant]?.let {
            className = it
        }

        +props.children
    }
}