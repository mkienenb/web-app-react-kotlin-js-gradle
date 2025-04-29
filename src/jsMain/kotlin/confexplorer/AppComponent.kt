package confexplorer

import api.VideoService
import confexplorer.viewvideo.Video
import confexplorer.viewvideo.VideoListReactComponent
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.h3
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.span
import react.useEffectOnce
import react.useState
import testsupport.dataCodeElementHandleAttribute


val AppComponent = FC<Props> {
    var videoList: List<Video> by useState(emptyList())

    useEffectOnce {
        MainScope().launch {
            videoList = VideoService.getVideos()
        }
    }
    h1 {
        +"Conference Explorer"
    }

    div {
        dataCodeElementHandleAttribute="videoLists"
        if (videoList.isNotEmpty()) {
            VideoListReactComponent {
                videos = videoList
            }
        } else {
            p {
                dataCodeElementHandleAttribute="loading"
                +"Loading..."
            }
        }
    }

    div {
        h3 {
            dataCodeElementHandleAttribute="video-detail-title"
            +"Learning kotlin"
        }
        span {
            dataCodeElementHandleAttribute="react-player-url"
            +"www.youtube.com/learning-kotlin"
        }
    }
}