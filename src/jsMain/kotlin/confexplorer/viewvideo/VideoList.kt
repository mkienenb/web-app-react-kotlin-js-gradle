package confexplorer.viewvideo

import confexplorer.UISymbol.VIDEO_SELECTOR_SYMBOL
import react.FC
import react.Props
import react.dom.aria.AriaRole
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.span
import react.dom.html.ReactHTML.ul
import testsupport.dataCodeElementHandleAttribute

external interface VideoListProps : Props {
    var videos: List<Video>
}

val VideoList = FC<VideoListProps> { props ->
    ul {
        props.videos.forEach {
            li {
                role= AriaRole.option
                ariaSelected = it.title == "Learning kotlin"
                dataCodeElementHandleAttribute = "unwatched-video-title"
                +it.title
//                if (it.title == "Learning kotlin") {
//                    span {
//                        dataCodeElementHandleAttribute = "video-selection-indicator"
//                        +VIDEO_SELECTOR_SYMBOL
//                    }
//                }
            }
        }
    }
}