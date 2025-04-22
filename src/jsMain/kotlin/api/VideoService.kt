package api

import confexplorer.viewvideo.Video

object VideoService {
    private var videos = emptyList<Video>()
    private var fetchFunction : (Int) -> Video? = {null}

    fun getVideos(): List<Video> {
        return videos
    }

    fun setVideos(videos: List<Video>) {
        this.videos = videos
    }

    fun setFetchFunction(fetchFunction: (Int) -> Video?) {
        this.fetchFunction = fetchFunction
    }
}