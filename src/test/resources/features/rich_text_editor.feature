@rich_text_editor_automation
Feature: Rich Text Editor Validation
  As a user of the online HTML editor
  I want to type formatted text in the rich text editor
  So that I can verify bold and underline formatting works correctly

  @testCase3 @regression
  Scenario: Type and validate formatted text in rich text editor
    Given user opens the url "https://onlinehtmleditor.dev"
    And user waits for the editor to load
    When user types "Automation" in bold format in the editor
    And user types " " in normal format in the editor
    And user types "Test" in underline format in the editor
    And user types " Example" in normal format in the editor
    Then the text "Automation Test Example" should be visible in the rich text editor
    And "Automation" text should be in bold format
    And "Test" text should be in underline format
    And "Example" text should have no formatting
