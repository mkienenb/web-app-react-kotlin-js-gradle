import react.FC
import react.Props
import react.dom.html.ReactHTML.div

external interface WelcomeProps : Props {
    var userName: String
}

val WelcomeComponent = FC<WelcomeProps> { props ->
    div {
        +"Welcome, ${props.userName}!"
    }
}