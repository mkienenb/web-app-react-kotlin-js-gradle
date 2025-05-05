package confexplorer

import api.createPromiseResponseFetchFunction
import confexplorer.viewvideo.Video
import io.kotest.assertions.withClue
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import js.array.asList
import kotest.suspendSetup
import kotlinx.coroutines.CompletableDeferred
import org.w3c.dom.Element
import reactdi.ReactShouldSpecBase
import kotlin.coroutines.CoroutineContext

class AppTest : ReactShouldSpecBase () {
    init {
        should("show 'Conference Explorer' on page") {
            suspendSetup(object {

            }).exercise {
                renderReactComponent(App)
                container.querySelector("h1")?.textContent
            }.verify { pageHeader: String ->
                withClue("page header") {
                    pageHeader shouldBe "Conference Explorer"
                }
            }
        }

        should("show unwatched video titles of 'Learning Kotlin' and 'Unlearning Java' on page") {
            suspendSetup(object {
                val videoList = listOf(
                    Video(1, "Learning Kotlin"),
                    Video(2, "Unlearning Java")
                )
            }).withDI {
                it.videoServiceFetchFunction = createPromiseResponseFetchFunction(videoList)
            }.exercise {
                renderReactComponent(App)
                waitUntilElementGone(container,"[data-code-element-handle='loading']")
                    container.querySelectorAll("[data-code-element-handle='unwatched-video-title']")
                        .asList()
                        .map { it.textContent }
            }.verify {  actualUnwatchedVideoTitlesList: List<String> ->
                withClue("unwatched video titles") {
                    actualUnwatchedVideoTitlesList shouldContainExactly listOf("Learning Kotlin", "Unlearning Java")
                }
            }
        }

        should("show 'Loading...' when video list is still loading") {
            suspendSetup(object {
                val videoList: List<Video> = listOf(
                    Video(1, "Learning Kotlin"),
                    Video(2, "Unlearning Java")
                )
                val fetchGate = CompletableDeferred<Unit>()
            }).withDI {
                it.videoServiceFetchFunction = { url ->
                    fetchGate.await()
                    createPromiseResponseFetchFunction(videoList)(url)
                }
            }.exercise {
                renderReactComponent(App)
                container.querySelector("[data-code-element-handle='loading']")
            }.verify { actualVideoListsElement: Element? ->
                withClue("loading element") {
                    actualVideoListsElement?.textContent shouldBe "Loading..."
                }
                fetchGate.complete(Unit)  // then run the suspendable test code
            }
        }

        should("not show video player when there is no video selected") {
            suspendSetup(object {
                val videoList = listOf(
                    Video(1, "Learning Kotlin"),
                    Video(2, "Unlearning Java")
                )
            }).withDI {
                it.videoServiceFetchFunction = createPromiseResponseFetchFunction(videoList)
            }.exercise {
                renderReactComponent(App)
                waitUntilElementGone(container, "[data-code-element-handle='loading']")
                container.querySelector("[data-code-element-handle='video-detail-title']")
            }.verify {actualVideoTitleElement: Element? ->
                withClue("react player video title") {
                    actualVideoTitleElement.shouldBeNull()
                }
            }
        }
    }
}
