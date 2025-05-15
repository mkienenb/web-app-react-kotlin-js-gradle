package confexplorer

import api.VideoService
import confexplorer.ElementHandle.LOADING
import confexplorer.ElementHandle.UNWATCHED_VIDEO_LIST
import confexplorer.ElementHandle.WATCHED_VIDEO_LIST
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
    var unwatchedVideoList: List<Video> by useState(emptyList())
    var watchedVideoList: List<Video> by useState(emptyList())
    val (getSelectedVideo, setSelectedVideo) = useState<Video?>(null)
    val di = use(DIContext)!!
    val videoService: VideoService by di.instance()
    val scope: CoroutineScope by di.instance()

    useEffectOnce {
        scope.launch {
            unwatchedVideoList = videoService.getVideos()
        }
    }
    h1 {
        +"Conference Explorer"
    }

    div {
        dataCodeElementHandleAttribute = "videoLists"
        if (unwatchedVideoList.isNotEmpty()) {
            VideoList {
                videoListLabel = UNWATCHED_VIDEO_LIST
                videos = unwatchedVideoList
                this.selectedVideo = getSelectedVideo
                onSelectVideo = { video -> setSelectedVideo(video) }
            }
            VideoList {
                videoListLabel = WATCHED_VIDEO_LIST
                videos = watchedVideoList
                this.selectedVideo = getSelectedVideo
                onSelectVideo = { video -> setSelectedVideo(video) }
            }
        } else {
            p {
                dataCodeElementHandleAttribute = LOADING
                +"Loading..."
            }
        }
    }

    getSelectedVideo?.let { selectedVideo ->
        VideoPlayer {
            video = selectedVideo
            onMarkAsWatched = { video ->
                unwatchedVideoList -= video
                watchedVideoList += video
            }
        }
    }


}