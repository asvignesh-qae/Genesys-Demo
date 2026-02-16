package com.vignesh.utils;

import com.vignesh.driver.DriverFactory;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.util.Base64;

/**
 * Logs messages to both Log4j and Allure so that
 * every step and log entry appears in the Allure report.
 * Automatically attaches an inline screenshot when a WebDriver is active.
 */
public final class ReportLogger {

    private ReportLogger() {
    }

    private static final Logger logger = LogManager.getLogger(ReportLogger.class);

    public static void step(String message) {
        logger.info(message);
        Allure.step(message);
        attachScreenshot(message);
    }

    public static void pass(String message) {
        logger.info(message);
        Allure.step("[PASS] " + message);
        attachScreenshot(message);
    }

    public static void fail(String message) {
        logger.error(message);
        Allure.step("[FAIL] " + message);
        attachScreenshot(message);
    }

    public static void warn(String message) {
        logger.warn(message);
        Allure.step("[WARN] " + message);
        attachScreenshot(message);
    }

    private static void attachScreenshot(String name) {
        try {
            WebDriver driver = DriverFactory.getDriver();
            if (driver != null) {
                String base64 = ScreenshotUtils.captureAsBase64(driver);
                if (base64 != null) {
                    Allure.addAttachment(name, "image/png",
                            new ByteArrayInputStream(Base64.getDecoder().decode(base64)),
                            ".png");
                }
            }
        } catch (Exception e) {
            logger.debug("Could not capture screenshot: {}", e.getMessage());
        }
    }
}
