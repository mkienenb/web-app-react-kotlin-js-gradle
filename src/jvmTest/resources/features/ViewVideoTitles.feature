Feature: View Video Titles
  Background:
    Given the following videos provided by the video service:
    | Title               | URL                               |
    | Learning kotlin     | www.youtube.com/learning-kotlin   |
    | Learning koin       | www.youtube.com/learning-koin     |
    | Learning kotest     | www.youtube.com/learning-kotest   |
    | Learning react      | www.youtube.com/learning-react    |

Scenario: See list of unwatched videos
  When I go to the conference explorer page
  Then I should see the following list of unwatched videos:
    | Learning kotlin     |
    | Learning koin       |
    | Learning kotest     |
    | Learning react      |

Scenario: Select unwatched video and show details
  When I go to the conference explorer page
  And I select the video "Learning kotlin" from the unwatched list
  Then I should see that the video player has queued "www.youtube.com/learning-kotlin" url
  And I should see the video title "Learning kotlin" in the video details
  And I should only see the selection indicator next to the "Learning kotlin" video