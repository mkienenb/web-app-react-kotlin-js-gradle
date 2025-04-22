package api

import confexplorer.viewvideo.Video

object VideoService {
    private var videos = emptyList<Video>()
    private var fetchFunction : (Int) -> Video? = {null}

    fun getVideos(): List<Video> {
        return (1..2).mapNotNull{ getVideo(it) }
    }

    fun setVideos(videos: List<Video>) {
        this.videos = videos
    }

    fun setFetchFunction(fetchFunction: (Int) -> Video?) {
        this.fetchFunction = fetchFunction
    }

    fun getVideo(videoId: Int): Video? {
        return fetchFunction(videoId)
    }
}