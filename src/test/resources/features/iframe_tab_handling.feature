@iframe_and_tab_handling_automation
Feature: iFrame and Tab Handling
  As a user of the Guru99 website
  I want to interact with iframes and handle multiple browser tabs
  So that I can verify cross-frame and cross-tab navigation works correctly

  @testCase4 @regression
  Scenario: Handle iFrame click and new tab navigation
    Given user navigates to the url "http://demo.guru99.com/test/guru99home"
    When user switches to the iframe near the bottom of the page
    And user clicks on the image inside the iframe
    Then a new tab should open with title "Selenium Live Project for Practice"
    When user closes the new tab and switches back to the main window
    And user hovers on the "Testing" menu item from the top menu
    And user clicks on the "Selenium" link from the dropdown
#    Then the wide red Join Now button should be displayed near the bottom of the page
