package confexplorer

import api.createPromiseResponseFetchFunction
import confexplorer.viewvideo.Video
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import js.array.asList
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
import org.kodein.di.DI
import react.FC
import react.Props
import react.act
import react.create
import react.dom.client.createRoot
import reactdi.KodeinProvider
import reactdi.createTestDi
import web.dom.document
import web.html.HTMLDivElement
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class AppTest : ShouldSpec({
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

    suspend fun <P : Props> actRenderComponentWithDI(
        di: DI,
        component: FC<P>,
        propsBuilder: P.() -> Unit = {}
    ) {
        act {
            val root = createRoot(container)

            root.render(
                KodeinProvider.create {
                    this.di = di
                    +component.create(propsBuilder)
                }
            )
        }
    }


    beforeTest {
        setUpReactContainer()
    }

    afterTest {
        container.remove()
    }

    should("show 'Conference Explorer' on page") {
        actRenderComponentWithDI(createTestDi(), App)
        val pageHeader = container.querySelector("h1")?.textContent
        withClue("page header") {
            pageHeader shouldBe "Conference Explorer"
        }
    }

    should("show unwatched video titles of 'Learning Kotlin' and 'Unlearning Java' on page") {
        val videoList = listOf(
            Video(1, "Learning Kotlin"),
            Video(2, "Unlearning Java")
        )
        actRenderComponentWithDI(createTestDi(createPromiseResponseFetchFunction(videoList)), App)
        waitUntilElementGone("[data-code-element-handle='loading']")
        val actualUnwatchedVideoTitlesList = container.querySelectorAll("[data-code-element-handle='unwatched-video-title']")
            .asList()
            .map { it.textContent }
        withClue("unwatched video titles") {
            actualUnwatchedVideoTitlesList shouldContainExactly listOf("Learning Kotlin", "Unlearning Java")
        }
    }

    should("show 'Loading...' when video list is still loading") {
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
})
