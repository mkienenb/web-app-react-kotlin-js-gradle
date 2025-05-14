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
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith


class ViewVideoTitlesStepdefs(var scenarioContext: ScenarioContext) {

    @Given("the following videos provided by the video service:")
    fun theFollowingVideosProvidedByTheVideoService(dataTable: DataTable) {
        with(scenarioContext) {
            val rows = dataTable.asMaps()
            val fakeWebservice = fakeWebservice("videoWebService")
            addReactAppEnvironmentVariable("URL", fakeWebservice.url)
            val urlToResponseMap = rows.withIndex().associate { (index, row) ->
                val videoIndex = index + 1
                val title = row.get("Title")
                val url = row.get("URL")
                "$CONTEXT_PATH${videoIndex}" to
                        """
            {
              "id": "${videoIndex}",
              "videoUrl": "$url"
              "title": "$title",
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

            withClue("unwatched video list") {
                unwatchedVideoNameList.shouldContainExactlyInAnyOrder(expectedUnwatchedVideoList)
            }
        }
    }

    @Then("I should see the following list of watched videos:")
    fun iShouldSeeTheFollowingListOfWatchedVideos(dataTable: DataTable) {
        scenarioContext.withViewVideoPage {
            val expectedWatchedVideoList = dataTable.asList()
            waitUntilDoneLoading()

            withClue("watched video list") {
                watchedVideoNameList.shouldContainExactlyInAnyOrder(expectedWatchedVideoList)
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
                waitForVideoPlayerToBeLoaded()
                videoPlayerUrl shouldStartWith videoUrl
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

    @Then("I should only see the selection indicator next to the {string} video")
    fun iShouldOnlySeeTheSelectionIndicatorNextToTheVideo (videoName: String) {
        scenarioContext.withViewVideoPage {
            selectedVideoTitle shouldBe videoName
        }
    }

    @Then("I should see no video selected")
    fun iShouldSeeNoVideoSelected () {
        scenarioContext.withViewVideoPage {
            selectedVideoTitle.shouldBeNull()
        }
    }

    @Then("I mark the selected video as watched")
    fun iMarkTheSelectedVideoAsWatched() {
        scenarioContext.withViewVideoPage {
            markSelectedVideoAsWatched()
        }
    }
}