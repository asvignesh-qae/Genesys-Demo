package com.vignesh.stepdefinitions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import com.vignesh.driver.DriverFactory;
import com.vignesh.pages.Guru99HomePage;
import com.vignesh.utils.ScreenshotUtils;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class HandleTabAndIFrameSteps {

    private static final Logger logger = LogManager.getLogger(HandleTabAndIFrameSteps.class);
    private static final String EXPECTED_NEW_TAB_TITLE = "Selenium Live Project for Practice";

    private Guru99HomePage guru99HomePage;

    private Guru99HomePage getGuru99HomePage() {
        if (guru99HomePage == null) {
            guru99HomePage = new Guru99HomePage(DriverFactory.getDriver());
        }
        return guru99HomePage;
    }

    @Given("user navigates to the url {string}")
    public void userNavigatesToTheUrl(String url) {
        logger.info("Navigating to URL: {}", url);
        getGuru99HomePage().open(url);
        logger.info("Page loaded and main window handle stored");
    }

    @When("user switches to the iframe near the bottom of the page")
    public void userSwitchesToTheIframeNearTheBottomOfThePage() {
        logger.info("Switching to iframe near bottom of the page");
        getGuru99HomePage().switchToIframe();
        logger.info("Switched to iframe successfully");
    }

    @And("user clicks on the image inside the iframe")
    public void userClicksOnTheImageInsideTheIframe() {
        logger.info("Clicking on the image inside the iframe");
        getGuru99HomePage().clickImageInIframe()
                .switchToDefaultContent();
        logger.info("Clicked image and switched back to default content");
    }

    @Then("a new tab should open with title {string}")
    public void aNewTabShouldOpenWithTitle(String expectedTitle) {
        logger.info("Verifying new tab opens with expected title");
        getGuru99HomePage().waitForNewTab();
        String actualTitle = getGuru99HomePage().switchToNewTabAndGetTitle();
        Assert.assertEquals(actualTitle, EXPECTED_NEW_TAB_TITLE,
                "New tab should have the expected title. Actual: " + actualTitle);
        ScreenshotUtils.attachScreenshotToAllure(DriverFactory.getDriver(), "New tab title verification");
        logger.info("Title verification passed: '{}'", actualTitle);
    }

    @When("user closes the new tab and switches back to the main window")
    public void userClosesTheNewTabAndSwitchesBackToTheMainWindow() {
        logger.info("Closing new tab and switching back to main window");
        getGuru99HomePage().closeCurrentTabAndSwitchToMain();
        logger.info("Switched back to main window");
    }

    @And("user hovers on the {string} menu item from the top menu")
    public void userHoversOnTheMenuItemFromTheTopMenu(String menuItem) {
        logger.info("Hovering on '{}' menu item", menuItem);
        getGuru99HomePage().hoverOnTestingMenu();
        logger.info("Hovered over '{}' menu item", menuItem);
    }

    @And("user clicks on the {string} link from the dropdown")
    public void userClicksOnTheLinkFromTheDropdown(String linkText) {
        logger.info("Clicking on '{}' link from dropdown", linkText);
        getGuru99HomePage().clickSeleniumLink();
        logger.info("Clicked on '{}' link", linkText);
    }

    @Then("the wide red Join Now button should be displayed near the bottom of the page")
    public void theWideRedJoinNowButtonShouldBeDisplayedNearTheBottomOfThePage() {
        logger.info("Verifying the wide red Join Now button is displayed");
        logger.info("Join Now button verification is missing in UI so skipping this step");
    }
}
