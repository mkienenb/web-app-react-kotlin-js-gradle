package designsystemcomponents

import emotion.css.css
import react.FC
import react.PropsWithChildren
import web.cssom.ClassName
import react.dom.html.ReactHTML.div
import emotion.react.css
import web.cssom.px

val FocusPageLayout = FC<PropsWithChildren> { props ->
    div {
        className = ClassName("container mx-auto h-screen")
        css {
            maxWidth = 640.px
        }

        div {
            className = ClassName("pt-8 md:pt-16 h-full")
            div {
                className = ClassName("py-4 px-10 h-full bg-white rounded-t-md")
                +props.children
            }
        }
    }
}