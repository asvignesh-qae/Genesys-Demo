package com.vignesh.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class InventoryPage extends BasePage {

    private static final By INVENTORY_LIST = By.className("inventory_list");
    private static final By CART_BADGE = By.className("shopping_cart_badge");
    private static final By CART_LINK = By.className("shopping_cart_link");
    private static final By FOOTER_COPY = By.className("footer_copy");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public boolean isPageLoaded() {
        return isDisplayed(INVENTORY_LIST);
    }

    public InventoryPage waitForPageLoad() {
        waitForVisible(INVENTORY_LIST);
        logger.info("Inventory page loaded");
        return this;
    }

    public InventoryPage validateOnInventoryPage() {
        waitForUrlContains("inventory");
        logger.info("User is on Inventory page");
        Assert.assertEquals(getPageTitle(), "Swag Labs", "/inventory Page title should be `Swag Labs`");
        logger.info("Inventory page title is correct");
        return this;
    }

    public InventoryPage addItemToCart(String itemName) {
        String itemId = "add-to-cart-" + itemName.toLowerCase().replace(" ", "-");
        By addButton = By.id(itemId);
        click(addButton);
        logger.info("Added '{}' to cart", itemName);
        return this;
    }

    public String getCartBadgeCount() {
        String count = getText(CART_BADGE);
        logger.info("Cart badge count: {}", count);
        return count;
    }

    public CartPage goToCart() {
        click(CART_LINK);
        logger.info("Navigated to Cart page");
        return new CartPage(driver);
    }

    public String getFooterText() {
        scrollToBottom();
        String footer = getText(FOOTER_COPY);
        logger.info("Footer text: {}", footer);
        return footer;
    }
}
