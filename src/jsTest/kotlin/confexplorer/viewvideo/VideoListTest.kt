package confexplorer.viewvideo

import io.kotest.assertions.withClue
import io.kotest.matchers.shouldBe
import kotest.suspendSetup
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
//            should("show Unlearning Java video") {
//                ForComponent(VideoList, {
//                    videos = listOf(Video(1, "Unlearning Java"))
//                }) {
//                    val firstVideoTitle = container.querySelector("ul li")?.textContent
//                    withClue("unordered list") {
//                        firstVideoTitle shouldBe "Unlearning Java"
//                    }
//                }
//            }
//            should("show Learning kotlin and Unlearning Java videos") {
//                ForComponent(VideoList, {
//                    videos = listOf(Video(1, "Learning Kotlin"), Video(2, "Unlearning Java"))
//                }) {
//                    val actualVideoTitles = container.querySelectorAll("ul li").asList().map { it.textContent }
//                    withClue("unordered list") {
//                        actualVideoTitles shouldContainExactly listOf("Learning Kotlin", "Unlearning Java")
//                    }
//                }
//            }
//
//            xshould("show video selection symbol when 'Learning react' video is selected") {
//                ForComponent(VideoList, {
//                    videos = listOf(Video(1, "Learning Kotlin"), Video(2, "Learning React"))
//                }) {
//                    TODO()
//                }
//            }
    }
}