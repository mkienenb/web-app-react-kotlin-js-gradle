package confexplorer

import api.createPromiseResponseFetchFunction
import com.zegreatrob.testmints.async.ScopeMint
import com.zegreatrob.testmints.async.asyncSetup
import confexplorer.viewvideo.Video
import io.kotest.assertions.withClue
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import js.array.asList
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import org.kodein.di.DI
import org.w3c.fetch.Response
import react.FC
import react.Props
import react.act
import react.create
import react.dom.client.createRoot
import reactdi.KodeinProvider
import reactdi.createTestDi
import web.dom.document
import web.html.HTMLDivElement
import kotlin.js.Promise
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class AppTestStandardTestShouldSpecMints {
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

    @BeforeTest
    fun beforeTest() {
        setUpReactContainer()
    }

    @AfterTest
    fun afterTest() {
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

        @Test
        fun shouldHavePageHeaderOfConferenceExplorer() = asyncSetup(object : ScopeMint() {
                val testDI = createTestDi(scope = exerciseScope)
        }) exercise {
                renderReactComponent(App, testDI)
                container.querySelector("h1")?.textContent
        } verify { pageHeader ->
            withClue("page header") {
                    pageHeader shouldBe "Conference Explorer"
                }

        }

        @Test
        fun showUnwatchedVideoTitlesOfLearningKotlinAndUnlearningJavaOnPage() = asyncSetup(object : ScopeMint() {
                val videoList = listOf(
                    Video(1, "Learning Kotlin"),
                    Video(2, "Unlearning Java")
                )
                val videoServiceFetchFunction = createPromiseResponseFetchFunction(videoList)
                val testDI = createTestDi(scope = testScope, videoServiceFetchFunction = videoServiceFetchFunction)
        }) exercise {
                renderReactComponent(App, testDI)
                waitUntilElementGone("[data-code-element-handle='loading']")
                container
                    .querySelectorAll("[data-code-element-handle='unwatched-video-title']")
                    .asList()
                    .map { it.textContent }
        } verify { actualUnwatchedVideoTitlesList ->
                withClue("unwatched video titles") {
                    actualUnwatchedVideoTitlesList shouldContainExactly listOf(
                        "Learning Kotlin",
                        "Unlearning Java"
                    )
                }
        }


        @Test
        fun showLoadingWhenVideoListIsStillLoading() = asyncSetup(object : ScopeMint() {
                val videoList = listOf(
                    Video(1, "Learning Kotlin")
                )
                val fetchGate = CompletableDeferred<Unit>()
                val videoServiceFetchFunction: suspend (String) -> Promise<Response> = { url ->
                    fetchGate.await()
                    createPromiseResponseFetchFunction(videoList)(url)
                }
                val testDI =
                    createTestDi(scope = CoroutineScope(Job() + StandardTestDispatcher()), videoServiceFetchFunction = videoServiceFetchFunction)
        }) exercise {
                renderReactComponent(App, testDI)
                container.querySelector("[data-code-element-handle='videoLists']")
        } verify { actualVideoListsElement ->
                withClue("video lists element") {
                    actualVideoListsElement?.textContent shouldBe "Loading..."
                }
                fetchGate.complete(Unit)  // then run the suspendable test code
        }
    }