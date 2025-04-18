package cucumber.confexplorer

import CONTEXT_PATH
import cucumber.common.ScenarioContext
import cucumber.common.driver.baseUrl
import cucumber.common.fakewebservice.fakeWebservice
import io.cucumber.datatable.DataTable
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder

class ViewVideoTitlesStepdefs(var scenarioContext: ScenarioContext) {

    @Given("the following videos provided by the video service:")
    fun theFollowingVideosProvidedByTheVideoService(dataTable: DataTable) {
        val videoList = dataTable.asList()
        val fakeWebservice = scenarioContext.fakeWebservice("videoWebService")
        val urlToResponseMap = videoList.withIndex().associate { (index, videoName) ->
            val videoIndex = index + 1
            "$CONTEXT_PATH/${videoIndex}" to
            """
            {
              "id": "${videoIndex}",
              "videoUrl": "${fakeWebservice.url}$CONTEXT_PATH/${videoIndex}",
              "title": "$videoName",
              "speaker": "Pav"
            }
            """.trimIndent()
        }
        fakeWebservice.setPathToResponseMappings(urlToResponseMap)
    }

    @Given("I have watched the following videos:")
    fun iHaveWatchedTheFollowingVideos(dataTable: DataTable) {
        val videoList = dataTable.asList()
    }

    @When("I go to the conference explorer page")
    fun iGoToTheConferenceExplorerPage() {
        with(scenarioContext) {
            driver.navigate().to(baseUrl())
        }
    }

    @Then("I should see the following list of unwatched videos:")
    fun iShouldSeeTheFollowingListOfUnwatchedVideos(dataTable: DataTable) {
        scenarioContext.withViewVideoPage {
            val expectedUnwatchedVideoList = dataTable.asList()
            val actualUnwatchedVideoList = unwatchedVideoNameList

            actualUnwatchedVideoList.shouldContainExactlyInAnyOrder(expectedUnwatchedVideoList)
        }
    }
}