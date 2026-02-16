package com.vignesh.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class CheckOutStepTwoPage extends BasePage {

    private static final By FINISH_BUTTON = By.id("finish");
    private static final By SUMMARY_INFO = By.className("summary_info");

    public CheckOutStepTwoPage(WebDriver driver) {
        super(driver);
        waitForVisible(FINISH_BUTTON);
        logger.info("Checkout Overview page loaded");
    }

    public CheckOutStepTwoPage validateIfOnCheckoutStepTwoPage() {
        waitForUrlContains("checkout-step-two");
        logger.info("User is on checkout-step-two page");
        Assert.assertEquals(getPageTitle(), "Swag Labs", "/checkout-step-two Page title should be `Swag Labs`");
        logger.info("checkout-step-two page title is correct");
        return this;
    }

    public CheckoutCompletePage finishCheckout() {
        click(FINISH_BUTTON);
        logger.info("Clicked Finish button");
        return new CheckoutCompletePage(driver);
    }

    public boolean isSummaryDisplayed() {
        return isDisplayed(SUMMARY_INFO);
    }
}
