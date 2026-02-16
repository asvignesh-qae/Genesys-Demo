package com.vignesh.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class CheckoutCompletePage extends BasePage {

    private static final By COMPLETE_HEADER = By.className("complete-header");
    private static final By BACK_HOME_BUTTON = By.id("back-to-products");

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
        waitForVisible(COMPLETE_HEADER);
        logger.info("Checkout Complete page loaded");
    }

    public CheckoutCompletePage validateIfOnCheckoutCompletePage() {
        waitForUrlContains("checkout-complete");
        logger.info("User is on checkout-complete page");
        Assert.assertEquals(getPageTitle(), "Swag Labs", "/checkout-complete Page title should be `Swag Labs`");
        logger.info("checkout-complete page title is correct");
        return this;
    }

    public String getSuccessMessage() {
        String message = getText(COMPLETE_HEADER);
        logger.info("Success message: {}", message);
        return message;
    }

    public InventoryPage backToProducts() {
        click(BACK_HOME_BUTTON);
        return new InventoryPage(driver);
    }
}
