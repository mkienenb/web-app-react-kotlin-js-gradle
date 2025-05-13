Feature: View Video Titles
  Background:
    Given the following videos provided by the video service:
    | Title               | URL                                         |
    | Learning kotlin     | https://www.youtube.com/watch?v=kotlin78901 |
    | Learning koin       | https://www.youtube.com/watch?v=koin1325648 |
    | Learning kotest     | https://www.youtube.com/watch?v=kotest32658 |
    | Learning react      | https://www.youtube.com/watch?v=react456215 |

Scenario: See list of unwatched videos
  When I go to the conference explorer page
  Then I should see the following list of unwatched videos:
    | Learning kotlin     |
    | Learning koin       |
    | Learning kotest     |
    | Learning react      |
  And I should see no video selected

Scenario: Select unwatched video and show details
  When I go to the conference explorer page
  And I select the video "Learning kotlin" from the unwatched list
  Then I should see that the video player has queued "https://www.youtube.com/embed/kotlin78901" url
  And I should see the video title "Learning kotlin" in the video details
  And I should only see the selection indicator next to the "Learning kotlin" video

Scenario: Select unwatched video, click mark as watched button, and move video to watched video list
  When I go to the conference explorer page
  And I select the video "Learning kotlin" from the unwatched list
  And I mark the selected video as watched
  Then I should see the following list of unwatched videos:
    | Learning koin   |
    | Learning kotest |
    | Learning react  |
  And I should see the following list of watched videos:
    | Learning kotlin |
  And I should only see the selection indicator next to the "Learning kotlin" video
