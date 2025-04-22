package api

import confexplorer.viewvideo.Video

object VideoService {
    private var fetchURLFunction : (String) -> Video? = {null}
    private var fetchURLToJsonFunction : (String) -> String? = {null}

    fun getVideos(): List<Video> {
        return (1..2).mapNotNull{ getVideo(it) }
    }

    fun setFetchURLFunction(fetchURLFunction: (String) -> Video?) {
        this.fetchURLFunction = fetchURLFunction
    }

    fun setFetchURLToJsonFunction(fetchURLToJsonFunction: (String) -> String?) {
        this.fetchURLToJsonFunction = fetchURLToJsonFunction
    }

    fun getVideo(videoId: Int): Video? {
        return fetchURLFunction("/$videoId")
    }
}