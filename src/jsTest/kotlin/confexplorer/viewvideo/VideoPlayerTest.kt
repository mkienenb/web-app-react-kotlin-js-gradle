package confexplorer.viewvideo

import api.createPromiseResponseFetchFunction
import com.zegreatrob.wrapper.testinglibrary.react.RoleOptions
import com.zegreatrob.wrapper.testinglibrary.react.TestingLibraryReact.screen
import com.zegreatrob.wrapper.testinglibrary.userevent.UserEvent
import confexplorer.App
import confexplorer.ConfExplorerTestBase
import io.kotest.assertions.withClue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import kotest.suspendSetup

class VideoPlayerTest : ConfExplorerTestBase() {
    init {
        if (runningInBrowser()) {
            // This test requires both window.fetch support as well as real iframe support, neither of which are available under node js
            should("set url 'www.youtube.com/learning-kodein' in react player when 'learning kodein' video is queued") {
                suspendSetup( object {
                    var learningReactVideo = Video(1, "Learning kodein", videoUrl = "https://www.youtube.com/watch?v=kodein56215")
                    val user = UserEvent.setup()
                }).withDI {
                    it.videoServiceFetchFunction = createPromiseResponseFetchFunction(listOf(learningReactVideo))
                }.exercise {
                    renderReactComponent(App)
                    waitUntilElementGone(container,"[data-code-element-handle='loading']")
                    val htmlElementBefore = screen.getByRole("option", RoleOptions("Learning kodein"))
                    user.click(htmlElementBefore)
                    val iframeSelector = "[data-code-element-handle='react-player'] iframe"
                    waitUntilElementExists(container, iframeSelector)
                    container.querySelector(iframeSelector)?.getAttribute("src")
                }.verify { reactPlayerUrl : String ->
                    withClue("react player url") {
                        reactPlayerUrl shouldStartWith "https://www.youtube.com/embed/kodein56215"
                    }
                }()
            }
        }

        should("show 'Learning react' as video detail title when 'Learning react' video is selected") {
            suspendSetup( object {
                var learningReactVideo = Video(1, "Learning react", videoUrl = "www.youtube.com/learning-react")
                val user = UserEvent.setup()
            }).withDI {
                it.videoServiceFetchFunction = createPromiseResponseFetchFunction(listOf(learningReactVideo))
            }.exercise {
                renderReactComponent(App)
                waitUntilElementGone(container,"[data-code-element-handle='loading']")
                val htmlElementBefore = screen.getByRole("option", RoleOptions("Learning react"))
                user.click(htmlElementBefore)
                container.querySelector("[data-code-element-handle='video-detail-title']")?.textContent
            }.verify { reactPlayerTitle : String ->
                withClue("react player url") {
                    reactPlayerTitle shouldBe "Learning react"
                }
            }()
        }
    }
}