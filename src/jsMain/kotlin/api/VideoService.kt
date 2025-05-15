package api

import api.VideoServiceLocator.CONTEXT_PATH
import confexplorer.viewvideo.Video
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.w3c.fetch.Response
import kotlin.js.Promise

typealias URLToPromiseResponseFunction = suspend (String) -> Promise<Response>

class VideoService(
    private var fetchURLToPromiseResponseFunction: URLToPromiseResponseFunction,
    private var urlProvider: UrlProvider
) {

    suspend fun getVideos(): List<Video> {
        return (1..4).mapNotNull{ getVideo(it) }
    }

    fun setFetchURLToPromiseResponseFunction(fetchURLToPromiseResponseFunction: URLToPromiseResponseFunction) {
        this.fetchURLToPromiseResponseFunction = fetchURLToPromiseResponseFunction
    }

    private suspend fun getVideo(videoId: Int): Video? {
        val url = "${urlProvider.getBaseUrl()}$CONTEXT_PATH$videoId"
        val responsePromise = fetchURLToPromiseResponseFunction(url)
        val response = responsePromise.await()
        if (response.status == 404.toShort()) {
            return null
        }

        val json = response.text().await()
        return Json.decodeFromString<Video>(json)
    }
}