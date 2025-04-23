package api

import confexplorer.viewvideo.Video
import kotlinx.coroutines.await
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.w3c.fetch.Response
import kotlin.js.Promise

object VideoService {
    private var fetchURLToJsonFunction : (String) -> String? = {null}
    private lateinit var fetchURLToResponseFunction : (String) -> Response
    private lateinit var fetchURLToPromiseResponseFunction : (String) -> Promise<Response>

    suspend fun getVideos(): List<Video> {
        return (1..2).mapNotNull{ getVideo(it) }
    }

    fun setFetchURLToJsonFunction(fetchURLToJsonFunction: (String) -> String?) {
        this.fetchURLToJsonFunction = fetchURLToJsonFunction
    }

    fun setFetchURLToPromiseResponseFunction(fetchURLToPromiseResponseFunction: (String) -> Promise<Response>) {
        this.fetchURLToPromiseResponseFunction = fetchURLToPromiseResponseFunction
    }

    private suspend fun getVideo(videoId: Int): Video? {

        // input url


        val response = fetchURLToResponseFunction("/$videoId")
        if (response.status == 404.toShort()) {
            return null
        }

        return Json.decodeFromString<Video>(response.text().await())
    }
}