Feature: View Video Titles
  Background:
    Given the following videos are provided by the video service:
    | Learning kotlin     |
    | Learning koin       |
    | Learning kotest     |
    | Learning react      |
    And the react app is started
    And I have watched the following videos:
      | Learning koin     |
      | Learning react     |


Scenario: See list of unwatched videos
  When I go to the conference explorer page
  Then I should see the following list of unwatched videos:
    | Learning kotlin |
    | Learning kotest |
