package confexplorer.viewvideo

import browserOnlyCode
import io.kotest.assertions.withClue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotest.ReactComponentTestBase
import web.html.HTMLLIElement
import web.html.HTMLUListElement

class VideoListReactComponentTest : ReactComponentTestBase() {
    init {
        browserOnlyCode {
            should("show Learning kotlin video") {
                ForComponent(VideoListReactComponent, {
                    videos = listOf(Video(1, "Learning kotlin"))
                }) {
                    val unorderedListElement = container.getElementsByTagName("ul")[0] as? HTMLUListElement
                    val firstVideoListItem = unorderedListElement?.getElementsByTagName("li")?.get(0) as? HTMLLIElement
                    withClue("unordered list") {
                        firstVideoListItem?.innerText shouldBe "Learning kotlin"
                    }
                }
            }
        }
    }
}