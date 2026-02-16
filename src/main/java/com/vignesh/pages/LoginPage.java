package com.vignesh.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private static final By USERNAME_FIELD = By.id("user-name");
    private static final By PASSWORD_FIELD = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");
    private static final By ERROR_MESSAGE = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage enterUsername(String username) {
        logger.info("Entering username: {}", username);
        type(USERNAME_FIELD, username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        logger.info("Entering password");
        type(PASSWORD_FIELD, password);
        return this;
    }

    public InventoryPage loginAs(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
        logger.info("Login successful, navigated to Inventory page");
        return new InventoryPage(driver);
    }

    public LoginPage clickLogin() {
        logger.info("Clicking login button");
        click(LOGIN_BUTTON);
        return this;
    }

    public String getErrorMessage() {
        String error = getText(ERROR_MESSAGE);
        logger.info("Error message: {}", error);
        return error;
    }

    public boolean isErrorDisplayed() {
        return isDisplayed(ERROR_MESSAGE);
    }

    public boolean isLoginPageDisplayed() {
        return isDisplayed(USERNAME_FIELD);
    }
}
