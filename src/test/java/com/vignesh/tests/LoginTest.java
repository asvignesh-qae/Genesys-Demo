package com.vignesh.tests;

import com.vignesh.base.BaseTest;
import com.vignesh.config.ConfigReader;
import com.vignesh.pages.InventoryPage;
import com.vignesh.pages.LoginPage;
import com.vignesh.utils.JsonDataReader;
import com.vignesh.utils.ReportLogger;
import com.vignesh.utils.RetryAnalyzer;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

public class LoginTest extends BaseTest {

    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        return JsonDataReader.toDataProviderArray("testdata/loginData.json");
    }

    @Test(groups = {"smoke", "login"}, dataProvider = "loginData")
    public void testLoginScenarios(Map<String, String> data) {
        String username = data.get("username");
        String password = data.get("password");
        String expected = data.get("expectedResult");

        ReportLogger.step("Testing login with username='" + username + "', expected='" + expected + "'");

        ReportLogger.step("Step 1: Navigate to login page");
        getDriver().get(ConfigReader.getBaseUrl());
        LoginPage loginPage = new LoginPage(getDriver());

        if ("success".equals(expected)) {
            ReportLogger.step("Step 2: Login with valid credentials");
            InventoryPage inventoryPage = loginPage.loginAs(username, password);
            Assert.assertTrue(inventoryPage.isPageLoaded(),
                    "Inventory page should load after successful login");
            ReportLogger.pass("Login succeeded for user: " + username);
        } else {
            ReportLogger.step("Step 2: Login with invalid credentials");
            loginPage.enterUsername(username);
            loginPage.enterPassword(password);
            loginPage.clickLogin();

            ReportLogger.step("Step 3: Verify error message");
            Assert.assertEquals(loginPage.getErrorMessage(), expected);
            ReportLogger.pass("Error validation passed for user: " + username);
        }
    }

    @Test(groups = {"smoke", "login"}, retryAnalyzer = RetryAnalyzer.class)
    public void testMandatoryFieldValidation() {
        ReportLogger.step("Step 1: Navigate to login page");
        getDriver().get(ConfigReader.getBaseUrl());
        LoginPage loginPage = new LoginPage(getDriver());

        ReportLogger.step("Step 2: Click login without entering any fields");
        loginPage.clickLogin();

        ReportLogger.step("Step 3: Verify mandatory field error message");
        Assert.assertEquals(
                loginPage.getErrorMessage(),
                "Epic sadface: Username is required",
                "Error message should indicate username is required"
        );
        ReportLogger.pass("Mandatory field validation passed");
    }
}
