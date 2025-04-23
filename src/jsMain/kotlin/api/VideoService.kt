package api

import confexplorer.viewvideo.Video
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

object VideoService {
    private var fetchURLToJsonFunction : (String) -> String? = {null}

    fun getVideos(): List<Video> {
        return (1..2).mapNotNull{ getVideo(it) }
    }

    fun setFetchURLToJsonFunction(fetchURLToJsonFunction: (String) -> String?) {
        this.fetchURLToJsonFunction = fetchURLToJsonFunction
    }

    fun getVideo(videoId: Int): Video? {

        // input url


        // output json rep of a video
        val json = fetchURLToJsonFunction("/$videoId")

        return if (!json.isNullOrEmpty()) {
            Json.decodeFromString<Video>(json)
        } else {
            null
        }
    }
}