package api

import confexplorer.viewvideo.Video
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.delay
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.w3c.fetch.Response
import org.w3c.fetch.ResponseInit
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag
import kotlin.js.Promise

class VideoServiceTest : ShouldSpec({
    should("show unwatched video titles of 'Learning Kotlin' and 'Unlearning Java' on page") {
        fun fetchFunction(videos: List<Video>): (String) -> Promise<Response> = { url ->
            val id = url.substringAfterLast('/').toIntOrNull()
            val video = videos.firstOrNull { it.id == id }

            val json = Json.encodeToString(video)
            val blob = Blob(arrayOf(json), BlobPropertyBag(type = "application/json"))
            val responseInit = ResponseInit(status = if (video != null) 200 else 404)

            Promise.resolve(Response(blob, responseInit))
        }

        VideoService.setFetchURLToJsonFunction(
            fetchFunction(
                listOf(
                    Video(1, "Learning Kotlin"),
                    Video(2, "Unlearning Java")
                )
            )
        )

        val actualVideoList = VideoService.getVideos()

        withClue("unwatched video titles") {
            actualVideoList.map { it.title } shouldContainExactly listOf("Learning Kotlin", "Unlearning Java")
        }
        "A" shouldBe "Not A"
    }
})