package confexplorer.viewvideo

import com.zegreatrob.wrapper.testinglibrary.react.TestingLibraryReact.screen
import com.zegreatrob.wrapper.testinglibrary.userevent.UserEvent

import io.kotest.assertions.withClue
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import js.array.asList
import kotest.suspendSetup
import org.w3c.dom.HTMLElement
import reactdi.ReactShouldSpecBase

class VideoListTest : ReactShouldSpecBase() {
    init {
        should("show Learning kotlin video") {
            suspendSetup(object {
                val videoList = listOf(Video(1, "Learning kotlin"))
            }).exercise {
                renderReactComponent(VideoList) {
                    videos = videoList
                }
                container.querySelector("[data-code-element-handle='unwatched-video-title']")?.textContent
            }.verify {firstVideoTitle: String? ->
                withClue("unordered list") {
                    firstVideoTitle shouldBe "Learning kotlin"
                }
            }()
        }

        should("show Unlearning Java video") {
            suspendSetup(object {
                val videoList = listOf(Video(1, "Unlearning Java"))
            }).exercise {
                renderReactComponent(VideoList) {
                    videos = videoList
                }
                container.querySelector("[data-code-element-handle='unwatched-video-title']")?.textContent
            }.verify {firstVideoTitle: String? ->
                withClue("unordered list") {
                    firstVideoTitle shouldBe "Unlearning Java"
                }
            }()
        }

        should("show Learning kotlin and Unlearning Java videos") {
            suspendSetup(object {
                val videoList = listOf(Video(1, "Learning Kotlin"), Video(2, "Unlearning Java"))
            }).exercise {
                renderReactComponent(VideoList) {
                    videos = videoList
                }
                container.querySelectorAll("[data-code-element-handle='unwatched-video-title']").asList().map { it.textContent }
            }.verify {actualVideoTitles: List<String>? ->
                withClue("unordered list") {
                    actualVideoTitles shouldContainExactly listOf("Learning Kotlin", "Unlearning Java")
                }
            }()
        }

        should("show video selection symbol when 'Learning react' video is selected") {
            suspendSetup(object {
                val videoList = listOf(Video(1, "Learning Kotlin"), Video(2, "Learning react"))
                val user = UserEvent.setup()
            }).exercise {
                renderReactComponent(VideoList) {
                    videos = videoList
                }
                val htmlElementBefore = screen.getByText("Learning react")
                user.click(htmlElementBefore)
                val title = screen.getByText("Learning react")
                title.closest("[data-code-element-handle='video-selection-indicator']")
            }.verify { indicatorNode:  HTMLElement? ->
                withClue("video-selection-indicator element") {
                    indicatorNode.shouldNotBeNull()
                }
            }()
        }
    }
}