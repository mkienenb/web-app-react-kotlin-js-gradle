package api

import confexplorer.viewvideo.Video

object VideoService {
    private var fetchFunction : (Int) -> Video? = {null}
    private var fetchURLFunction : (String) -> Video? = {null}

    fun getVideos(): List<Video> {
        return (1..2).mapNotNull{ getVideo(it) }
    }

    fun setFetchFunction(fetchFunction: (Int) -> Video?) {
        this.fetchFunction = fetchFunction
    }

    fun setFetchURLFunction(fetchURLFunction: (String) -> Video?) {
        this.fetchURLFunction = fetchURLFunction
    }

    fun getVideo(videoId: Int): Video? {
        return fetchFunction(videoId)
    }
}