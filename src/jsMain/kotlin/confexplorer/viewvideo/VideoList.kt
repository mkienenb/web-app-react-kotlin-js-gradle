package confexplorer.viewvideo

import confexplorer.ElementHandle.UNWATCHED_VIDEO_LIST
import confexplorer.ElementHandle.VIDEO_TITLE
import confexplorer.UISymbol.VIDEO_SELECTOR_SYMBOL
import react.FC
import react.Props
import react.dom.aria.AriaRole
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.span
import react.dom.html.ReactHTML.ul

external interface VideoListProps : Props {
    var videos: List<Video>
    var selectedVideo : Video?
    var onSelectVideo: (Video) -> Unit
}

val VideoList = FC<VideoListProps> { props ->
    ul {
        ariaLabel = UNWATCHED_VIDEO_LIST
        props.videos.forEach { video ->
            li {
                role= AriaRole.option
                ariaSelected = video == props.selectedVideo
                onClick = {
                    props.onSelectVideo(video)
                }
                if(video == props.selectedVideo) {
                    span {
                        +"$VIDEO_SELECTOR_SYMBOL "
                    }
                }
                span {
                    ariaLabel = VIDEO_TITLE
                    +video.title
                }
            }
        }
    }
}