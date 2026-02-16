package com.vignesh.tests;

import com.vignesh.base.BaseTest;
import com.vignesh.config.ConfigReader;
import com.vignesh.pages.InventoryPage;
import com.vignesh.pages.LoginPage;
import com.vignesh.utils.ReportLogger;
import com.vignesh.utils.RetryAnalyzer;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ErrorMessageTest extends BaseTest {

    @Test(priority = 1, groups = {"regression", "testCase2a"}, retryAnalyzer = RetryAnalyzer.class)
    public void validateInvalidLoginErrorMessage() {
        ReportLogger.step("Step 1: Navigate to login page and login");
        getDriver().get(ConfigReader.getBaseUrl());
        LoginPage loginPage = new LoginPage(getDriver());
        ReportLogger.step("Step 2: Click on login button without entering credentials");
        loginPage.clickLogin();
        ReportLogger.step("Step 3: Validate error message is displayed");
        Assert.assertEquals(loginPage.getErrorMessage(), "Epic sadface: Username is required",
                "Error message should be displayed when username or password is missing");
    }

    @Test(priority = 2, groups = {"regression", "testCase2b"}, retryAnalyzer = RetryAnalyzer.class)
    public void scrollToFooterAndValidateText() {
        ReportLogger.step("Step 1: Navigate to SauceDemo login page and login");
        getDriver().get(ConfigReader.getBaseUrl());
        LoginPage loginPage = new LoginPage(getDriver());
        InventoryPage inventoryPage = loginPage.loginAs("standard_user", "secret_sauce");
        inventoryPage.waitForPageLoad();
        ReportLogger.step("Step 2: Scroll to footer and validate text contains '2026' and 'Terms of Service'");
        String footerText = inventoryPage.getFooterText();
        Assert.assertTrue(footerText.contains("2026"),
                "Footer should contain '2026'. Actual: " + footerText);
        Assert.assertTrue(footerText.contains("Terms of Service"),
                "Footer should contain 'Terms of Service'. Actual: " + footerText);
        ReportLogger.pass("Footer validation test passed");
    }
}
