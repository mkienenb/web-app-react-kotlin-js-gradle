package confexplorer

import api.VideoService
import browserOnlyCode
import confexplorer.viewvideo.Video
import io.kotest.assertions.withClue
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import js.array.asList
import kotest.ReactComponentTestBase
import kotlinx.coroutines.delay
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.w3c.fetch.Response
import kotlin.js.Promise

import org.w3c.fetch.ResponseInit
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag


private const val delay: Long = 10

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
                VideoService.setFetchURLToJsonFunction(
                    fetchFunction(
                        listOf(
                            Video(1, "Learning Kotlin"),
                            Video(2, "Unlearning Java")
                        )
                    )
                )
                ForComponentCallingCoroutines(AppComponent) {
                    delay(delay)
                    val actualUnwatchedVideoTitlesList = container.querySelectorAll("[data-code-element-handle='unwatchedVideo']")
                        .asList()
                        .map { it.textContent }
                    withClue("unwatched video titles") {
                        actualUnwatchedVideoTitlesList shouldContainExactly listOf("Learning Kotlin", "Unlearning Java")
                    }
                }
            }

            should("show unwatched video titles of 'Learning Kotlin' on page") {
                VideoService.setFetchURLToJsonFunction(
                    fetchFunction(
                        listOf(
                            Video(1, "Learning Kotlin"),
                        )
                    )
                )
                ForComponentCallingCoroutines(AppComponent) {
                    delay(delay)
                    val actualUnwatchedVideoTitlesList = container.querySelectorAll("[data-code-element-handle='unwatchedVideo']")
                        .asList()
                        .map { it.textContent }
                    withClue("unwatched video titles") {
                        actualUnwatchedVideoTitlesList shouldContainExactly listOf("Learning Kotlin")
                    }
                }
            }

            should("show unwatched video titles of 'Unlearning Java' on page 2") {
                VideoService.setFetchURLToJsonFunction(
                    fetchFunction(
                        listOf(
                            Video(2, "Unlearning Java")
                        )
                    )
                )
                ForComponentCallingCoroutines(AppComponent) {
                    delay(delay)
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

    private fun fetchFunction(videos: List<Video>): (String) -> Promise<Response> = { url ->
        val id = url.substringAfterLast('/').toIntOrNull()
        val video = videos.firstOrNull { it.id == id }

        val json = Json.encodeToString(video)
        val blob = Blob(arrayOf(json), BlobPropertyBag(type = "application/json"))
        val responseInit = ResponseInit(status = if (video != null) 200 else 404)

        Promise.resolve(Response(blob, responseInit))
    }
}