package com.vignesh.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Set;

public class Guru99HomePage extends BasePage {

    private static final By BODY = By.tagName("body");
    private static final By IFRAME = By.xpath("//iframe[@id='a077aa5e']");
    private static final By IMAGE_IN_IFRAME = By.xpath("//img[contains(@src, 'Jmeter')]");
    private static final By TESTING_MENU = By.xpath("//ul[contains(@class,'gf-menu')]//a[contains(text(),'Testing')]");
    private static final By SELENIUM_LINK = By.xpath("//ul[contains(@class,'gf-menu')]//ul[@class='l2']//a[contains(text(),'Selenium')]");

    private String mainWindowHandle;

    public Guru99HomePage(WebDriver driver) {
        super(driver);
    }

    public Guru99HomePage open(String url) {
        logger.info("Opening URL: {}", url);
        driver.get(url);
        waitForVisible(BODY);
        mainWindowHandle = driver.getWindowHandle();
        logger.info("Main window handle: {}", mainWindowHandle);
        return this;
    }

    public Guru99HomePage switchToIframe() {
        logger.info("Switching to iframe");
        WebElement iframe = waitForVisible(IFRAME);
        driver.switchTo().frame(iframe);
        logger.info("Switched to iframe successfully");
        return this;
    }

    public Guru99HomePage validateIfOnGuru99HomePage() {
        logger.info("Validate if on Guru99 Home Page by checking url");
        waitForUrlContains("guru99home");
        logger.info("User is on Guru99 Home Page");
        return this;
    }

    public Guru99HomePage clickImageInIframe() {
        logger.info("Clicking image inside iframe");
        WebElement img = waitForVisible(IMAGE_IN_IFRAME);
        WebElement link = img.findElement(By.xpath("./ancestor::a"));
        String href = link.getAttribute("href");
        logger.info("Image link href: {}", href);
        ((JavascriptExecutor) driver).executeScript("window.open(arguments[0])", href);
        return this;
    }

    public Guru99HomePage switchToDefaultContent() {
        driver.switchTo().defaultContent();
        logger.info("Switched back to default content");
        return this;
    }

    public Guru99HomePage waitForNewTab() {
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        logger.info("New tab opened");
        return this;
    }

    public String switchToNewTabAndGetTitle() {
        Set<String> allWindows = driver.getWindowHandles();
        logger.info("Number of open windows/tabs: {}", allWindows.size());

        for (String windowHandle : allWindows) {
            if (!windowHandle.equals(mainWindowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }

        wait.until(ExpectedConditions.titleContains("Selenium Live Project"));
        String title = driver.getTitle();
        logger.info("New tab title: '{}'", title);
        return title;
    }

    public Guru99HomePage closeCurrentTabAndSwitchToMain() {
        logger.info("Closing current tab and switching back to main window");
        driver.close();
        driver.switchTo().window(mainWindowHandle);
        logger.info("Switched back to main window: {}", driver.getTitle());
        return this;
    }

    public Guru99HomePage hoverOnTestingMenu() {
        logger.info("Hovering on Testing menu");
        WebElement testingMenu = waitForVisible(TESTING_MENU);
        Actions actions = new Actions(driver);
        actions.moveToElement(testingMenu).perform();
        logger.info("Hovered over Testing menu item");
        return this;
    }

    public Guru99HomePage clickSeleniumLink() {
        logger.info("Clicking Selenium link");
        click(SELENIUM_LINK);
        return this;
    }

    public String getMainWindowHandle() {
        return mainWindowHandle;
    }
}
