package confexplorer.viewvideo

import browserOnlyCode
import io.kotest.assertions.withClue
import io.kotest.matchers.shouldNotBe
import kotest.ReactComponentTestBase
import kotlinx.coroutines.test.runTest
import react.Props
import react.create
import react.dom.client.createRoot
import react.dom.test.act
import web.html.HTMLUListElement

class VideoListReactComponentTest : ReactComponentTestBase() {
    init {
        browserOnlyCode {
            should("render video list") {
                ForComponent(VideoListReactComponent, {
                    videos = listOf(Video(1, "Learning kotlin"))
                }) {
                    val unorderedList = container.getElementsByTagName("ul")[0] as? HTMLUListElement
                    withClue("unordered list") {
                        unorderedList shouldNotBe null
                    }
                }
            }
        }
    }
}