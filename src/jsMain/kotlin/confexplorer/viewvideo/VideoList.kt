package confexplorer.viewvideo

import confexplorer.UISymbol.VIDEO_SELECTOR_SYMBOL
import react.FC
import react.Props
import react.dom.html.ReactHTML.ul
import react.dom.html.ReactHTML.li
import testsupport.dataCodeElementHandleAttribute

external interface VideoListProps : Props {
    var videos: List<Video>
}

val VideoList = FC<VideoListProps> { props ->
    ul {
        props.videos.forEach {
            li {
                dataCodeElementHandleAttribute="unwatchedVideo"


                +"${if(it.title == "Learning kotlin") VIDEO_SELECTOR_SYMBOL else ""}${it.title}"
            }
        }
    }
}