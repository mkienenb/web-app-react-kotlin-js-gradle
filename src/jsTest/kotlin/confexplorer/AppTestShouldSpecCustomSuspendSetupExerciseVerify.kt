package confexplorer

import api.createPromiseResponseFetchFunction
import confexplorer.viewvideo.Video
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import js.array.asList
import kotest.suspendSetup
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
import org.kodein.di.DI
import org.w3c.fetch.Response
import react.FC
import react.Props
import react.act
import react.create
import react.dom.client.createRoot
import reactdi.KodeinProvider
import reactdi.createTestDi
import web.dom.Element
import web.dom.document
import web.html.HTMLDivElement
import kotlin.js.Promise
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class AppTestShouldSpecCustomSuspendSetupExerciseVerify :
    ShouldSpec({
        lateinit var container: HTMLDivElement

        suspend fun waitUntil(
            timeout: Duration = 5000.milliseconds,
            interval: Duration = 50.milliseconds,
            condition: () -> Boolean
        ) {
            val startTime = js("Date.now()").unsafeCast<Double>()
            while (!condition()) {
                delay(interval)
                val now = js("Date.now()").unsafeCast<Double>()
                if (now - startTime > timeout.inWholeMilliseconds) {
                    throw AssertionError("Timed out after $timeout waiting for condition to be true")
                }
            }
        }

        suspend fun waitUntilElementGone(
            selector: String,
            timeout: Duration = 5000.milliseconds
        ) {
            waitUntil(timeout) {
                container.querySelector(selector) == null
            }
        }

        fun setUpReactContainer() {
            container = document.createElement("div") as HTMLDivElement
            document.body.appendChild(container)
        }

        beforeTest {
            setUpReactContainer()
        }

        afterTest {
            container.remove()
        }

        suspend fun renderReactComponent(componentUnderTest: FC<Props>, testDI: DI) {
            val root = createRoot(container)
            act {
                root.render(KodeinProvider.create {
                    di = testDI
                    +componentUnderTest.create {}
                })
            }
        }

        should("have page header of 'Conference Explorer'") {
            suspendSetup(object {
                val testDI = createTestDi(scope = this@should)
            }).exercise {
                renderReactComponent(App, testDI)
                container.querySelector("h1")?.textContent
            }.verify { pageHeader: String ->
                withClue("page header") {
                    pageHeader shouldBe "Conference Explorer"
                }
            }()
        }

        should("show unwatched video titles of 'Learning Kotlin' and 'Unlearning Java' on page") {
            suspendSetup(object {
                val videoList = listOf(
                    Video(1, "Learning Kotlin"),
                    Video(2, "Unlearning Java")
                )
                val videoServiceFetchFunction = createPromiseResponseFetchFunction(videoList)
                val testDI = createTestDi(scope = this@should, videoServiceFetchFunction = videoServiceFetchFunction)
            }).exercise {
                renderReactComponent(App, testDI)
                waitUntilElementGone("[data-code-element-handle='loading']")
                container
                    .querySelectorAll("[data-code-element-handle='unwatched-video-title']")
                    .asList()
                    .map { it.textContent }
            }.verify { actualUnwatchedVideoTitlesList: List<String?> ->
                withClue("unwatched video titles") {
                    actualUnwatchedVideoTitlesList shouldContainExactly listOf(
                        "Learning Kotlin",
                        "Unlearning Java"
                    )
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
                val videoServiceFetchFunction: suspend (String) -> Promise<Response> = { url ->
                    fetchGate.await()
                    createPromiseResponseFetchFunction(videoList)(url)
                }
                val testDI =
                    createTestDi(scope = this@should, videoServiceFetchFunction = videoServiceFetchFunction)
            }).exercise {
                renderReactComponent(App, testDI)
                container.querySelector("[data-code-element-handle='videoLists']")
            }.verify { actualVideoListsElement: Element? ->
                withClue("video lists element") {
                    actualVideoListsElement?.textContent shouldBe "Loading..."
                }
                fetchGate.complete(Unit)  // then run the suspendable test code
            }()
        }
    })