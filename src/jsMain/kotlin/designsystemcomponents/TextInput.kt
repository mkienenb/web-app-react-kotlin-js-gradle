import react.dom.html.InputHTMLAttributes
import react.dom.html.ReactHTML.input
import react.FC
import react.useEffectOnce
import react.useRef
import web.cssom.ClassName
import web.html.HTMLInputElement

external interface TextInputProps : InputHTMLAttributes<HTMLInputElement> {
    var dataTestAttribute : String
}

val TextInput = FC<TextInputProps> { props ->
    val inputRef = useRef<HTMLInputElement>(null)

    useEffectOnce {
        inputRef.current?.setAttribute("data-test", props.dataTestAttribute)
    }

    input {
        ref = inputRef
        className = ClassName("grow px-3 py-2 text-gray-700 text-md sm:text-xl placeholder-gray-500 bg-white outline-none border-none focus:ring-transparent focus:placeholder-transparent")
    }
}