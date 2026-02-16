package com.vignesh.pages;

import com.vignesh.config.ConfigReader;
import com.vignesh.utils.ReportLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

public abstract class BasePage {

    protected final Logger logger = LogManager.getLogger(getClass());
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver,
                Duration.ofSeconds(ConfigReader.getExplicitWait()));
    }

    protected void click(By locator) {
        ReportLogger.step("Click on element: " + locator);
        scrollToElement(locator);
        waitForClickable(locator).click();
    }

    protected void type(By locator, String text) {
        ReportLogger.step("Type '" + text + "' into element: " + locator);
        scrollToElement(locator);
        WebElement element = waitForVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(By locator) {
        String text = waitForVisible(locator).getText();
        ReportLogger.step("Got text '" + text + "' from element: " + locator);
        return text;
    }

    protected boolean isDisplayed(By locator) {
        try {
            boolean displayed = waitForVisible(locator).isDisplayed();
            ReportLogger.step("Element is displayed: " + locator);
            return displayed;
        } catch (Exception e) {
            ReportLogger.step("Element is NOT displayed: " + locator);
            return false;
        }
    }

    protected WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected List<WebElement> waitForAllVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    protected void waitForUrlContains(String url) {
        wait.until(ExpectedConditions.urlContains(url));
        Assert.assertTrue(Objects.requireNonNull(driver.getCurrentUrl()).contains(url),
        "Expected URL to contain '" + url + "' but was '" + driver.getCurrentUrl() + "'");

    }

    public void scrollToBottom() {
        ReportLogger.step("Scroll to bottom of page");
        ((JavascriptExecutor) driver).executeScript(
                "window.scrollTo(0, document.body.scrollHeight)");
    }

    public void scrollToElement(By locator) {
        ReportLogger.step("Scroll to element: " + locator);
        WebElement element = waitForVisible(locator);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
