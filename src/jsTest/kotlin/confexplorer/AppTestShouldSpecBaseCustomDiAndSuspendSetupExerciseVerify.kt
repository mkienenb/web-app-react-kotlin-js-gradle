package confexplorer

import api.createPromiseResponseFetchFunction
import confexplorer.viewvideo.Video
import io.kotest.assertions.withClue
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import js.array.asList
import kotest.suspendSetup
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.StandardTestDispatcher
import org.w3c.fetch.Response
import reactdi.ReactShouldSpecBase
import web.dom.Element
import kotlin.js.Promise

class AppTestShouldSpecBaseCustomDiAndSuspendSetupExerciseVerify : ReactShouldSpecBase() {

    init {
        should("have page header of 'Conference Explorer'") {
            suspendSetup(object {}).exercise {
                renderReactComponent(App)
                container.querySelector("h1")?.textContent
            }.verify { pageHeader: String? ->
                pageHeader shouldBe "Conference Explorer"
            }()
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
                waitUntilElementGone(container, "[data-code-element-handle='loading']")
                container
                    .querySelectorAll("[data-code-element-handle='unwatched-video-title']")
                    .asList()
                    .map { it.textContent }
            }.verify { actualTitles: List<String?> ->
                withClue("unwatched video titles") {
                    actualTitles shouldContainExactly listOf("Learning Kotlin", "Unlearning Java")
                }
            }()
        }

        should("show 'Loading...' when video list is still loading") {
            suspendSetup(object {
                val videoList = listOf(
                    Video(1, "Learning Kotlin"),
                    Video(2, "Unlearning Java")
                )
                val fetchGate = CompletableDeferred<Unit>()
                val controlledFetchFunction: suspend (String) -> Promise<Response> = { url ->
                    fetchGate.await()
                    createPromiseResponseFetchFunction(videoList)(url)
                }
            }).withDI {
                with(it) {
                    videoServiceFetchFunction = controlledFetchFunction
                    scope = CoroutineScope(Job() + StandardTestDispatcher())
                }
            }.exercise {
                renderReactComponent(App)
                container.querySelector("[data-code-element-handle='videoLists']")
            }.verify { actualElement: Element? ->
                withClue("video lists element") {
                    actualElement?.textContent shouldBe "Loading..."
                }
                fetchGate.complete(Unit)
            }()
        }
    }
}
