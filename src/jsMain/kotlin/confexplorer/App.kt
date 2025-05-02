package confexplorer

import api.VideoService
import confexplorer.viewvideo.Video
import confexplorer.viewvideo.VideoList
import confexplorer.viewvideo.VideoPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.kodein.di.instance
import react.*
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.p
import reactdi.DIContext
import testsupport.dataCodeElementHandleAttribute


val App = FC<Props> {
    var videoList: List<Video> by useState(emptyList())
    val (getSelectedVideo, setSelectedVideo) = useState<Video?>(null)
    val di = use(DIContext)!!
    val videoService: VideoService by di.instance()
    val scope: CoroutineScope by di.instance()

    useEffectOnce {
        scope.launch {
            videoList = videoService.getVideos()
        }
    }
    h1 {
        +"Conference Explorer"
    }

    div {
        dataCodeElementHandleAttribute="videoLists"
        if (videoList.isNotEmpty()) {
            VideoList {
                videos = videoList
                this.selectedVideo = getSelectedVideo
                onSelectVideo = { video -> setSelectedVideo(video) }
            }
        } else {
            p {
                dataCodeElementHandleAttribute="loading"
                +"Loading..."
            }
        }
    }

    VideoPlayer {
        video = Video(1, "Learning kotlin", "www.youtube.com/learning-kotlin")
    }
}