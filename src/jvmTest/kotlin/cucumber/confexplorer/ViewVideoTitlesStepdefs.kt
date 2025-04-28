package cucumber.confexplorer

import api.VideoServiceLocator.CONTEXT_PATH
import cucumber.common.ScenarioContext
import cucumber.common.driver.addReactAppEnvironmentVariable
import cucumber.common.driver.baseUrl
import cucumber.common.driver.startReactApp
import cucumber.common.fakewebservice.fakeWebservice
import io.cucumber.datatable.DataTable
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.kotest.assertions.withClue
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe


class ViewVideoTitlesStepdefs(var scenarioContext: ScenarioContext) {

    @Given("the following videos provided by the video service:")
    fun theFollowingVideosProvidedByTheVideoService(dataTable: DataTable) {
        with(scenarioContext) {
            val videoList = dataTable.asList()
            val fakeWebservice = fakeWebservice("videoWebService")
            addReactAppEnvironmentVariable("URL", fakeWebservice.url)
            val urlToResponseMap = videoList.withIndex().associate { (index, videoName) ->
                val videoIndex = index + 1
                "$CONTEXT_PATH${videoIndex}" to
                        """
            {
              "id": "${videoIndex}",
              "videoUrl": "https://www.youtube.com/"
              "title": "$videoName",
              "speaker": "Pav"
            }
            """.trimIndent()
            }
            fakeWebservice.setPathToResponseMappings(urlToResponseMap)
        }
    }

    @When("I go to the conference explorer page")
    fun iGoToTheConferenceExplorerPage() {
        with(scenarioContext) {
            startReactApp()
            driver.navigate().to(baseUrl())
        }
    }

    @Then("I should see the following list of unwatched videos:")
    fun iShouldSeeTheFollowingListOfUnwatchedVideos(dataTable: DataTable) {
        scenarioContext.withViewVideoPage {
            val expectedUnwatchedVideoList = dataTable.asList()
            waitUntilDoneLoading()
            val actualUnwatchedVideoList = unwatchedVideoNameList

            actualUnwatchedVideoList.shouldContainExactlyInAnyOrder(expectedUnwatchedVideoList)
            withClue("unwatched video list") {
                actualUnwatchedVideoList.shouldContainExactlyInAnyOrder(expectedUnwatchedVideoList)
            }
        }
    }

    @When("I select the video {string} from the unwatched list")
    fun iSelectTheVideoFromTheUnwatchedList (videoName: String) {
        scenarioContext.withViewVideoPage {
            waitUntilDoneLoading()
            selectVideo(videoName)
        }
    }

    @Then("I should see that the video player has queued {string} url")
    fun iShouldSeeThatTheVideoPlayerHasQueuedUrl (videoUrl: String) {
        scenarioContext.withViewVideoPage {
            withClue("videoPlayerUrl"){
                videoPlayerUrl shouldBe videoUrl
            }
        }
    }

    @Then("I should see the video title {string} in the video details")
    fun iShouldSeeTheVideoTitleInTheVideoDetails (videoName: String) {
        scenarioContext.withViewVideoPage {
            withClue("video detail title") {
                videoDetailTitle shouldBe videoName
            }
        }
    }

    @Then("I should see the selection indicator next to the {string} video")
    fun iShouldSeeTheSelectionIndicatorNextToTheVideo (videoName: String) {
        scenarioContext.withViewVideoPage {
            selectedVideoTitle shouldBe videoName
        }
    }
}