package com.vignesh.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;

public class CartPage extends BasePage {

    private static final By CART_LIST = By.className("cart_list");
    private static final By CART_ITEMS = By.className("cart_item");
    private static final By CHECKOUT_BUTTON = By.id("checkout");
    private static final By CONTINUE_SHOPPING = By.id("continue-shopping");

    public CartPage(WebDriver driver) {
        super(driver);
        waitForVisible(CART_LIST);
        logger.info("Cart page loaded");
    }

    public CartPage validateIfOnCartPage() {
        waitForUrlContains("cart");
        logger.info("User is on Cart page");
        Assert.assertEquals(getPageTitle(), "Swag Labs", "/cart Page title should be `Swag Labs`");
        logger.info("Cart page title is correct");
        return this;
    }

    public int getCartItemCount() {
        List<WebElement> items = waitForAllVisible(CART_ITEMS);
        logger.info("Cart contains {} items", items.size());
        return items.size();
    }

    public CheckOutStepOnePage proceedToCheckout() {
        click(CHECKOUT_BUTTON);
        logger.info("Proceeded to checkout");
        return new CheckOutStepOnePage(driver);
    }

    public InventoryPage continueShopping() {
        click(CONTINUE_SHOPPING);
        return new InventoryPage(driver);
    }
}
