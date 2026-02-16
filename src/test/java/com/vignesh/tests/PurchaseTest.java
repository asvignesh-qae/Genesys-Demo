package com.vignesh.tests;

import com.google.gson.JsonObject;
import com.vignesh.base.BaseTest;
import com.vignesh.config.ConfigReader;
import com.vignesh.pages.*;
import com.vignesh.utils.JsonDataReader;
import com.vignesh.utils.ReportLogger;
import com.vignesh.utils.RetryAnalyzer;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PurchaseTest extends BaseTest {

    @Test(groups = {"regression", "testCase1"}, retryAnalyzer = RetryAnalyzer.class)
    public void testEndToEndPurchase() {
        JsonObject credentials = JsonDataReader.readJsonFile("credential.json");
        String username = credentials.get("username").getAsString();
        String password = credentials.get("password").getAsString();

        ReportLogger.step("Step 1: Navigate to login page and login");
        getDriver().get(ConfigReader.getBaseUrl());
        LoginPage loginPage = new LoginPage(getDriver());
        InventoryPage inventoryPage = loginPage.loginAs(username, password);
        inventoryPage.waitForPageLoad();
        inventoryPage.validateOnInventoryPage();

        ReportLogger.step("Step 2: Add items to cart");
        inventoryPage.addItemToCart("sauce labs backpack");
        inventoryPage.addItemToCart("sauce labs fleece jacket");

        ReportLogger.step("Step 3: Verify cart badge shows 2 items");
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), "2",
                "Cart badge should show 2 items");
        ReportLogger.pass("Cart badge count verified: 2");

        ReportLogger.step("Step 4: Go to cart and verify item count");
        CartPage cartPage = inventoryPage.goToCart();
        cartPage.validateIfOnCartPage();
        Assert.assertEquals(cartPage.getCartItemCount(), 2,
                "Cart should contain 2 items");
        ReportLogger.pass("Cart contains 2 items");

        ReportLogger.step("Step 5: Proceed through checkout");
        CheckOutStepOnePage checkoutStepOnePage = cartPage.proceedToCheckout();
        CheckOutStepTwoPage checkOutStepTwoPage = checkoutStepOnePage
                .fillInfoAndContinue("Liszt", "Franz", "12345");
        checkOutStepTwoPage.validateIfOnCheckoutStepTwoPage();
        checkOutStepTwoPage.isSummaryDisplayed();
        CheckoutCompletePage completePage = checkOutStepTwoPage.finishCheckout();

        ReportLogger.step("Step 6: Verify order completion message");
        Assert.assertEquals(
                completePage.getSuccessMessage(),
                "Thank you for your order!",
                "Order completion message should be displayed"
        );
        ReportLogger.pass("End-to-end purchase test passed");
    }
}
