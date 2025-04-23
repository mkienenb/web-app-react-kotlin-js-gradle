package api

import confexplorer.viewvideo.Video
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.w3c.fetch.Response

object VideoService {
    private var fetchURLToJsonFunction : (String) -> String? = {null}
    private var fetchURLToResponseFunction : (String) -> Response? = {null}

    fun getVideos(): List<Video> {
        return (1..2).mapNotNull{ getVideo(it) }
    }

    fun setFetchURLToJsonFunction(fetchURLToJsonFunction: (String) -> String?) {
        this.fetchURLToJsonFunction = fetchURLToJsonFunction
    }

    fun setFetchURLToResponseFunction(fetchURLToResponseFunction: (String) -> Response?) {
        this.fetchURLToResponseFunction = fetchURLToResponseFunction
    }

    private fun getVideo(videoId: Int): Video? {

        // input url


        // output json rep of a video
        val json = fetchURLToJsonFunction("/$videoId")

        return if (!json.isNullOrEmpty() && json != "{}") {
            Json.decodeFromString<Video>(json)
        } else {
            null
        }
    }
}