package confexplorer.viewvideo

import browserOnlyCode
import io.kotest.assertions.withClue
import io.kotest.matchers.shouldBe
import kotest.ReactComponentTestBase

class VideoListReactComponentTest : ReactComponentTestBase() {
    init {
        browserOnlyCode {
            should("show Learning kotlin video") {
                ForComponent(VideoListReactComponent, {
                    videos = listOf(Video(1, "Learning kotlin"))
                }) {
                    val firstVideoTitle = container.querySelector("ul li")?.textContent
                    withClue("unordered list") {
                        firstVideoTitle shouldBe "Learning kotlin"
                    }
                }
            }
        }
    }
}