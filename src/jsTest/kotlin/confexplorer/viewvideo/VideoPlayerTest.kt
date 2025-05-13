package confexplorer.viewvideo

import api.createPromiseResponseFetchFunction
import com.zegreatrob.wrapper.testinglibrary.react.RoleOptions
import com.zegreatrob.wrapper.testinglibrary.react.TestingLibraryReact.screen
import com.zegreatrob.wrapper.testinglibrary.react.TestingLibraryReact.within
import com.zegreatrob.wrapper.testinglibrary.userevent.UserEvent
import confexplorer.App
import confexplorer.ConfExplorerTestBase
import confexplorer.ElementHandle.LOADING
import confexplorer.ElementHandle.REACT_PLAYER
import confexplorer.ElementHandle.VIDEO_DETAIL_TITLE
import io.kotest.assertions.withClue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import kotest.suspendSetup
import confexplorer.getCodeElementHandle
import test.html.waitUntilElementExists
import test.html.waitUntilElementDoesNotExist

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
                    container.waitUntilElementDoesNotExist(getCodeElementHandle(LOADING))
                    val htmlElementBefore = screen.getByRole("option", RoleOptions("Learning kodein"))
                    user.click(htmlElementBefore)
                    val reactPlayer = screen.findByLabelText(REACT_PLAYER)
                    reactPlayer.waitUntilElementExists("iframe")
                    reactPlayer.querySelector("iframe")?.getAttribute("src")
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
                container.waitUntilElementDoesNotExist(getCodeElementHandle(LOADING))
                val htmlElementBefore = screen.getByRole("option", RoleOptions("Learning react"))
                user.click(htmlElementBefore)
                screen.getByLabelText(VIDEO_DETAIL_TITLE).textContent
            }.verify { reactPlayerTitle : String ->
                withClue("react player url") {
                    reactPlayerTitle shouldBe "Learning react"
                }
            }()
        }
    }
}