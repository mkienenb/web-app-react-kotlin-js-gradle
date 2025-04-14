package designsystemcomponents

import react.FC
import react.Fragment
import react.PropsWithChildren
import react.ReactNode
import react.dom.html.ReactHTML.div
import web.cssom.ClassName

external interface FormFieldProps : PropsWithChildren {
    var button: ReactNode?
    var error : ReactNode?
}

val FormField = FC<FormFieldProps> { props ->
    Fragment {
        // Container div with the Tailwind CSS classes
        div {
            className = ClassName("flex flex-row p-1.5 overflow-hidden border rounded-lg focus-within:ring focus-within:ring-opacity-40 focus-within:border-blue-400 focus-within:ring-blue-300")

            // Insert the children if available.
            props.children?.let { +it }

            // Insert the button if provided.
            props.button?.let { +it }
        }

        // If error exists, render the Alert component with variant="error"
        props.error?.let {
            Alert {
                variant = "error"
                // Pass error as children to Alert.
                props.error?.let { +it }
            }
        }
    }
}