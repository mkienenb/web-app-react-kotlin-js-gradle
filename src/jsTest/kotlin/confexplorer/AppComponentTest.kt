package confexplorer

import api.VideoService
import browserOnlyCode
import confexplorer.viewvideo.Video
import io.kotest.assertions.withClue
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import js.array.asList
import kotest.ReactComponentTestBase
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AppComponentTest: ReactComponentTestBase() {
    init {
        browserOnlyCode {
            should("show 'Conference Explorer' on page") {
                ForComponent(AppComponent) {
                    val pageHeader = container.querySelector("h1")?.textContent
                    withClue("page header") {
                        pageHeader shouldBe "Conference Explorer"
                    }
                }
            }

            should("show unwatched video titles of 'Learning Kotlin' and 'Unlearning Java' on page") {
                VideoService.setFetchURLToJsonFunction { url ->
                    Json.encodeToString(
                    listOf(
                        Video(1, "Learning Kotlin"),
                        Video(2, "Unlearning Java")
                    ).firstOrNull { it.id == url.substringAfterLast('/').toIntOrNull() })
                }
                ForComponent(AppComponent) {
                    val actualUnwatchedVideoTitlesList = container.querySelectorAll("[data-code-element-handle='unwatchedVideo']")
                        .asList()
                        .map { it.textContent }
                    withClue("unwatched video titles") {
                        actualUnwatchedVideoTitlesList shouldContainExactly listOf("Learning Kotlin", "Unlearning Java")
                    }
                }
            }

            should("show unwatched video titles of 'Learning Kotlin' on page") {
                VideoService.setFetchURLFunction { url ->
                    listOf(
                        Video(1, "Learning Kotlin")
                    ).firstOrNull { it.id == url.substringAfterLast('/').toIntOrNull() }
                }
                ForComponent(AppComponent) {
                    val actualUnwatchedVideoTitlesList = container.querySelectorAll("[data-code-element-handle='unwatchedVideo']")
                        .asList()
                        .map { it.textContent }
                    withClue("unwatched video titles") {
                        actualUnwatchedVideoTitlesList shouldContainExactly listOf("Learning Kotlin")
                    }
                }
            }

            should("show unwatched video titles of 'Unlearning Java' on page 2") {
                VideoService.setFetchURLFunction { url ->
                    listOf(
                        Video(1, "Unlearning Java")
                    ).firstOrNull { it.id == url.substringAfterLast('/').toIntOrNull() }
                }
                ForComponent(AppComponent) {
                    val actualUnwatchedVideoTitlesList = container.querySelectorAll("[data-code-element-handle='unwatchedVideo']")
                        .asList()
                        .map { it.textContent }
                    withClue("unwatched video titles") {
                        actualUnwatchedVideoTitlesList shouldContainExactly listOf("Unlearning Java")
                    }
                }
            }
        }
    }
}