package api

import confexplorer.viewvideo.Video
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldContainExactly
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class VideoServiceTest : ShouldSpec({

    should("show unwatched video titles of 'Learning Kotlin' and 'Unlearning Java' on page") {
        VideoService.setFetchURLToJsonFunction { url ->
            Json.encodeToString(
                listOf(
                    Video(1, "Learning Kotlin"), Video(2, "Unlearning Java")
                ).firstOrNull { it.id == url.substringAfterLast('/').toIntOrNull() })
        }
        val actualVideoTitleList = VideoService.getVideos().map { it.title }
        withClue("unwatched video titles") {
            actualVideoTitleList shouldContainExactly listOf("Learning Kotlin", "Unlearning Java")
        }
    }

    should("show unwatched video titles of 'Learning Kotlin' on page") {
        VideoService.setFetchURLToJsonFunction { url ->
            Json.encodeToString(
                listOf(
                    Video(1, "Learning Kotlin")
                ).firstOrNull { it.id == url.substringAfterLast('/').toIntOrNull() })
        }
        val actualUnwatchedVideoTitlesList = VideoService.getVideos().map { it.title }
        withClue("unwatched video titles") {
            actualUnwatchedVideoTitlesList shouldContainExactly listOf("Learning Kotlin")
        }
    }

    should("show unwatched video titles of 'Unlearning Java' on page 2") {
        VideoService.setFetchURLToJsonFunction { url ->
            Json.encodeToString(
                listOf(
                    Video(2, "Unlearning Java")
                ).firstOrNull { it.id == url.substringAfterLast('/').toIntOrNull() })
        }
        val actualUnwatchedVideoTitlesList = VideoService.getVideos().map { it.title }
        withClue("unwatched video titles") {
            actualUnwatchedVideoTitlesList shouldContainExactly listOf("Unlearning Java")
        }
    }
})