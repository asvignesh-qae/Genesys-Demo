# Hybrid Test Automation Framework

A Selenium-based Hybrid Test Automation Framework built with Java 17, combining Page Object Model (POM), Data-Driven Testing, and BDD (Cucumber) approaches.

## Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Java | 17 | Programming language |
| Selenium | 4.40.0 | Browser automation |
| TestNG | 7.11.0 | Test runner and assertions |
| Cucumber | 7.34.2 | BDD / Gherkin feature files |
| REST Assured | 5.5.6 | API testing |
| Allure | 2.29.0 | Test reporting (TestNG + Cucumber + REST Assured) |
| Log4j2 | 2.25.3 | Logging |
| Maven | 3.x | Build and dependency management |
| Gson | 2.11.0 | JSON data parsing |
| Lombok | 1.18.40 | Boilerplate reduction for data models |

## Architecture Diagram

```
+==========================================================================================+
|                           HYBRID TEST AUTOMATION FRAMEWORK                               |
+==========================================================================================+
|                                                                                          |
|  +--------------------------------------+   +----------------------------------------+   |
|  |         TestNG Execution             |   |        Cucumber (BDD) Execution        |   |
|  +--------------------------------------+   +----------------------------------------+   |
|  |                                      |   |                                        |   |
|  |  testng-chrome.xml                   |   |  CucumberTestRunner.java               |   |
|  |  testng-firefox.xml                  |   |    extends AbstractTestNGCucumberTests  |   |
|  |  testng-edge.xml                     |   |    @DataProvider(parallel = true)       |   |
|  |  testng-smoke.xml                    |   |                                        |   |
|  |  testng-regression.xml              |   |  Feature Files (.feature)               |   |
|  |         |                            |   |    |                                   |   |
|  |         v                            |   |    v                                   |   |
|  |  BaseTest.java                       |   |  Hooks.java (@Before / @After)         |   |
|  |    @BeforeMethod -> initDriver       |   |    @Before -> initDriver               |   |
|  |    @AfterMethod  -> quitDriver       |   |    @After  -> screenshot + quit        |   |
|  |         |                            |   |         |                              |   |
|  |         v                            |   |         v                              |   |
|  |  Test Classes:                       |   |  Step Definitions:                     |   |
|  |    LoginTest.java                    |   |    LoginSteps.java                     |   |
|  |    PurchaseTest.java                 |   |    PurchaseTestSteps.java              |   |
|  |    ErrorMessageTest.java             |   |    ErrorMessageTestSteps.java          |   |
|  |    RichTextEditorTest.java           |   |    RichTextEditorSteps.java            |   |
|  |    FrameAndTabHandlingTest.java      |   |    HandleTabAndIFrameSteps.java        |   |
|  |    RestApiTest.java                  |   |    RestAssuredApiTestSteps.java         |   |
|  |    PurchaseTestVanilla.java          |   |                                        |   |
|  +--------------------------------------+   +----------------------------------------+   |
|                     |                                      |                             |
|                     +------------------+-------------------+                             |
|                                        |                                                 |
|                                        v                                                 |
|  +------------------------------------------------------------------------------------+  |
|  |                             SHARED CORE LAYER                                      |  |
|  +------------------------------------------------------------------------------------+  |
|  |                                                                                    |  |
|  |  +-----------------+   +------------------+   +--------------------------------+  |  |
|  |  | ConfigReader    |   | DriverFactory    |   | Page Objects (POM)             |  |  |
|  |  | .properties     |   | ThreadLocal      |   |   BasePage.java                |  |  |
|  |  | System override |   | Chrome/FF/Edge   |   |   LoginPage.java               |  |  |
|  |  +-----------------+   +------------------+   |   InventoryPage.java            |  |  |
|  |                                               |   CartPage.java                 |  |  |
|  |  +-----------------+   +------------------+   |   CheckOutStepOnePage.java      |  |  |
|  |  | ScreenshotUtils |   | JsonDataReader   |   |   CheckOutStepTwoPage.java     |  |  |
|  |  | Allure attach   |   | @DataProvider    |   |   CheckoutCompletePage.java    |  |  |
|  |  | File + Base64   |   | JSON -> Object[] |   |   RichTextEditorPage.java      |  |  |
|  |  +-----------------+   +------------------+   |   Guru99HomePage.java           |  |  |
|  |                                               +--------------------------------+  |  |
|  |  +-----------------+   +------------------+   +---------------------+              |  |
|  |  | RetryAnalyzer   |   | ReportLogger     |   | Data Models (API)   |              |  |
|  |  | Auto-retry on   |   | Log4j + Allure   |   |   User, Company,    |              |  |
|  |  | test failure    |   | step/pass/fail   |   |   Address, Geo       |              |  |
|  |  +-----------------+   +------------------+   +---------------------+              |  |
|  |                                                                                    |  |
|  +------------------------------------------------------------------------------------+  |
|                                        |                                                 |
|                                        v                                                 |
|  +------------------------------------------------------------------------------------+  |
|  |                              REPORTING LAYER                                       |  |
|  +------------------------------------------------------------------------------------+  |
|  |   Allure Reports   |   Cucumber HTML/JSON   |   TestNG Reports   |   Screenshots   |  |
|  +------------------------------------------------------------------------------------+  |
+==========================================================================================+
```

## TestNG Execution Flow

```
Maven (mvn clean test)
  |
  +--> testng-{suite}.xml
         |
         +--> TestListener.java (ITestListener)
         |      onTestStart / onTestSuccess / onTestFailure (screenshot)
         |
         +--> BaseTest.java
                @BeforeMethod --> DriverFactory.initDriver(browser)
                @AfterMethod  --> DriverFactory.quitDriver()
                |
                +--> Test Classes (extend BaseTest)
                       |
                       +--> Page Objects (extend BasePage)
                       |      click / type / waitForVisible / scroll
                       |
                       +--> RetryAnalyzer (auto-retry on failure)
                       |
                       +--> ReportLogger (Allure steps + Log4j)
```

## Cucumber (BDD) Execution Flow

```
Maven (mvn clean test -Dtest=CucumberTestRunner)
  |
  +--> CucumberTestRunner.java
         @CucumberOptions(features, glue, plugin, tags)
         extends AbstractTestNGCucumberTests
         @DataProvider(parallel = true)
         |
         +--> Feature Files (.feature)
         |      Gherkin: Given / When / Then / And
         |
         +--> Hooks.java
         |      @Before --> DriverFactory.initDriver()
         |      @After  --> screenshot on failure + quitDriver()
         |
         +--> Step Definitions
                @Given / @When / @Then / @And
                |
                +--> Page Objects (shared with TestNG)
                |
                +--> ScreenshotUtils.attachScreenshotToAllure()
                       (attached on every @Then step)
```

## Project Structure

```
src/
├── main/java/com/vignesh/
│   ├── config/
│   │   └── ConfigReader.java              # Reads config.properties with system property override
│   ├── driver/
│   │   └── DriverFactory.java             # ThreadLocal WebDriver management (parallel-safe)
│   ├── pages/                             # Page Object Model
│   │   ├── BasePage.java                  # Common actions: click, type, wait, scroll
│   │   ├── LoginPage.java                 # SauceDemo login page
│   │   ├── InventoryPage.java             # SauceDemo product listing page
│   │   ├── CartPage.java                  # Shopping cart page
│   │   ├── CheckOutStepOnePage.java       # Checkout information form
│   │   ├── CheckOutStepTwoPage.java       # Checkout overview
│   │   ├── CheckoutCompletePage.java      # Order confirmation page
│   │   ├── RichTextEditorPage.java        # CKEditor interactions
│   │   └── Guru99HomePage.java            # iFrame and tab handling
│   ├── listeners/
│   │   └── TestListener.java              # Allure integration + screenshot on failure
│   ├── models/                            # POJO models for API response deserialization
│   │   ├── User.java
│   │   ├── Company.java
│   │   ├── Address.java
│   │   └── Geo.java
│   └── utils/
│       ├── ScreenshotUtils.java           # Screenshot capture (file, Base64, Allure attachment)
│       ├── JsonDataReader.java            # JSON-based data-driven test data reader
│       ├── ReportLogger.java              # Dual logging: Log4j + Allure steps
│       └── RetryAnalyzer.java             # Auto-retry failed tests (configurable)
│
├── test/java/com/vignesh/
│   ├── base/
│   │   └── BaseTest.java                  # TestNG base class with driver lifecycle
│   ├── tests/                             # TestNG test classes
│   │   ├── LoginTest.java                 # Data-driven login tests via @DataProvider
│   │   ├── PurchaseTest.java              # End-to-end purchase flow
│   │   ├── ErrorMessageTest.java          # Error message and footer validation
│   │   ├── RichTextEditorTest.java        # CKEditor bold/underline formatting
│   │   ├── FrameAndTabHandlingTest.java   # iFrame switch + new tab handling
│   │   ├── RestApiTest.java               # REST API tests with POJO deserialization
│   │   └── PurchaseTestVanilla.java       # Vanilla Selenium (no POM) purchase flow
│   ├── stepdefinitions/                   # Cucumber step definitions
│   │   ├── Hooks.java                     # Before/After hooks with driver + screenshot
│   │   ├── LoginSteps.java
│   │   ├── PurchaseTestSteps.java
│   │   ├── ErrorMessageTestSteps.java
│   │   ├── HandleTabAndIFrameSteps.java
│   │   ├── RichTextEditorSteps.java
│   │   └── RestAssuredApiTestSteps.java
│   └── runners/
│       └── CucumberTestRunner.java        # Cucumber-TestNG runner with parallel support
│
└── test/resources/
    ├── config.properties                  # Browser, URLs, timeouts, retry config
    ├── credential.json                    # Login credentials for purchase tests
    ├── allure.properties                  # Allure results directory config
    ├── log4j2.xml                         # Log4j2 configuration
    ├── testng-chrome.xml                  # Full suite on Chrome
    ├── testng-firefox.xml                 # Full suite on Firefox
    ├── testng-edge.xml                    # Full suite on Edge
    ├── testng-smoke.xml                   # Smoke test suite
    ├── testng-regression.xml              # Regression test suite
    ├── testdata/
    │   └── loginData.json                 # Data-driven test data (5 login scenarios)
    └── features/
        ├── login.feature                  # Login scenarios (valid + invalid)
        ├── purchase_automation.feature    # End-to-end purchase flow
        ├── error_messages.feature         # Error message and footer validation
        ├── iframe_tab_handling.feature    # iFrame and new tab handling
        ├── rich_text_editor.feature       # Rich text editor formatting
        └── rest_api_testing.feature       # REST API GET users validation
```

## Test Cases

### TestNG Test Cases

| # | Test Class | Test Method | Description |
|---|---|---|---|
| 1 | `LoginTest` | `testLoginScenarios` | Data-driven login: valid users (standard_user, performance_glitch_user), locked user, empty fields, wrong password |
| 2 | `LoginTest` | `testMandatoryFieldValidation` | Click login without credentials, verify "Username is required" error |
| 3 | `PurchaseTest` | `testEndToEndPurchase` | Full purchase flow: login, add 2 items, checkout, verify "Thank you" message |
| 4 | `ErrorMessageTest` | `validateInvalidLoginErrorMessage` | Click login without credentials, verify error message |
| 5 | `ErrorMessageTest` | `scrollToFooterAndValidateText` | Login, scroll to footer, verify contains "2026" and "Terms of Service" |
| 6 | `RichTextEditorTest` | `testRichTextEditorFormatting` | Type bold, underline, and normal text in CKEditor, validate HTML formatting |
| 7 | `FrameAndTabHandlingTest` | `testIFrameAndTabHandling` | Switch to iFrame, click image to open new tab, verify title, hover on menu |
| 8 | `RestApiTest` | `testGetUsersApi` | GET /users API, verify status 200, validate POJO deserialization, nested objects |
| 9 | `PurchaseTestVanilla` | `testPurchaseProcess` | Full purchase flow using vanilla Selenium (no Page Objects) |
| 10 | `PurchaseTestVanilla` | `testMandatoryFieldErrorAndFooter` | Error message + footer validation using vanilla Selenium |

### Cucumber (BDD) Test Scenarios

| # | Feature File | Scenario | Tag |
|---|---|---|---|
| 1 | `purchase_automation.feature` | Automate Purchase Process: login, add items, checkout, verify success | `@testCase1` |
| 2 | `error_messages.feature` | Verify error messages for mandatory fields and footer text | `@testCase2` |
| 3 | `rich_text_editor.feature` | Type and validate formatted text in rich text editor | `@testCase3` |
| 4 | `iframe_tab_handling.feature` | Handle iFrame click and new tab navigation | `@testCase4` |
| 5 | `rest_api_testing.feature` | GET users API and validate response data | `@testCase5` |
| 6 | `login.feature` | Successful login with valid credentials | `@smoke` |
| 7 | `login.feature` | Login fails with empty credentials | `@negative @smoke` |

#### 1. Purchase Automation (`@testCase1 @regression`)

```gherkin
Feature: SauceDemo Purchase and Validation

  Scenario: Automate Purchase Process
    Given user opens the url "https://www.saucedemo.com/inventory.html"
    And user parses credentials from credential.json file
    When user types the username and password and clicks on login button
    And user adds "Sauce Labs Backpack" to the shopping cart
    And user adds "Sauce Labs Fleece Jacket" to the shopping cart
    Then the number on the cart symbol should be "2"
    When user goes through the checkout process
    Then "Thank you for your order!" message should be displayed
```

#### 2. Error Messages (`@testCase2 @regression`)

```gherkin
Feature: SauceDemo Purchase and Validation

  Scenario: Verify error messages for mandatory fields
    Given user opens the url "https://www.saucedemo.com/inventory.html"
    When user clicks on the login button
    Then the error message should be displayed as "Epic sadface: Username is required"
    When user logs in with credentials "standard_user" and "secret_sauce"
    And user scrolls down to the bottom of the page
    Then the footer message should contain "2026" and "Terms of Service"
```

#### 3. Rich Text Editor (`@testCase3 @regression`)

```gherkin
Feature: Rich Text Editor Validation

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
```

#### 4. iFrame and Tab Handling (`@testCase4 @regression`)

```gherkin
Feature: iFrame and Tab Handling

  Scenario: Handle iFrame click and new tab navigation
    Given user navigates to the url "http://demo.guru99.com/test/guru99home"
    When user switches to the iframe near the bottom of the page
    And user clicks on the image inside the iframe
    Then a new tab should open with title "Selenium Live Project for Practice"
    When user closes the new tab and switches back to the main window
    And user hovers on the "Testing" menu item from the top menu
    And user clicks on the "Selenium" link from the dropdown
```

#### 5. REST API Testing (`@testCase5 @regression`)

```gherkin
Feature: REST API Testing

  Scenario: GET users and validate response data
    Given the API base URL is configured
    When user sends a GET request to "/users" endpoint
    Then the response status code should be 200
    And the response should contain a non-empty list of users
    And user logs the name and email of each user
    And the first user email should contain "@"
```

#### 6. Login - Valid Credentials (`@smoke`)

```gherkin
Feature: Login Functionality

  Scenario: Successful login with valid credentials
    Given the user is on the login page
    When the user logs in with username "standard_user" and password "secret_sauce"
    Then the inventory page should be displayed
```

#### 7. Login - Empty Credentials (`@negative @smoke`)

```gherkin
Feature: Login Functionality

  Scenario: Login fails with empty credentials
    Given the user is on the login page
    When the user clicks the login button without entering credentials
    Then the error message "Epic sadface: Username is required" should be displayed
```

### Applications Under Test

| Application | URL | Test Coverage |
|---|---|---|
| SauceDemo | https://www.saucedemo.com | Login, purchase, error messages, footer |
| JSONPlaceholder API | https://jsonplaceholder.typicode.com | GET /users endpoint |
| Online HTML Editor | https://onlinehtmleditor.dev | CKEditor bold/underline formatting |
| Guru99 Demo | http://demo.guru99.com/test/guru99home | iFrame, new tab, hover menu |

## Commands to Run Tests

### Prerequisites

- Java 17+
- Maven 3.8+
- Chrome / Firefox / Edge browser installed

### Run All Tests (Default TestNG Suite)

```bash
mvn clean test
```

### Run with a Specific Browser

```bash
mvn clean test -Dbrowser=chrome
mvn clean test -Dbrowser=firefox
mvn clean test -Dbrowser=edge
```

### Run in Headless Mode

```bash
mvn clean test -Dheadless=true
```

### Run a Specific TestNG Suite

```bash
# Chrome suite (all tests)
mvn clean test -DsuiteXmlFile=src/test/resources/testng-chrome.xml

# Firefox suite
mvn clean test -DsuiteXmlFile=src/test/resources/testng-firefox.xml

# Edge suite
mvn clean test -DsuiteXmlFile=src/test/resources/testng-edge.xml

# Smoke tests only
mvn clean test -DsuiteXmlFile=src/test/resources/testng-smoke.xml

# Regression tests only
mvn clean test -DsuiteXmlFile=src/test/resources/testng-regression.xml
```

### Run a Specific TestNG Test Class

```bash
mvn clean test -Dtest=com.vignesh.tests.LoginTest
mvn clean test -Dtest=com.vignesh.tests.PurchaseTest
mvn clean test -Dtest=com.vignesh.tests.ErrorMessageTest
mvn clean test -Dtest=com.vignesh.tests.RichTextEditorTest
mvn clean test -Dtest=com.vignesh.tests.FrameAndTabHandlingTest
mvn clean test -Dtest=com.vignesh.tests.RestApiTest
```

### Run Only Cucumber BDD Tests

```bash
mvn clean test -Dtest=com.vignesh.runners.CucumberTestRunner
```

### Run Cucumber Tests by Tag

```bash
# Single tag
mvn clean test -Dtest=com.vignesh.runners.CucumberTestRunner -Dcucumber.filter.tags="@testCase1"

# Multiple tags
mvn clean test -Dtest=com.vignesh.runners.CucumberTestRunner -Dcucumber.filter.tags="@testCase1 or @testCase2"

# Smoke tests
mvn clean test -Dtest=com.vignesh.runners.CucumberTestRunner -Dcucumber.filter.tags="@smoke"
```

### Run with Cucumber Retry (Failed Scenario Retry)

```bash
mvn clean test -Dtest=com.vignesh.runners.CucumberTestRunner -Dcucumber.execution.retry=2
```

### Run with Combined Options

```bash
mvn clean test -Dbrowser=firefox -Dheadless=true -Dcucumber.execution.retry=2
```

### Generate Allure Report

```bash
# Run tests first, then generate and open report
mvn clean test
mvn allure:serve
```

## CI/CD - GitHub Actions

Tests run automatically on every push and pull request to `main`/`master` via GitHub Actions.

### Workflow Overview

The workflow (`.github/workflows/ci.yml`) runs two parallel jobs:

| Job | Suite | Description |
|---|---|---|
| `testng-tests` | `testng-chrome.xml` | All TestNG tests in headless Chrome |
| `cucumber-tests` | `CucumberTestRunner` | All Cucumber BDD scenarios in headless Chrome |

### Artifacts

After each run, the following artifacts are uploaded:

| Artifact | Condition | Contents |
|---|---|---|
| `allure-results-testng` | Always | Allure results from TestNG tests |
| `allure-results-cucumber` | Always | Allure results from Cucumber tests |
| `cucumber-reports` | Always | Cucumber HTML/JSON reports |
| `screenshots-*` | On failure | Failure screenshots |

### Manual Trigger

You can also trigger the workflow manually from the **Actions** tab using `workflow_dispatch`.

## Reports

After test execution, reports are generated at:

| Report | Location |
|---|---|
| Allure Results | `target/allure-results/` |
| Cucumber HTML | `target/cucumber-reports/cucumber.html` |
| Cucumber JSON | `target/cucumber-reports/cucumber.json` |
| TestNG Default | `target/surefire-reports/index.html` |
| Screenshots | `target/screenshots/` |

## Configuration

All framework settings are managed in `src/test/resources/config.properties`:

```properties
browser=chrome              # chrome | firefox | edge
headless=false              # true for headless execution
base_url=https://www.saucedemo.com
api_base_url=https://jsonplaceholder.typicode.com
explicit_wait=30            # seconds
page_load_timeout=60        # seconds
max_retry_count=1           # TestNG retry attempts
screenshot.on.failure=true
screenshot_path=target/screenshots/
```

Any property can be overridden at runtime via `-D` flag:

```bash
mvn clean test -Dbrowser=firefox -Dheadless=true
```

## Key Design Decisions

- **ThreadLocal WebDriver** -- `DriverFactory` uses `ThreadLocal<WebDriver>` for thread-safe parallel execution across both TestNG and Cucumber
- **Page Object Model** -- Fluent method chaining where each action returns `this` or the next page object
- **Shared Core Layer** -- Page objects, utilities, and config are shared between TestNG and Cucumber tests
- **Dual Reporting** -- Allure integrates with both TestNG (`allure-testng`) and Cucumber (`allure-cucumber7-jvm`) via plugins
- **Screenshot on @Then Steps** -- Every Cucumber `@Then` step captures a screenshot to Allure for visual verification
- **REST Assured + Allure** -- `AllureRestAssured` filter attaches request/response bodies to the report
- **Retry Logic** -- TestNG uses `RetryAnalyzer`, Cucumber uses `cucumber.execution.retry` system property
- **Data-Driven Testing** -- JSON files + `@DataProvider` for parameterized TestNG tests

## Adding New Tests

### Adding a new Page Object
1. Create a class in `src/main/java/com/vignesh/pages/` extending `BasePage`
2. Define locators as `private static final By` fields
3. Add action methods using inherited helpers (`click`, `type`, `getText`, `waitForVisible`)

### Adding a new TestNG test
1. Create a class in `src/test/java/com/vignesh/tests/` extending `BaseTest`
2. Use `getDriver()` to access the WebDriver instance
3. Add the class to the appropriate `testng-*.xml` suite file

### Adding a new Cucumber scenario
1. Write the feature file in `src/test/resources/features/`
2. Create step definitions in `src/test/java/com/vignesh/stepdefinitions/`
3. Tag scenarios and update `CucumberTestRunner` tags if needed

### Adding data-driven test data
1. Add a JSON file in `src/test/resources/testdata/`
2. Use `JsonDataReader.toDataProviderArray("testdata/yourFile.json")` in a `@DataProvider`
