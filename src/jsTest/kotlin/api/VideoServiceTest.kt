package api

import confexplorer.productionModule
import confexplorer.viewvideo.Video
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainExactly
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.instance
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
        val videoService = createVideoService(videoList)
        val actualVideoTitleList = videoService.getVideos().map { it.title }
        withClue("unwatched video titles") {
            actualVideoTitleList shouldContainExactly listOf("Learning Kotlin", "Unlearning Java")
        }
    }

    should("fetch video title Strings of 'Learning Kotlin'") {
        val videoList = listOf(Video(1, "Learning Kotlin"))
        val videoService = createVideoService(videoList)
        val actualUnwatchedVideoTitlesList = videoService.getVideos().map { it.title }
        withClue("unwatched video titles") {
            actualUnwatchedVideoTitlesList shouldContainExactly listOf("Learning Kotlin")
        }
    }

    should("fetch video title Strings of 'Unlearning Java'") {
        val videoList = listOf(Video(2, "Unlearning Java"))
        val videoService = createVideoService(videoList)
        val actualUnwatchedVideoTitlesList = videoService.getVideos().map { it.title }
        withClue("unwatched video titles") {
            actualUnwatchedVideoTitlesList shouldContainExactly listOf("Unlearning Java")
        }
    }

    should("fetch video title Responses of 'Learning Kotlin'") {
        val videoList = listOf(Video(1, "Learning Kotlin"))
        val videoService = createVideoService(videoList)
        val actualUnwatchedVideoTitlesList = videoService.getVideos().map { it.title }
        withClue("unwatched video titles") {
            actualUnwatchedVideoTitlesList shouldContainExactly listOf("Learning Kotlin")
        }
    }

    should("fetch video with title of 'Learning Kotlin' from url 'https://shady.videos/kotlin-hands-on/kotlinconf-json/videos/1'") {
        val requestedUrls = mutableListOf<String>()
        setServiceVideoUrlEnvironmentVariable("https://shady.videos")
        val videoService = VideoService(createURLVerificationFetchFunction(requestedUrls),
            object : UrlProvider {
                override fun getBaseUrl(): String = "https://shady.videos"
            })
        videoService.getVideos()
        withClue("requested video url") {
            requestedUrls.shouldContain("https://shady.videos/kotlin-hands-on/kotlinconf-json/videos/1")
        }
    }

    should("fetch video with title of 'Learning Kotlin' from url 'https://good.videos/kotlin-hands-on/kotlinconf-json/videos/1'") {
        val requestedUrls = mutableListOf<String>()
        val videoService = VideoService(createURLVerificationFetchFunction(requestedUrls),
            object : UrlProvider {
                override fun getBaseUrl(): String = "https://good.videos"
            })
        videoService.getVideos()
        withClue("requested video url") {
            requestedUrls.shouldContain("https://good.videos/kotlin-hands-on/kotlinconf-json/videos/1")
        }
    }
    should("fetch video with title of 'Learning Kotlin' from url 'https://okay.videos/kotlin-hands-on/kotlinconf-json/videos/1' using environment variable") {
        setServiceVideoUrlEnvironmentVariable("https://okay.videos")
        val requestedUrls = mutableListOf<String>()
        val prodDI = DI {
            import(productionModule)
        }
        val videoService = prodDI.di.instance<VideoService>() as VideoService
        videoService.getVideos()
        withClue("requested video url") {
            requestedUrls.shouldContain("https://okay.videos/kotlin-hands-on/kotlinconf-json/videos/1")
        }

    }
})

private fun createURLVerificationFetchFunction(requestedUrls: MutableList<String>): URLToPromiseResponseFunction {
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