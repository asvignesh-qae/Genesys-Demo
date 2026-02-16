package com.vignesh.stepdefinitions;

import com.vignesh.config.ConfigReader;
import com.vignesh.driver.DriverFactory;
import com.vignesh.pages.InventoryPage;
import com.vignesh.pages.LoginPage;
import com.vignesh.utils.ScreenshotUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class LoginSteps {

    private LoginPage loginPage;
    private InventoryPage inventoryPage;

    @Given("the user is on the login page")
    public void theUserIsOnTheLoginPage() {
        DriverFactory.getDriver().get(ConfigReader.getBaseUrl());
        loginPage = new LoginPage(DriverFactory.getDriver());
    }

    @When("the user logs in with username {string} and password {string}")
    public void theUserLogsInWithCredentials(String username, String password) {
        if (isValidCredential(username, password)) {
            inventoryPage = loginPage.loginAs(username, password);
        } else {
            loginPage.enterUsername(username);
            loginPage.enterPassword(password);
            loginPage.clickLogin();
        }
    }

    @When("the user clicks the login button without entering credentials")
    public void theUserClicksLoginWithoutCredentials() {
        loginPage.clickLogin();
    }

    @Then("the inventory page should be displayed")
    public void theInventoryPageShouldBeDisplayed() {
        Assert.assertTrue(inventoryPage.isPageLoaded(),
                "Inventory page should be displayed after login");
        ScreenshotUtils.attachScreenshotToAllure(DriverFactory.getDriver(), "Inventory page verification");
    }

    @Then("the error message {string} should be displayed")
    public void theErrorMessageShouldBeDisplayed(String expectedMessage) {
        Assert.assertEquals(loginPage.getErrorMessage(), expectedMessage);
        ScreenshotUtils.attachScreenshotToAllure(DriverFactory.getDriver(), "Error message verification");
    }

    private boolean isValidCredential(String username, String password) {
        return username != null && !username.isEmpty()
                && password.equals("secret_sauce")
                && !username.equals("locked_out_user");
    }
}
