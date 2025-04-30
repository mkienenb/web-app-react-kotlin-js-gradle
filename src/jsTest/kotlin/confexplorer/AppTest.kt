package confexplorer

import api.*
import browserOnlyCode
import confexplorer.viewvideo.Video
import io.kotest.assertions.withClue
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import js.array.asList
import kotest.ReactComponentTestBase
import kotlinx.coroutines.CompletableDeferred

class AppTest: ReactComponentTestBase() {
    init {
        browserOnlyCode {
            should("show 'Conference Explorer' on page") {
                ForComponent(App) {
                    val pageHeader = container.querySelector("h1")?.textContent
                    withClue("page header") {
                        pageHeader shouldBe "Conference Explorer"
                    }
                }
            }


            should("show unwatched video titles of 'Learning Kotlin' and 'Unlearning Java' on page") {
                Env.testServiceVideoUrl = "http://localhost"
                val videoList = listOf(
                    Video(1, "Learning Kotlin"),
                    Video(2, "Unlearning Java")
                )
                val videoService = createVideoService(videoList)
                ForComponentCallingCoroutines(App) {
                    waitUntilElementGone("[data-code-element-handle='loading']")
                    val actualUnwatchedVideoTitlesList = container.querySelectorAll("[data-code-element-handle='unwatched-video-title']")
                        .asList()
                        .map { it.textContent }
                    withClue("unwatched video titles") {
                        actualUnwatchedVideoTitlesList shouldContainExactly listOf("Learning Kotlin", "Unlearning Java")
                    }
                }
            }

            should("show 'Loading...' when video list is still loading") {
                Env.testServiceVideoUrl = "http://localhost"
                val videoList = listOf(
                    Video(1, "Learning Kotlin"),
                    Video(2, "Unlearning Java")
                )
                val fetchGate = CompletableDeferred<Unit>()
                val controlledFetchFunction : URLToPromiseResponseFunction = { url ->
                    fetchGate.await()
                    createPromiseResponseFetchFunction(videoList)(url)
                }

                //VideoService.setFetchURLToPromiseResponseFunction(controlledFetchFunction)
                ForComponentCallingCoroutines(App) {
                    val actualVideoListsElement = container.querySelector("[data-code-element-handle='videoLists']")
                    withClue("video lists element") {
                        actualVideoListsElement?.textContent shouldBe "Loading..."
                    }
                    fetchGate.complete(Unit)
                }
            }
        }
    }
}
