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
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import react.act
import react.create
import react.dom.client.createRoot
import reactdi.KodeinProvider

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
                val videoList = listOf(
                    Video(1, "Learning Kotlin"),
                    Video(2, "Unlearning Java")
                )
                val fetchGate = CompletableDeferred<Unit>()
                val testModule = DI.Module("test") {
                    bind<URLToPromiseResponseFunction>() with singleton {
                        { url ->
                            fetchGate.await()
                            createPromiseResponseFetchFunction(videoList)(url)
                        }
                    }
                    bind<UrlProvider>() with singleton {
                        object : UrlProvider {
                            override fun getBaseUrl(): String = "http://localhost"
                        }
                    }

                    bind<VideoService> {singleton { VideoService(instance(), instance()) }}
                }

                Env.testServiceVideoUrl = "http://localhost"


                actRenderComponent(App, {})  // now we use our extracted act-render helper
                val actualVideoListsElement = container.querySelector("[data-code-element-handle='videoLists']")
                withClue("video lists element") {
                    actualVideoListsElement?.textContent shouldBe "Loading..."
                }
                fetchGate.complete(Unit)  // then run the suspendable test code
            }
        }
    }
}
