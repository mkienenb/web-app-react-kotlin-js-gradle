package api

import confexplorer.viewvideo.Video
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainExactly
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.w3c.fetch.Response
import org.w3c.fetch.ResponseInit
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag
import kotlin.js.Promise

class VideoServiceTest : ShouldSpec({

    beforeTest() {
        setServiceVideoUrlEnvironmentVariable("http://localhost")
    }
    should("fetch video title Strings of 'Learning Kotlin' and 'Unlearning Java'") {
        val videoList = listOf(
            Video(1, "Learning Kotlin"), Video(2, "Unlearning Java")
        )
        VideoService.setFetchURLToPromiseResponseFunction(createPromiseResponseFetchFunction(videoList))
        val actualVideoTitleList = VideoService.getVideos().map { it.title }
        withClue("unwatched video titles") {
            actualVideoTitleList shouldContainExactly listOf("Learning Kotlin", "Unlearning Java")
        }
    }

    should("fetch video title Strings of 'Learning Kotlin'") {
        val videoList = listOf(Video(1, "Learning Kotlin"))
        VideoService.setFetchURLToPromiseResponseFunction(createPromiseResponseFetchFunction(videoList))
        val actualUnwatchedVideoTitlesList = VideoService.getVideos().map { it.title }
        withClue("unwatched video titles") {
            actualUnwatchedVideoTitlesList shouldContainExactly listOf("Learning Kotlin")
        }
    }

    should("fetch video title Strings of 'Unlearning Java'") {
        val videoList = listOf(Video(2, "Unlearning Java"))
        VideoService.setFetchURLToPromiseResponseFunction(createPromiseResponseFetchFunction(videoList))
        val actualUnwatchedVideoTitlesList = VideoService.getVideos().map { it.title }
        withClue("unwatched video titles") {
            actualUnwatchedVideoTitlesList shouldContainExactly listOf("Unlearning Java")
        }
    }

    should("fetch video title Responses of 'Learning Kotlin'") {
        val videoList = listOf(Video(1, "Learning Kotlin"))
        VideoService.setFetchURLToPromiseResponseFunction(createPromiseResponseFetchFunction(videoList))
        val actualUnwatchedVideoTitlesList = VideoService.getVideos().map { it.title }
        withClue("unwatched video titles") {
            actualUnwatchedVideoTitlesList shouldContainExactly listOf("Learning Kotlin")
        }
    }

    should("fetch video with title of 'Learning Kotlin' from url 'https://shady.videos/kotlin-hands-on/kotlinconf-json/videos/1'") {
        val requestedUrls = mutableListOf<String>()
        setServiceVideoUrlEnvironmentVariable("https://shady.videos")
        VideoService.setFetchURLToPromiseResponseFunction { url ->
            requestedUrls += url
            val blob = Blob(arrayOf("{}"), BlobPropertyBag(type = "application/json"))
            val responseInit = ResponseInit(status = 404)
            Promise.resolve(Response(blob, responseInit))
        }
        VideoService.getVideos()
        withClue("requested video url") {
            requestedUrls.shouldContain("https://shady.videos/kotlin-hands-on/kotlinconf-json/videos/1")
        }
    }

    should("fetch video with title of 'Learning Kotlin' from url 'https://good.videos/kotlin-hands-on/kotlinconf-json/videos/1'") {
        val requestedUrls = mutableListOf<String>()
        setServiceVideoUrlEnvironmentVariable("https://good.videos")
        VideoService.setFetchURLToPromiseResponseFunction { url ->
            requestedUrls += url
            val blob = Blob(arrayOf("{}"), BlobPropertyBag(type = "application/json"))
            val responseInit = ResponseInit(status = 404)
            Promise.resolve(Response(blob, responseInit))
        }
        VideoService.getVideos()
        withClue("requested video url") {
            requestedUrls.shouldContain("https://good.videos/kotlin-hands-on/kotlinconf-json/videos/1")
        }
    }
})

private fun setServiceVideoUrlEnvironmentVariable(value: String) {
    Env.testServiceVideoUrl = value
}

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