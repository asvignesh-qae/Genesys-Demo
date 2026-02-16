package com.vignesh.tests;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import org.openqa.selenium.JavascriptExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class PurchaseTestVanilla {

    private static final Logger logger = LogManager.getLogger(PurchaseTestVanilla.class);

    private WebDriver driver;
    private WebDriverWait wait;
    private String username;
    private String password;

    @BeforeMethod
    public void setUp() {
        logger.info("=== Setting up ChromeDriver ===");

        // Parse credentials from credential.json
        InputStream is = getClass().getClassLoader().getResourceAsStream("credential.json");
        Assert.assertNotNull(is, "credential.json not found in test/resources");
        JsonObject credentials = JsonParser.parseReader(
                new InputStreamReader(is, StandardCharsets.UTF_8)
        ).getAsJsonObject();
        username = credentials.get("username").getAsString();
        password = credentials.get("password").getAsString();
        logger.info("Credentials loaded from credential.json");

        // Initialize ChromeDriver with password manager disabled
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        options.setExperimentalOption("prefs", java.util.Map.of(
                "credentials_enable_service", false,
                "profile.password_manager_enabled", false,
                "profile.password_manager_leak_detection", false
        ));
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        logger.info("ChromeDriver initialized successfully");
    }

    @Test
    public void testPurchaseProcess() {
        // Step 1: Open the URL
        logger.info("Step 1: Opening URL - https://www.saucedemo.com/inventory.html");
        driver.get("https://www.saucedemo.com/inventory.html");

        // Step 2: Login - the site redirects to login page if not authenticated
        logger.info("Step 2: Logging in with credentials");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")));
        driver.findElement(By.id("user-name")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("login-button")).click();
        logger.info("Login submitted");

        // Step 3: Wait for inventory page to load
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("inventory_list")));
        logger.info("Step 3: Inventory page loaded successfully");

        // Step 4: Add "Sauce Labs Backpack" to cart
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        logger.info("Step 4: Added 'Sauce Labs Backpack' to cart");

        // Step 5: Add "Sauce Labs Fleece Jacket" to cart
        driver.findElement(By.id("add-to-cart-sauce-labs-fleece-jacket")).click();
        logger.info("Step 5: Added 'Sauce Labs Fleece Jacket' to cart");

        // Step 6: Validate cart badge shows "2"
        WebElement cartBadge = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.className("shopping_cart_badge"))
        );
        Assert.assertEquals(cartBadge.getText(), "2", "Cart badge should show 2 items");
        logger.info("Step 6: Cart badge validation passed - shows '{}'", cartBadge.getText());

        // Step 7: Go to cart
        driver.findElement(By.className("shopping_cart_link")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("cart_list")));
        logger.info("Step 7: Navigated to cart page");

        // Step 8: Click Checkout
        wait.until(ExpectedConditions.elementToBeClickable(By.id("checkout"))).click();
        logger.info("Step 8: Clicked Checkout button");

        // Step 9: Wait for checkout page to load, then fill information
        wait.until(ExpectedConditions.urlContains("checkout-step-one"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("first-name")));
        driver.findElement(By.id("first-name")).sendKeys("John");
        driver.findElement(By.id("last-name")).sendKeys("Doe");
        driver.findElement(By.id("postal-code")).sendKeys("12345");
        driver.findElement(By.id("continue")).click();
        logger.info("Step 9: Filled checkout information (John Doe, 12345) and continued");

        // Step 10: Finish checkout
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("finish")));
        driver.findElement(By.id("finish")).click();
        logger.info("Step 10: Clicked Finish button");

        // Step 11: Validate "Thank you for your order!" message
        WebElement thankYouMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.className("complete-header"))
        );
        Assert.assertEquals(
                thankYouMessage.getText(),
                "Thank you for your order!",
                "Order completion message should be displayed"
        );
        logger.info("Step 11: Order completion validation passed - '{}'", thankYouMessage.getText());

        logger.info("=== testPurchaseProcess passed successfully ===");
    }

    @Test
    public void testMandatoryFieldErrorAndFooter() {
        // Step 1: Open the URL (redirects to login page since not authenticated)
        logger.info("Step 1: Opening URL - https://www.saucedemo.com/inventory.html");
        driver.get("https://www.saucedemo.com/inventory.html");

        // Step 2: Click login button without entering any credentials
        wait.until(ExpectedConditions.elementToBeClickable(By.id("login-button"))).click();
        logger.info("Step 2: Clicked login button without entering credentials");

        // Step 3: Validate the error message for empty mandatory fields
        WebElement errorMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']"))
        );
        Assert.assertEquals(
                errorMessage.getText(),
                "Epic sadface: Username is required",
                "Error message should indicate username is required"
        );
        logger.info("Step 3: Error message validation passed - '{}'", errorMessage.getText());

        // Step 4: Login with standard_user / secret_sauce
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
        logger.info("Step 4: Logged in with standard_user");

        // Wait for inventory page to load
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("inventory_list")));
        logger.info("Inventory page loaded successfully");

        // Step 5: Scroll down to the bottom of the page
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        logger.info("Step 5: Scrolled to bottom of page");

        // Step 6: Validate the footer message contains "2026" and "Terms of Service"
        WebElement footer = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.className("footer_copy"))
        );
        String footerText = footer.getText();
        Assert.assertTrue(footerText.contains("2026"),
                "Footer should contain '2026'. Actual: " + footerText);
        Assert.assertTrue(footerText.contains("Terms of Service"),
                "Footer should contain 'Terms of Service'. Actual: " + footerText);
        logger.info("Step 6: Footer validation passed - '{}'", footerText);

        logger.info("=== testMandatoryFieldErrorAndFooter passed successfully ===");
    }

    @AfterMethod
    public void tearDown() {
        logger.info("=== Tearing down ChromeDriver ===");
        if (driver != null) {
            driver.quit();
        }
        logger.info("ChromeDriver closed");
    }
}
