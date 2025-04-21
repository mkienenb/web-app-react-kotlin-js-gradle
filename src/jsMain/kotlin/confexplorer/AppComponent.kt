package confexplorer

import react.FC
import react.Props
import react.dom.html.ReactHTML.h1


val AppComponent = FC<Props> {
    h1 {
        +"Conference Explorer"
    }
}