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

val VideoPlayer = FC<VideoPlayerProps> { props ->
    div {
        h3 {
            dataCodeElementHandleAttribute = "video-detail-title"
            +props.video.title
        }
        span {
            dataCodeElementHandleAttribute = "react-player"
            ReactPlayer {
                url = props.video.videoUrl
                controls = true
            }
        }
    }
}