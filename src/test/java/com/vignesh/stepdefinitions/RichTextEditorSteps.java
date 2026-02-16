package com.vignesh.stepdefinitions;

import com.vignesh.driver.DriverFactory;
import com.vignesh.pages.RichTextEditorPage;
import com.vignesh.utils.ScreenshotUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

public class RichTextEditorSteps {

    private static final Logger logger = LogManager.getLogger(RichTextEditorSteps.class);

    private RichTextEditorPage editorPage;

    private RichTextEditorPage getEditorPage() {
        if (editorPage == null) {
            editorPage = new RichTextEditorPage(DriverFactory.getDriver());
        }
        return editorPage;
    }

    @And("user waits for the editor to load")
    public void userWaitsForTheEditorToLoad() {
        logger.info("Waiting for CKEditor to load");
        getEditorPage().dismissCookieDialog();
        getEditorPage().waitForEditorToLoad();
        logger.info("CKEditor loaded successfully");
    }

    @When("user types {string} in bold format in the editor")
    public void userTypesTextInBoldFormatInTheEditor(String text) {
        logger.info("Typing '{}' in bold format", text);
        getEditorPage().toggleBold();
        Assert.assertEquals(getEditorPage().getBoldButtonState(), "true",
                "Bold button should be active after clicking");
        getEditorPage().clickEditor().typeText(text);
        getEditorPage().toggleBold();
        Assert.assertEquals(getEditorPage().getBoldButtonState(), "false",
                "Bold button should be inactive after clicking");
        logger.info("Typed '{}' in bold format", text);
    }

    @When("user types {string} in underline format in the editor")
    public void userTypesTextInUnderlineFormatInTheEditor(String text) {
        logger.info("Typing '{}' in underline format", text);
        getEditorPage().toggleUnderline();
        Assert.assertEquals(getEditorPage().getUnderlineButtonState(), "true",
                "Underline button should be active after clicking");
        getEditorPage().typeText(text);
        getEditorPage().toggleUnderline();
        Assert.assertEquals(getEditorPage().getUnderlineButtonState(), "false",
                "Underline button should be inactive after clicking");
        logger.info("Typed '{}' in underline format", text);
    }

    @And("user types {string} in normal format in the editor")
    public void userTypesTextInNormalFormatInTheEditor(String text) {
        logger.info("Typing '{}' in normal format", text);
        getEditorPage().typeText(text);
        logger.info("Typed '{}' in normal format", text);
    }

    @Then("the text {string} should be visible in the rich text editor")
    public void theTextShouldBeVisibleInTheRichTextEditor(String expectedText) {
        logger.info("Validating editor text: '{}'", expectedText);
        String editorText = getEditorPage().getEditorText();
        Assert.assertEquals(editorText, expectedText,
                "Editor should contain '" + expectedText + "'. Actual: " + editorText);
        ScreenshotUtils.attachScreenshotToAllure(DriverFactory.getDriver(), "Editor text verification");
        logger.info("Text validation passed: '{}'", editorText);
    }

    @And("{string} text should be in bold format")
    public void textShouldBeInBoldFormat(String text) {
        logger.info("Validating '{}' is bold", text);
        String editorHtml = getEditorPage().getEditorHtml();
        Assert.assertTrue(getEditorPage().isTextBold(text),
                "'" + text + "' should be bold. Actual HTML: " + editorHtml);
        logger.info("Bold validation passed: '{}' is wrapped in <strong> tags", text);
    }

    @And("{string} text should be in underline format")
    public void textShouldBeInUnderlineFormat(String text) {
        logger.info("Validating '{}' is underlined", text);
        String editorHtml = getEditorPage().getEditorHtml();
        Assert.assertTrue(getEditorPage().isTextUnderlined(text),
                "'" + text + "' should be underlined. Actual HTML: " + editorHtml);
        logger.info("Underline validation passed: '{}' is wrapped in <u> tags", text);
    }

    @And("{string} text should have no formatting")
    public void textShouldHaveNoFormatting(String text) {
        logger.info("Validating '{}' has no formatting", text);
        String editorHtml = getEditorPage().getEditorHtml();
        Assert.assertTrue(getEditorPage().hasNoFormatting(text),
                "'" + text + "' should not have any formatting. Actual HTML: " + editorHtml);
        logger.info("No-formatting validation passed: '{}' has no bold or underline tags", text);
    }
}
