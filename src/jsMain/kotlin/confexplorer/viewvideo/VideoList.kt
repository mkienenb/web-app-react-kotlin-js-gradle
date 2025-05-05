package confexplorer.viewvideo

import react.FC
import react.Props
import react.dom.aria.AriaRole
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.ul
import testsupport.dataCodeElementHandleAttribute

external interface VideoListProps : Props {
    var videos: List<Video>
    var selectedVideo : Video?
    var onSelectVideo: (Video) -> Unit
}

val VideoList = FC<VideoListProps> { props ->
    ul {
        props.videos.forEach { video ->
            li {
                role= AriaRole.option
//                ariaSelected = video == props.selectedVideo
                  ariaSelected = true
                onClick = {
                    props.onSelectVideo(video)
                }
                dataCodeElementHandleAttribute = "unwatched-video-title"
                +video.title
            }
        }
    }
}