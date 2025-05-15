package confexplorer

import api.createPromiseResponseFetchFunction
import confexplorer.ElementHandle.LOADING
import confexplorer.ElementHandle.UNWATCHED_VIDEO_TITLE
import confexplorer.ElementHandle.VIDEO_DETAIL_TITLE
import confexplorer.viewvideo.Video
import io.kotest.assertions.withClue
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import js.array.asList
import kotest.suspendSetup
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.StandardTestDispatcher
import org.w3c.dom.Element
import test.html.waitUntilElementDoesNotExist

class AppTest : ConfExplorerTestBase () {
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
                container.waitUntilElementDoesNotExist(getCodeElementHandle(LOADING))
                    container.querySelectorAll(getCodeElementHandle(UNWATCHED_VIDEO_TITLE))
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
                it.scope = CoroutineScope(Job() + StandardTestDispatcher())
            }.exercise {
                renderReactComponent(App)
                container.querySelector(getCodeElementHandle(LOADING))
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
                container.waitUntilElementDoesNotExist(getCodeElementHandle(LOADING))
                container.querySelector(getCodeElementHandle(VIDEO_DETAIL_TITLE))
            }.verify {actualVideoTitleElement: Element? ->
                withClue("react player video title") {
                    actualVideoTitleElement.shouldBeNull()
                }
            }
        }
    }
}
