@error_messages_for_mandatory_fields_automation
Feature: SauceDemo Purchase and Validation
  As a user of SauceDemo website
  I want to automate the purchase process and verify error messages
  So that I can ensure the website works correctly

  @testCase2 @regression
  Scenario: Verify error messages for mandatory fields
    Given user opens the url "https://www.saucedemo.com/inventory.html"
    When user clicks on the login button
    Then the error message should be displayed as "Epic sadface: Username is required"
    When user logs in with credentials "standard_user" and "secret_sauce"
    And user scrolls down to the bottom of the page
    Then the footer message should contain "2026" and "Terms of Service"