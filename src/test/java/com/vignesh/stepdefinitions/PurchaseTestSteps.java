package com.vignesh.stepdefinitions;

import com.google.gson.JsonObject;
import com.vignesh.driver.DriverFactory;
import com.vignesh.pages.*;
import com.vignesh.utils.ScreenshotUtils;
import com.vignesh.utils.JsonDataReader;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

public class PurchaseTestSteps {

    private static final Logger logger = LogManager.getLogger(PurchaseTestSteps.class);

    private LoginPage loginPage;
    private InventoryPage inventoryPage;
    private CheckoutCompletePage completePage;
    private String username;
    private String password;

    @Given("user opens the url {string}")
    public void userOpensTheUrl(String url) {
        logger.info("Opening URL: {}", url);
        DriverFactory.getDriver().get(url);
        loginPage = new LoginPage(DriverFactory.getDriver());
    }

    @And("user parses credentials from credential.json file")
    public void userParsesCredentialsFromCredentialJsonFile() {
        JsonObject credentials = JsonDataReader.readJsonFile("credential.json");
        username = credentials.get("username").getAsString();
        password = credentials.get("password").getAsString();
        logger.info("Credentials loaded - username: {}", username);
    }

    @When("user types the username and password and clicks on login button")
    public void userTypesTheUsernameAndPasswordAndClicksOnLoginButton() {
        inventoryPage = loginPage.loginAs(username, password);
        inventoryPage.waitForPageLoad();
        inventoryPage.validateOnInventoryPage();
    }

    @When("user adds {string} to the shopping cart")
    public void userAddsItemToTheShoppingCart(String itemName) {
        inventoryPage.addItemToCart(itemName);
    }

    @Then("the number on the cart symbol should be {string}")
    public void theNumberOnTheCartSymbolShouldBe(String expectedCount) {
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), expectedCount,
                "Cart badge should show " + expectedCount + " items");
        ScreenshotUtils.attachScreenshotToAllure(DriverFactory.getDriver(), "Cart badge count verification");
    }

    @When("user goes through the checkout process")
    public void userGoesThroughTheCheckoutProcess() {
        CartPage cartPage = inventoryPage.goToCart();
        cartPage.validateIfOnCartPage();
        CheckOutStepOnePage checkoutStepOnePage = cartPage.proceedToCheckout();
        CheckOutStepTwoPage checkOutStepTwoPage = checkoutStepOnePage.fillInfoAndContinue("Liszt", "Franz", "12345");
        checkOutStepTwoPage.validateIfOnCheckoutStepTwoPage();
        completePage = checkOutStepTwoPage.finishCheckout();
        completePage.validateIfOnCheckoutCompletePage();
    }

    @Then("{string} message should be displayed")
    public void messageShouldBeDisplayed(String expectedMessage) {
        Assert.assertEquals(completePage.getSuccessMessage(), expectedMessage,
                "Order completion message should be displayed");
        ScreenshotUtils.attachScreenshotToAllure(DriverFactory.getDriver(), "Order completion message verification");
    }
}
