package confexplorer.viewvideo

import browserOnlyCode
import io.kotest.assertions.withClue
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldHave
import js.array.asList
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
            should("show Unlearning Java video") {
                ForComponent(VideoListReactComponent, {
                    videos = listOf(Video(1, "Unlearning Java"))
                }) {
                    val firstVideoTitle = container.querySelector("ul li")?.textContent
                    withClue("unordered list") {
                        firstVideoTitle shouldBe "Unlearning Java"
                    }
                }
            }
            should("show Learning kotlin and Unlearning Java videos") {
                ForComponent(VideoListReactComponent, {
                    videos = listOf(Video(1, "Learning Kotlin"), Video(2, "Unlearning Java"))
                }) {
                    val actualVideoTitles = container.querySelectorAll("ul li").asList().map { it.textContent }
                    withClue("unordered list") {
                        actualVideoTitles shouldContainExactly listOf("Learning Kotlin", "Unlearning Java")
                    }
                }
            }
        }
    }
}