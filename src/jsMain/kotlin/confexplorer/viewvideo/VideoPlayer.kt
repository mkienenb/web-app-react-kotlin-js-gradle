package confexplorer.viewvideo

import confexplorer.ElementHandle.MARK_AS_WATCHED_BUTTON
import confexplorer.ElementHandle.REACT_PLAYER
import confexplorer.ElementHandle.VIDEO_DETAIL_TITLE
import react.FC
import react.Props
import react.dom.aria.AriaRole
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h3
import react.dom.html.ReactHTML.span
import testsupport.dataCodeElementHandleAttribute

external interface VideoPlayerProps : Props {
    var video: Video
    var onMarkAsWatched: (Video) -> Unit
}

val VideoPlayer = FC<VideoPlayerProps> { props ->
    div {
        h3 {
            ariaLabel = VIDEO_DETAIL_TITLE
            +props.video.title
        }
        button {
            ariaLabel = MARK_AS_WATCHED_BUTTON
            onClick = {
                props.onMarkAsWatched(props.video)
            }

        }
        span {
            ariaLabel = REACT_PLAYER
            ReactPlayerHolder {
                url = props.video.videoUrl
                controls = true
            }
        }
    }
}