package api

import confexplorer.viewvideo.Video
import kotlinx.serialization.json.Json
import org.w3c.fetch.Response
import org.w3c.fetch.ResponseInit
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag
import kotlin.js.Promise

fun createURLVerificationFetchFunction(requestedUrls: MutableList<String>): URLToPromiseResponseFunction {
    return { url: String ->
        requestedUrls += url
        val blob = Blob(arrayOf("{}"), BlobPropertyBag(type = "application/json"))
        val responseInit = ResponseInit(status = 404)
        Promise.resolve(Response(blob, responseInit))
    }
}

fun createVideoService(videoList: List<Video>) =
    VideoService(createPromiseResponseFetchFunction(videoList),
        object : UrlProvider {
            override fun getBaseUrl(): String = "http://localhost"
        })

fun createPromiseResponseFetchFunction(videoList: List<Video>): URLToPromiseResponseFunction {
    return {
            url ->
        val video = videoList.firstOrNull {
            it.id == url.substringAfterLast('/').toIntOrNull()
        }
        var json = "{}"
        if (video != null) {
            json = Json.encodeToString(video)
        }
        val blob = Blob(arrayOf(json), BlobPropertyBag(type = "application/json"))
        val responseInit = ResponseInit(status = if (video != null) 200 else 404)
        Promise.resolve(Response(blob, responseInit))
    }
}