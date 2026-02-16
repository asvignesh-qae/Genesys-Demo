package com.vignesh.stepdefinitions;

import com.vignesh.driver.DriverFactory;
import com.vignesh.pages.InventoryPage;
import com.vignesh.pages.LoginPage;
import com.vignesh.utils.ScreenshotUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

public class ErrorMessageTestSteps {

    private static final Logger logger = LogManager.getLogger(ErrorMessageTestSteps.class);

    @When("user clicks on the login button")
    public void userClicksOnTheLoginButton() {
        new LoginPage(DriverFactory.getDriver()).clickLogin();
    }

    @Then("the error message should be displayed as {string}")
    public void theErrorMessageShouldBeDisplayedAs(String expectedError) {
        LoginPage loginPage = new LoginPage(DriverFactory.getDriver());
        Assert.assertEquals(loginPage.getErrorMessage(), expectedError,
                "Error message validation failed");
        ScreenshotUtils.attachScreenshotToAllure(DriverFactory.getDriver(), "Error message verification");
    }

    @When("user logs in with credentials {string} and {string}")
    public void userLogsInWithCredentials(String username, String password) {
        logger.info("Logging in with username: {}", username);
        new LoginPage(DriverFactory.getDriver()).loginAs(username, password);
    }

    @And("user scrolls down to the bottom of the page")
    public void userScrollsDownToTheBottomOfThePage() {
        new InventoryPage(DriverFactory.getDriver()).scrollToBottom();
    }

    @Then("the footer message should contain {string} and {string}")
    public void theFooterMessageShouldContain(String text1, String text2) {
        String footerText = new InventoryPage(DriverFactory.getDriver()).getFooterText();
        Assert.assertTrue(footerText.contains(text1),
                "Footer should contain '" + text1 + "'. Actual: " + footerText);
        Assert.assertTrue(footerText.contains(text2),
                "Footer should contain '" + text2 + "'. Actual: " + footerText);
        ScreenshotUtils.attachScreenshotToAllure(DriverFactory.getDriver(), "Footer message verification");
    }
}
