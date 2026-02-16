package com.vignesh.tests;

import com.vignesh.base.BaseTest;
import com.vignesh.pages.RichTextEditorPage;
import com.vignesh.utils.ReportLogger;
import com.vignesh.utils.RetryAnalyzer;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RichTextEditorTest extends BaseTest {

    private static final String EDITOR_URL = "https://onlinehtmleditor.dev";

    @Test(groups = {"regression", "testCase3"}, retryAnalyzer = RetryAnalyzer.class)
    public void testRichTextEditorFormatting() {
        ReportLogger.step("Step 1: Open the URL");
        RichTextEditorPage editorPage = new RichTextEditorPage(getDriver());
        editorPage.open(EDITOR_URL);

        ReportLogger.step("Step 2: Wait for CKEditor to load");
        editorPage.dismissCookieDialog();
        editorPage.waitForEditorToLoad();
        editorPage.validateIfOnRichTextEditorPage();

        ReportLogger.step("Step 3: Type 'Automation' in bold format");
        editorPage.toggleBold();
        Assert.assertEquals(editorPage.getBoldButtonState(), "true",
                "Bold button should be active after clicking");
        ReportLogger.pass("Bold button enabled (aria-pressed=true)");

        editorPage.clickEditor()
                .typeText("Automation");
        ReportLogger.pass("Typed 'Automation' in bold format");

        editorPage.toggleBold();
        Assert.assertEquals(editorPage.getBoldButtonState(), "false",
                "Bold button should be inactive after clicking");
        ReportLogger.pass("Bold button disabled (aria-pressed=false)");

        editorPage.typeSpace();

        ReportLogger.step("Step 4: Type 'Test' in underline format");
        editorPage.toggleUnderline();
        Assert.assertEquals(editorPage.getUnderlineButtonState(), "true",
                "Underline button should be active after clicking");
        ReportLogger.pass("Underline button enabled (aria-pressed=true)");

        editorPage.typeText("Test");
        ReportLogger.pass("Typed 'Test' in underline format");

        editorPage.toggleUnderline();
        Assert.assertEquals(editorPage.getUnderlineButtonState(), "false",
                "Underline button should be inactive after clicking");
        ReportLogger.pass("Underline button disabled (aria-pressed=false)");

        editorPage.typeText(" Example");
        ReportLogger.pass("Typed ' Example' in normal format");

        ReportLogger.step("Step 5: Validate text and formatting");
        String editorText = editorPage.getEditorText();
        Assert.assertEquals(editorText, "Automation Test Example",
                "Editor should contain 'Automation Test Example'. Actual: " + editorText);
        ReportLogger.pass("Text validation passed: '" + editorText + "'");

        String editorHtml = editorPage.getEditorHtml();
        Assert.assertNotNull(editorHtml, "Editor innerHTML should not be null");

        Assert.assertTrue(editorPage.isTextBold("Automation"),
                "'Automation' should be bold. Actual HTML: " + editorHtml);
        ReportLogger.pass("Bold validation passed: 'Automation' is wrapped in <strong> tags");

        Assert.assertTrue(editorPage.isTextUnderlined("Test"),
                "'Test' should be underlined. Actual HTML: " + editorHtml);
        ReportLogger.pass("Underline validation passed: 'Test' is wrapped in <u> tags");

        Assert.assertTrue(editorPage.hasNoFormatting("Example"),
                "'Example' should not have any formatting. Actual HTML: " + editorHtml);
        ReportLogger.pass("No-formatting validation passed: 'Example' has no bold or underline tags");

        ReportLogger.pass("All validations passed successfully");
    }
}
