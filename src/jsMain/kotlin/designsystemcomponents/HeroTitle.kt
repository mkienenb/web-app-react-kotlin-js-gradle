import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.span
import react.FC
import react.Props
import web.cssom.ClassName

external interface HeroTitleProps : Props {
    var title : String;
}

val HeroTitle = FC<HeroTitleProps> { props ->
    h2 {
        className = ClassName("-mt-11 mb-8 text-4xl font-extrabold leading-[3.5rem] tracking-wide text-center")
    }
    span {
        className = ClassName("px-4 py-1 bg-amber-100")
        span {
            className = ClassName("bg-clip-text bg-gradient-to-r from-indigo-600 to-pink-500 text-transparent uppercase ")
            +title
        }
    }

}