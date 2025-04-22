package confexplorer

import api.VideoService
import react.FC
import react.Props
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.p
import testsupport.dataCodeElementHandleAttribute


val AppComponent = FC<Props> {
    h1 {
        +"Conference Explorer"
    }
    VideoService.getVideos().forEach { video ->
        p {
            dataCodeElementHandleAttribute="unwatchedVideo"
            +video.title
        }
    }
}