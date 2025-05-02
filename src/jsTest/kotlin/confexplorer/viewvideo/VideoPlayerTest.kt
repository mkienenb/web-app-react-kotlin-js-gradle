package confexplorer.viewvideo

import api.createPromiseResponseFetchFunction
import io.kotest.assertions.withClue
import io.kotest.matchers.shouldBe
import kotest.suspendSetup
import reactdi.ReactShouldSpecBase

class VideoPlayerTest : ReactShouldSpecBase() {
    init {
        should("set url 'www.youtube.com/learning-react' in react player when 'learning react' video is queued") {
            suspendSetup( object {
                var learningReactVideo = Video(1, "Learning react", videoUrl = "www.youtube.com/learning-react")
            }).exercise {
                renderReactComponent(VideoPlayer) {
                    video = learningReactVideo
                }
                container.querySelector("[data-code-element-handle='react-player-url']")?.textContent
            }.verify { reactPlayerUrl : String ->
                withClue("react player url") {
                    reactPlayerUrl shouldBe "www.youtube.com/learning-react"
                }
            }()
        }

        xshould("show 'Learning react' as video detail title when 'Learning react' video is selected") {
            TODO()
        }
    }
}