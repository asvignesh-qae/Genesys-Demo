package com.vignesh.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

public class RichTextEditorPage extends BasePage {

    private static final By EDITOR = By.cssSelector("div.ck-editor__editable[contenteditable='true']");
    private static final By BOLD_BUTTON = By.xpath("//button[@data-cke-tooltip-text='Bold (Ctrl+B)']");
    private static final By UNDERLINE_BUTTON = By.xpath("//button[@data-cke-tooltip-text='Underline (Ctrl+U)']");
    private static final By COOKIE_DENY_BUTTON = By.cssSelector("#ch2-dialog .ch2-deny-all-btn");

    public RichTextEditorPage(WebDriver driver) {
        super(driver);
    }

    public RichTextEditorPage open(String url) {
        logger.info("Opening URL: {}", url);
        driver.get(url);
        return this;
    }

    public RichTextEditorPage dismissCookieDialog() {
        logger.info("Dismissing cookie consent dialog if present");
        try {
            WebElement denyButton = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(COOKIE_DENY_BUTTON));
            denyButton.click();
            logger.info("Cookie dialog dismissed");
        } catch (Exception e) {
            logger.info("No cookie dialog found, continuing");
        }
        return this;
    }

    public RichTextEditorPage waitForEditorToLoad() {
        logger.info("Waiting for CKEditor to load");
        waitForClickable(EDITOR);
        waitForClickable(BOLD_BUTTON);
        waitForClickable(UNDERLINE_BUTTON);
        logger.info("CKEditor loaded successfully");
        return this;
    }

    public RichTextEditorPage validateIfOnRichTextEditorPage() {
        waitForUrlContains("onlinehtmleditor");
        logger.info("User is on OnlineHtmlEditor page");
        Assert.assertEquals(getPageTitle(), "Free online HTML editor - Word to HTML | Onlinehtmleditor.dev",
                "Page title should be `Free online HTML editor - Word to HTML | Onlinehtmleditor.dev`");
        logger.info("OnlineHtmlEditor page title is correct");
        return this;
    }

    public RichTextEditorPage toggleBold() {
        logger.info("Toggling bold formatting");
        click(BOLD_BUTTON);
        return this;
    }

    public RichTextEditorPage toggleUnderline() {
        logger.info("Toggling underline formatting");
        click(UNDERLINE_BUTTON);
        return this;
    }

    public String getBoldButtonState() {
        return waitForVisible(BOLD_BUTTON).getAttribute("aria-pressed");
    }

    public String getUnderlineButtonState() {
        return waitForVisible(UNDERLINE_BUTTON).getAttribute("aria-pressed");
    }

    public RichTextEditorPage clickEditor() {
        waitForClickable(EDITOR).click();
        return this;
    }

    public RichTextEditorPage typeText(String text) {
        logger.info("Typing text: '{}'", text);
        waitForVisible(EDITOR).sendKeys(text);
        return this;
    }

    public RichTextEditorPage typeSpace() {
        waitForVisible(EDITOR).sendKeys(Keys.SPACE);
        return this;
    }

    public String getEditorText() {
        String text = waitForVisible(EDITOR).getText().replaceAll("[^a-zA-Z ]", "").trim();
        logger.info("Editor text: '{}'", text);
        return text;
    }

    public String getEditorHtml() {
        WebElement editor = waitForVisible(EDITOR);
        Object htmlResult = ((JavascriptExecutor) driver).executeScript(
                "return arguments[0].innerHTML;", editor);
        String html = htmlResult != null ? htmlResult.toString() : "";
        logger.debug("Editor innerHTML: {}", html);
        return html;
    }

    public boolean isTextBold(String text) {
        String html = getEditorHtml();
        return html.contains("<strong>" + text + "</strong>");
    }

    public boolean isTextUnderlined(String text) {
        String html = getEditorHtml();
        return html.contains("<u>") && html.contains(text + "</u>");
    }

    public boolean hasNoFormatting(String text) {
        String html = getEditorHtml();
        return !html.contains("<strong>" + text) && !html.contains("<u>" + text);
    }
}
