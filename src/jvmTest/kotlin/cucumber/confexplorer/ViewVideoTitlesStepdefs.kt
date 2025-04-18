package cucumber.confexplorer

import cucumber.common.ScenarioContext
import cucumber.common.driver.addReactAppEnvironmentVariable
import cucumber.common.driver.baseUrl
import cucumber.common.driver.startReactApp
import cucumber.common.fakewebservice.fakeWebservice
import io.cucumber.datatable.DataTable
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder

class ViewVideoTitlesStepdefs(var scenarioContext: ScenarioContext) {

    @Given("the following videos are provided by the video service:")
    fun theFollowingVideosAreProvidedByTheVideoService(dataTable: DataTable) {
        with (scenarioContext) {
            val videoList = dataTable.asList()
            val idsToVideosMap: Map<String, String> = videoList.withIndex().associate { (index, name) ->
                index.toString() to """
                {
                  "id": "$index",
                  "videoUrl": "https://www.youtube.com/watch?v=PsaFVLr8t4E",
                  "title": "$name",
                  "speaker": "unknown speaker"
                }
                """.trimIndent()
            }
            val videoServiceUrl = fakeWebservice("videoService", idsToVideosMap).url
            addReactAppEnvironmentVariable("SERVICE_VIDEO_URL", videoServiceUrl)
        }
    }

    @Given("the react app is started")
    fun theReactAppIsStarted() {
        scenarioContext.startReactApp()
    }

    @Given("I have watched the following videos:")
    fun iHaveWatchedTheFollowingVideos(dataTable: DataTable) {
        val videoList = dataTable.asList()
    }

    @When("I go to the conference explorer page")
    fun iGoToTheConferenceExplorerPage() {
        with(scenarioContext) {
            println("driver.navigate().to(${baseUrl()})")
            driver.navigate().to(baseUrl())
        }
    }

    @Then("I should see the following list of unwatched videos:")
    fun iShouldSeeTheFollowingListOfUnwatchedVideos(dataTable: DataTable) {
        scenarioContext.withViewVideoPage {
            val expectedUnwatchedVideoList = dataTable.asList()

            println("==== At time of test ====")
            println("==== BEGIN PAGE SOURCE DUMP ====")
            println(scenarioContext.driver.pageSource)
            println("==== END PAGE SOURCE DUMP ====")

            Thread.sleep(10000)

            println("---- At time of test ----")
            println("---- BEGIN PAGE SOURCE DUMP ----")
            println(scenarioContext.driver.pageSource)
            println("---- END PAGE SOURCE DUMP ----")

            val actualUnwatchedVideoList = unwatchedVideoNameList
            actualUnwatchedVideoList.shouldContainExactlyInAnyOrder(expectedUnwatchedVideoList)

        }
    }
}