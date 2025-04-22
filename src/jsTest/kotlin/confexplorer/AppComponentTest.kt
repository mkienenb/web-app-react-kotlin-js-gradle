package confexplorer

import api.VideoService
import browserOnlyCode
import confexplorer.viewvideo.Video
import io.kotest.assertions.withClue
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import js.array.asList
import kotest.ReactComponentTestBase

class AppComponentTest: ReactComponentTestBase() {
    init {
        browserOnlyCode {
            should("show 'Conference Explorer' on page") {
                ForComponent(AppComponent) {
                    val pageHeader = container.querySelector("h1")?.textContent
                    withClue("page header") {
                        pageHeader shouldBe "Conference Explorer"
                    }
                }
            }

            should("show unwatched video titles list on page") {
                VideoService.setVideos(listOf(Video(1, "Learning Kotlin"), Video(2, "Unlearning Java")))
                ForComponent(AppComponent) {
                    val actualUnwatchedVideoTitlesList = container.querySelectorAll("[data-code-element-handle='unwatchedVideo']")
                        .asList()
                        .map { it.textContent }
                    withClue("unwatched video titles") {
                        actualUnwatchedVideoTitlesList shouldContainExactly listOf("Learning Kotlin", "Unlearning Java")
                    }
                }
            }
        }
    }
}