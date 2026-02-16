@rest_api_testing_automation
Feature: REST API Testing
  As a tester
  I want to send REST API requests and validate responses
  So that I can ensure the API endpoints work correctly

  @testCase5 @regression
  Scenario: GET users and validate response data
    Given the API base URL is configured
    When user sends a GET request to "/users" endpoint
    Then the response status code should be 200
    And the response should contain a non-empty list of users
    And user logs the name and email of each user
    And the first user email should contain "@"
