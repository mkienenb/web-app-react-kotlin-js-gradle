package api

import confexplorer.viewvideo.Video
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldContainExactly
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class VideoServiceTest : ShouldSpec({

    should("fetch video title Strings of 'Learning Kotlin' and 'Unlearning Java'") {
        val videoList = listOf(
            Video(1, "Learning Kotlin"), Video(2, "Unlearning Java")
        )
        VideoService.setFetchURLToJsonFunction(createJsonFetchFunction(videoList))
        val actualVideoTitleList = VideoService.getVideos().map { it.title }
        withClue("unwatched video titles") {
            actualVideoTitleList shouldContainExactly listOf("Learning Kotlin", "Unlearning Java")
        }
    }

    should("fetch video title Strings of 'Learning Kotlin'") {
        val videoList = listOf(Video(1, "Learning Kotlin"))
        VideoService.setFetchURLToJsonFunction(createJsonFetchFunction(videoList))
        val actualUnwatchedVideoTitlesList = VideoService.getVideos().map { it.title }
        withClue("unwatched video titles") {
            actualUnwatchedVideoTitlesList shouldContainExactly listOf("Learning Kotlin")
        }
    }

    should("fetch video title Strings of 'Unlearning Java'") {
        val videoList = listOf(Video(2, "Unlearning Java"))
        VideoService.setFetchURLToJsonFunction(createJsonFetchFunction(videoList))
        val actualUnwatchedVideoTitlesList = VideoService.getVideos().map { it.title }
        withClue("unwatched video titles") {
            actualUnwatchedVideoTitlesList shouldContainExactly listOf("Unlearning Java")
        }
    }
})

private fun createJsonFetchFunction(videoList: List<Video>): (String) -> String {
    return {
        url ->
            val video = videoList.firstOrNull {
                it.id == url.substringAfterLast('/').toIntOrNull()
            }
            if (video == null) {
                "{}"
            } else {
                Json.encodeToString(video)
            }
    }
}