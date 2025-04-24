package api

import confexplorer.viewvideo.Video
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.w3c.fetch.Response
import org.w3c.fetch.ResponseInit
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag
import kotlin.js.Promise

class VideoServiceTest : ShouldSpec({

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
        setEnvironmentVariable("URL", "https://shady.videos")
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
        setEnvironmentVariable("URL", "https://good.videos")
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

private fun setEnvironmentVariable(key: String, value: String) {
}

// SOME IDEAS
//private fun setEnvironmentVariable(key: String, value: String) {
//    EnvironmentVariableService.setEnvVar(key) { js("code") as String }
//}
//
//object EnvironmentVariableService {
//    fun setEnvVar(key: String, valueProvider: () -> String) {
//
//    }
//}
//
//
//object EnvService {
//    fun getEnvVar(key: String): String? {
//        // Tries JS-style process.env
//        val processEnv = js("typeof process !== 'undefined' && process.env ? process.env[key] : undefined")
//        if (processEnv != undefined && processEnv != null) {
//            return processEnv as? String
//        }
//
//        // Fallback: check if injected on window (e.g., in index.html or test setup)
//        val windowVar = kotlinx.browser.window.asDynamic()[key]
//        return windowVar as? String
//    }
//
//    fun setEnvVar(key: String, value: String) {
//        // For test environments or client-side mocks
//        kotlinx.browser.window.asDynamic()[key] = value
//    }
//}
//
//
//object EnvService {
//    private val global = js("typeof globalThis !== 'undefined' ? globalThis : {}")
//
//    fun getEnvVar(key: String): String? {
//        // First try global.process.env (for Node/Webpack)
//        val processEnv = global.process?.env?.unsafeCast<dynamic>()?.let { it[key] }
//        if (processEnv != undefined && processEnv != null) {
//            return processEnv as? String
//        }
//
//        // Then try global[key] directly (for browser-style injection)
//        val globalVar = global[key]
//        return globalVar as? String
//    }
//
//    fun setEnvVar(key: String, value: String) {
//        // Injects into globalThis directly
//        global[key] = value
//    }
//}
//

private fun createPromiseResponseFetchFunction(videoList: List<Video>): (String) -> Promise<Response> {
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