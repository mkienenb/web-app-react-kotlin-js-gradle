package api

import confexplorer.viewvideo.Video
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.serialization.decodeFromString
import org.w3c.fetch.Response
import kotlin.js.Promise
import kotlinx.serialization.json.Json

const val serverPrefix = "https://my-json-server.typicode.com"

object VideoService {
    private var fetchURLToJsonFunction : (String) -> Promise<Response> = { url -> window.fetch(url) }

    suspend fun getVideos(): List<Video> {
        return (1..2).mapNotNull{ getVideo(it) }
    }

    fun setFetchURLToJsonFunction(fetchURLToJsonFunction: (String) -> Promise<Response>) {
        this.fetchURLToJsonFunction = fetchURLToJsonFunction
    }

    private suspend fun getVideo(videoId: Int): Video? {
        val url = "${serverPrefix()}${VideoServiceLocator.CONTEXT_PATH}$videoId"
        val data = fetchURLToJsonFunction(url)
        val response = data.await()
            .text()
            .await()
        return Json.decodeFromString(response)
    }

    private fun serverPrefix(): String {
        return Env.serviceVideoUrl
    }
}

object Env {
    val serviceVideoUrl: String
        get() = js("process.env.SERVICE_VIDEO_URL") as String
}
