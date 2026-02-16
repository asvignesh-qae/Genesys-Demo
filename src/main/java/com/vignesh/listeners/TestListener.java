package com.vignesh.listeners;

import com.vignesh.driver.DriverFactory;
import com.vignesh.utils.ScreenshotUtils;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;
import java.util.Base64;

public class TestListener implements ITestListener {

    private static final Logger logger = LogManager.getLogger(TestListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        logger.info("Test started: {}", testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test PASSED: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        logger.error("Test FAILED: {}", testName, result.getThrowable());

        if (DriverFactory.getDriver() != null) {
            String base64 = ScreenshotUtils.captureAsBase64(DriverFactory.getDriver());
            if (base64 != null) {
                Allure.addAttachment("Failure Screenshot",
                        "image/png",
                        new ByteArrayInputStream(Base64.getDecoder().decode(base64)),
                        ".png");
            }
            ScreenshotUtils.capture(DriverFactory.getDriver(), testName);
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("Test SKIPPED: {}", result.getMethod().getMethodName());
    }
}
