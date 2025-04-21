package confexplorer.viewvideo

import react.FC
import react.Props
import react.dom.html.ReactHTML.ul
import react.dom.html.ReactHTML.li

external interface VideoListProps : Props {
    var videos: List<Video>
}

val VideoListReactComponent = FC<VideoListProps> { props ->
    ul {
        li {
            props.videos.first().let {
                +it.title
            }
        }
    }
}