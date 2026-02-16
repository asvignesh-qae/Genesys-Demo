package com.vignesh.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckOutStepOnePage extends BasePage {

    private static final By FIRST_NAME = By.id("first-name");
    private static final By LAST_NAME = By.id("last-name");
    private static final By POSTAL_CODE = By.id("postal-code");
    private static final By CONTINUE_BUTTON = By.id("continue");

    public CheckOutStepOnePage(WebDriver driver) {
        super(driver);
        waitForUrlContains("checkout-step-one");
        waitForVisible(FIRST_NAME);
        logger.info("Checkout Information page loaded");
    }

    public CheckOutStepOnePage enterFirstName(String firstName) {
        type(FIRST_NAME, firstName);
        return this;
    }

    public CheckOutStepOnePage enterLastName(String lastName) {
        type(LAST_NAME, lastName);
        return this;
    }

    public CheckOutStepOnePage enterPostalCode(String postalCode) {
        type(POSTAL_CODE, postalCode);
        return this;
    }

    public CheckOutStepTwoPage fillInfoAndContinue(String firstName, String lastName, String postalCode) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostalCode(postalCode);
        click(CONTINUE_BUTTON);
        logger.info("Filled checkout info ({} {}, {}) and continued", firstName, lastName, postalCode);
        return new CheckOutStepTwoPage(driver);
    }
}
