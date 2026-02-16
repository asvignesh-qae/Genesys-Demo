package com.vignesh.tests;

import com.vignesh.base.BaseTest;
import com.vignesh.pages.Guru99HomePage;
import com.vignesh.utils.ReportLogger;
import com.vignesh.utils.RetryAnalyzer;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FrameAndTabHandlingTest extends BaseTest {

    private static final String GURU99_URL = "http://demo.guru99.com/test/guru99home";
    private static final String EXPECTED_NEW_TAB_TITLE = "Selenium Live Project for Practice";

    @Test(groups = {"regression", "testCase4"}, retryAnalyzer = RetryAnalyzer.class)
    public void testIFrameAndTabHandling() throws InterruptedException {
        ReportLogger.step("Step 1: Open the URL");
        Guru99HomePage guru99HomePage = new Guru99HomePage(getDriver());
        guru99HomePage.open(GURU99_URL);
        guru99HomePage.validateIfOnGuru99HomePage();

        ReportLogger.step("Step 2: Switch to iframe and click image inside it");
        guru99HomePage.switchToIframe()
                .clickImageInIframe()
                .switchToDefaultContent();

        ReportLogger.step("Step 3: Verify new tab is loaded with expected title");
        guru99HomePage.waitForNewTab();
        String newTabTitle = guru99HomePage.switchToNewTabAndGetTitle();
        Assert.assertEquals(newTabTitle, EXPECTED_NEW_TAB_TITLE,
                "New tab should have the expected title. Actual: " + newTabTitle);
        ReportLogger.pass("Title verification passed");

        ReportLogger.step("Step 4: Close the new tab and switch back to main window");
        guru99HomePage.closeCurrentTabAndSwitchToMain();

        ReportLogger.step("Step 5: Hover on Testing menu and click Selenium link");
        guru99HomePage.hoverOnTestingMenu()
                .clickSeleniumLink();

        ReportLogger.pass("All validations passed successfully");
    }
}
