Feature: Sample feature

  Scenario: Home page
    Given the React app is running
    When I visit the homepage
    Then I should see the title "Hello, Kotlin/JS!"
