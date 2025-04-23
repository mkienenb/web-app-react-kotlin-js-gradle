package confexplorer

import api.VideoService
import confexplorer.viewvideo.VideoListReactComponent
import react.FC
import react.Props
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.p
import testsupport.dataCodeElementHandleAttribute


val AppComponent = FC<Props> {
    h1 {
        +"Conference Explorer"
    }

    VideoListReactComponent {
//        videos = VideoService.getVideos()
        videos = TODO()
    }
}