@purchase_process_automation
Feature: SauceDemo Purchase and Validation
  As a user of SauceDemo website
  I want to automate the purchase process and verify error messages
  So that I can ensure the website works correctly

  @testCase1 @regression
  Scenario: Automate Purchase Process
    Given user opens the url "https://www.saucedemo.com/inventory.html"
    And user parses credentials from credential.json file
    When user types the username and password and clicks on login button
    And user adds "Sauce Labs Backpack" to the shopping cart
    And user adds "Sauce Labs Fleece Jacket" to the shopping cart
    Then the number on the cart symbol should be "2"
    When user goes through the checkout process
    Then "Thank you for your order!" message should be displayed
