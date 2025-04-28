Feature: View Video Titles
  Background:
    Given the following videos provided by the video service:
    | Learning kotlin     |
    | Learning koin       |
    | Learning kotest     |
    | Learning react      |

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
  Then I should see that the video "Learning kotlin" is in the video player
  And I should see the video title "Learning kotlin" in the video details
  And I should see the selection indicator next to the "Learning kotlin" video