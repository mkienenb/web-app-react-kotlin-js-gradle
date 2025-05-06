package confexplorer.viewvideo

import api.createPromiseResponseFetchFunction
import com.zegreatrob.wrapper.testinglibrary.react.RoleOptions
import com.zegreatrob.wrapper.testinglibrary.react.TestingLibraryReact.screen
import com.zegreatrob.wrapper.testinglibrary.userevent.UserEvent
import confexplorer.App
import io.kotest.assertions.withClue
import io.kotest.matchers.shouldBe
import kotest.suspendSetup
import reactdi.ReactShouldSpecBase

class VideoPlayerTest : ReactShouldSpecBase() {
    init {
        xshould("set url 'www.youtube.com/learning-react' in react player when 'learning react' video is queued") {
            suspendSetup( object {
                var learningReactVideo = Video(1, "Learning react", videoUrl = "www.youtube.com/learning-react")
                val user = UserEvent.setup()
            }).withDI {
                it.videoServiceFetchFunction = createPromiseResponseFetchFunction(listOf(learningReactVideo))
            }.exercise {
                renderReactComponent(App)
                val htmlElementBefore = screen.getByRole("option", RoleOptions("Learning react"))
                user.click(htmlElementBefore)
                container.querySelector("[data-code-element-handle='react-player-url']")?.textContent
            }.verify { reactPlayerUrl : String ->
                withClue("react player url") {
                    reactPlayerUrl shouldBe "www.youtube.com/learning-react"
                }
            }()
        }

        should("show 'Learning react' as video detail title when 'Learning react' video is selected") {
            suspendSetup( object {
                var learningReactVideo = Video(1, "Learning react", videoUrl = "www.youtube.com/learning-react")
                val user = UserEvent.setup()
            }).withDI {
                it.videoServiceFetchFunction = createPromiseResponseFetchFunction(listOf(learningReactVideo))
            }.exercise {
                renderReactComponent(App)
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