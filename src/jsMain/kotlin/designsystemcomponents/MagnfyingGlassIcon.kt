package designsystemcomponents

import react.FC
import react.Props
import react.dom.svg.ReactSVG.path
import react.dom.svg.ReactSVG.svg
import react.dom.svg.StrokeLinecap
import react.dom.svg.StrokeLinejoin
import web.cssom.ClassName

val MagnfyingGlassIcon = FC<Props> { _ ->
    svg {
        xmlns = "http://www.w3.org/2000/svg"
        fill = "none"
        viewBox = "0 0 24 24"
        strokeWidth = 1.5
        stroke = "currentColor"
        className = ClassName("w-6 h-6")

        path {
            strokeLinecap = StrokeLinecap.round
            strokeLinejoin = StrokeLinejoin.round
            d = "M21 21l-5.197-5.197m0 0A7.5 7.5 0 105.196 5.196a7.5 7.5 0 0010.607 10.607z"
        }
    }
}