package confexplorer

import api.VideoService
import confexplorer.viewvideo.Video
import confexplorer.viewvideo.VideoListReactComponent
import kotlinx.coroutines.launch
import react.FC
import react.Props
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.p
import react.useEffectOnce
import react.useState
import testsupport.ScopeProvider
import testsupport.dataCodeElementHandleAttribute

val AppComponent = FC<Props> { props ->
    var unwatchedVideos: List<Video> by useState(emptyList())

    useEffectOnce {
        ScopeProvider.scope().launch {
            unwatchedVideos = VideoService.getVideos()
        }
    }

    h1 {
        +"Conference Explorer"
    }

    VideoListReactComponent {
        videos = unwatchedVideos
    }
}