package designsystemcomponents

import react.FC
import react.Props
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.span
import react.dom.html.ReactHTML.ul
import react.dom.svg.ReactSVG.path
import react.dom.svg.ReactSVG.svg
import web.cssom.ClassName

external interface PositiveListProps : Props {
    var items: List<String>
}

val PositiveList = FC<PositiveListProps> { props ->
    ul {
        className = ClassName("my-8 divide-y divide-gray-100")
        props.items.forEach { result ->
            li {
                key = result
                className = ClassName("px-3 text-lg leading-10 odd:bg-gray-50")
                svg {
                    viewBox = "0 0 32 32"
                    width = 1.2
                    height = 1.2
                    xmlns = "http://www.w3.org/2000/svg"
                    className = ClassName("inline-block")

                    path {
                        fill = "#66ba1c"
                        d = "m14 21.414l-5-5.001L10.413 15L14 18.586L21.585 11L23 12.415l-9 8.999z"
                    }
                    path {
                        fill = "#66ba1c"
                        d = "M16 2a14 14 0 1 0 14 14A14 14 0 0 0 16 2Zm0 26a12 12 0 1 1 12-12a12 12 0 0 1-12 12Z"
                    }
                }
                span {
                    className = ClassName("pl-2")
                    +result
                }
            }
        }
    }
}