package api

import api.VideoServiceLocator.CONTEXT_PATH
import confexplorer.viewvideo.Video
import kotlinx.coroutines.await
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.w3c.fetch.Response
import kotlin.js.Promise

object VideoService {
    private lateinit var fetchURLToPromiseResponseFunction : (String) -> Promise<Response>

    suspend fun getVideos(): List<Video> {
        return (1..2).mapNotNull{ getVideo(it) }
    }

    fun setFetchURLToPromiseResponseFunction(fetchURLToPromiseResponseFunction: (String) -> Promise<Response>) {
        this.fetchURLToPromiseResponseFunction = fetchURLToPromiseResponseFunction
    }

    private suspend fun getVideo(videoId: Int): Video? {
        val url = "${Env.serviceVideoUrl}$CONTEXT_PATH$videoId"
        val responsePromise = fetchURLToPromiseResponseFunction(url)
        val response = responsePromise.await()
        if (response.status == 404.toShort()) {
            return null
        }

        val json = response.text().await()
        return Json.decodeFromString<Video>(json)
    }
}