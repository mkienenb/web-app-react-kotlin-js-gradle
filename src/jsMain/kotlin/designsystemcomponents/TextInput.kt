import designsystemcomponents.dataTestAttribute
import react.FC
import react.dom.html.InputHTMLAttributes
import react.dom.html.ReactHTML.input
import web.cssom.ClassName
import web.html.HTMLInputElement

external interface TextInputProps : InputHTMLAttributes<HTMLInputElement> {
    var dataTestAttribute : String
}

val TextInput = FC<TextInputProps> { props ->
    input {
        dataTestAttribute = props.dataTestAttribute
        className = ClassName("grow px-3 py-2 text-gray-700 text-md sm:text-xl placeholder-gray-500 bg-white outline-none border-none focus:ring-transparent focus:placeholder-transparent")
    }
}