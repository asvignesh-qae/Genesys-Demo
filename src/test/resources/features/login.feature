@login
Feature: Login Functionality
  As a user of SauceDemo
  I want to be able to login with valid credentials
  So that I can access the inventory page

  @smoke
  Scenario: Successful login with valid credentials
    Given the user is on the login page
    When the user logs in with username "standard_user" and password "secret_sauce"
    Then the inventory page should be displayed

  @negative @smoke
  Scenario: Login fails with empty credentials
    Given the user is on the login page
    When the user clicks the login button without entering credentials
    Then the error message "Epic sadface: Username is required" should be displayed
