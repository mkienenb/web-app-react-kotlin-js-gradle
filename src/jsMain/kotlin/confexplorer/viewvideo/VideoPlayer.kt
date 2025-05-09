package confexplorer.viewvideo

import confexplorer.ElementHandle.REACT_PLAYER
import confexplorer.ElementHandle.VIDEO_DETAIL_TITLE
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
            dataCodeElementHandleAttribute = VIDEO_DETAIL_TITLE
            +props.video.title
        }
        span {
            dataCodeElementHandleAttribute = REACT_PLAYER
            ReactPlayerHolder {
                url = props.video.videoUrl
                controls = true
            }
        }
    }
}