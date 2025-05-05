package confexplorer

import api.createPromiseResponseFetchFunction
import confexplorer.viewvideo.Video
import io.kotest.assertions.withClue
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import js.array.asList
import kotest.suspendSetup
import reactdi.ReactShouldSpecBase

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

        /*should("show 'Loading...' when video list is still loading") {
            val videoList = listOf(
                Video(1, "Learning Kotlin"),
                Video(2, "Unlearning Java")
            )
            val fetchGate = CompletableDeferred<Unit>()
            actRenderComponentWithDI(createTestDi({ url ->
                fetchGate.await()
                createPromiseResponseFetchFunction(videoList)(url)
            }), App)
            val actualVideoListsElement = container.querySelector("[data-code-element-handle='videoLists']")
            withClue("video lists element") {
                actualVideoListsElement?.textContent shouldBe "Loading..."
            }
            fetchGate.complete(Unit)  // then run the suspendable test code
        }

        should("not show video player when there is no video selected") {
            val videoList = listOf(
                Video(1, "Learning Kotlin"),
                Video(2, "Unlearning Java")
            )
            actRenderComponentWithDI(createTestDi({ url ->
                createPromiseResponseFetchFunction(videoList)(url)
            }), App)

            waitUntilElementGone("[data-code-element-handle='loading']")

            // verify
            val actualVideoTitleElement = container.querySelector("[data-code-element-handle='video-detail-title']")
            withClue("react player video title") {
                actualVideoTitleElement.shouldBeNull()
            }
        }*/
    }
}
