package confexplorer.viewvideo

import browserOnlyCode
import io.kotest.assertions.withClue
import io.kotest.matchers.shouldBe
import kotest.ReactComponentTestBase

class VideoPlayerTest : ReactComponentTestBase() {
    init {
        browserOnlyCode {
            xshould("set url 'www.youtube.com/learning-react' in react player when 'learning react' video is queued") {
                TODO()
            }

            xshould("show 'Learning react' as video detail title when 'Learning react' video is selected") {
                TODO()
            }
        }
    }
}