package confexplorer.viewvideo

import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h3
import react.dom.html.ReactHTML.span
import testsupport.dataCodeElementHandleAttribute

external interface VideoPlayerProps : Props {
    var video: Video
}

val VideoPlayer = FC<VideoPlayerProps> {
    div {
        h3 {
            dataCodeElementHandleAttribute = "video-detail-title"
            +"Learning kotlin"
        }
        span {
            dataCodeElementHandleAttribute = "react-player-url"
            +"www.youtube.com/learning-kotlin"
        }
    }
}