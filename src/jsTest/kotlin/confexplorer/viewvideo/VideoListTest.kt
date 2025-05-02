package confexplorer.viewvideo

import com.zegreatrob.wrapper.testinglibrary.react.RoleOptions
import com.zegreatrob.wrapper.testinglibrary.react.TestingLibraryReact.screen
import com.zegreatrob.wrapper.testinglibrary.userevent.UserEvent

import io.kotest.assertions.withClue
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import js.array.asList
import kotest.suspendSetup
import org.w3c.dom.HTMLElement
import react.FC
import react.Props
import react.useState
import reactdi.ReactShouldSpecBase

class VideoListTest : ReactShouldSpecBase() {
    init {
        should("show Learning kotlin video") {
            suspendSetup(object {
                val videoList = listOf(Video(1, "Learning kotlin"))
            }).exercise {
                renderReactComponent(VideoList) {
                    videos = videoList
                    selectedVideo = null
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
                    selectedVideo = null
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
                    selectedVideo = null
                }
                container.querySelectorAll("[data-code-element-handle='unwatched-video-title']").asList().map { it.textContent }
            }.verify {actualVideoTitles: List<String>? ->
                withClue("unordered list") {
                    actualVideoTitles shouldContainExactly listOf("Learning Kotlin", "Unlearning Java")
                }
            }()
        }

        should("show video selection symbol when 'Learning kotlin' video is selected") {
            suspendSetup(object {
                val videoList = listOf(Video(1, "Learning react"), Video(2, "Learning kotlin"))
                val user = UserEvent.setup()

                val VideoListTestHarness = FC<Props> {
                    val (selectedVideo, setSelectedVideo) = useState<Video?>(null)

                    VideoList {
                        videos = videoList
                        this.selectedVideo = selectedVideo
                        onSelectVideo = { video -> setSelectedVideo(video) }
                    }
                }
            }).exercise {
                renderReactComponent(VideoListTestHarness)
                val htmlElementBefore = screen.getByRole("option", RoleOptions("Learning kotlin"))
                user.click(htmlElementBefore)
                screen.getByRole("option", RoleOptions("Learning kotlin", true))
            }.verify { selectedVideoElement:  HTMLElement? ->
                withClue("selectedVideoElement") {
                    selectedVideoElement.shouldNotBeNull()
                }
            }()
        }

        should("show video selection symbol when 'Learning react' video is selected") {
            suspendSetup(object {
                val videoList = listOf(Video(1, "Learning react"), Video(2, "Learning kotlin"))
                val user = UserEvent.setup()

                val VideoListTestHarness = FC<Props> {
                    val (selectedVideo, setSelectedVideo) = useState<Video?>(null)

                    VideoList {
                        videos = videoList
                        this.selectedVideo = selectedVideo
                        onSelectVideo = { video -> setSelectedVideo(video) }
                    }
                }
            }).exercise {
                renderReactComponent(VideoListTestHarness)
                val htmlElementBefore = screen.getByRole("option", RoleOptions("Learning react"))
                user.click(htmlElementBefore)
                screen.getByRole("option", RoleOptions("Learning react", true))
            }.verify { selectedVideoElement:  HTMLElement? ->
                withClue("selectedVideoElement") {
                    selectedVideoElement.shouldNotBeNull()
                }
            }()
        }

//        should("show video selection symbol when 'Learning react' video is selected") {
//            suspendSetup(object {
//                var currentSelectedVideo : Video? by useState { null }
//                val videoList = listOf(Video(1, "Learning kotlin"), Video(2, "Learning react"))
//                val user = UserEvent.setup()
//            }).exercise {
//                renderReactComponent(VideoList) {
//                    videos = videoList
//                    selectedVideo = currentSelectedVideo
//                    onSelectVideo = { video ->
//                        currentSelectedVideo = video
//                    }
//                }
//                val htmlElementBefore = screen.getByRole("option", RoleOptions("Learning react"))
//                println("Before click")
//                user.click(htmlElementBefore)
//                println("After click")
//                screen.getByRole("option", RoleOptions("Learning react", true))
//            }.verify { selectedVideoElement:  HTMLElement? ->
//                withClue("selectedVideoElement") {
//                    selectedVideoElement.shouldNotBeNull()
//                }
//            }()
//        }
    }
}