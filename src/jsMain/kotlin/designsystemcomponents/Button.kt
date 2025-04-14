package designsystemcomponents

import react.dom.html.InputHTMLAttributes
import react.dom.html.ReactHTML.button
import react.FC
import web.cssom.ClassName
import web.html.HTMLInputElement

external interface ButtonProps : InputHTMLAttributes<HTMLInputElement>

val Button = FC<ButtonProps> { props ->
    button {
        className=ClassName("grow-0 px-4 py-3 text-sm font-medium tracking-wider text-black uppercase transition-colors duration-300 transform bg-transparent border rounded-md hover:bg-sky-50 focus:bg-sky-50 focus:outline-none")
        +props.children
    }
}